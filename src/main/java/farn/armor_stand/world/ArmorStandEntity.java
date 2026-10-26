package farn.armor_stand.world;

import farn.armor_stand.ArmorStandMain;
import farn.armor_stand.event.MapItemToSlotEvent;
import farn.armor_stand.world.inventory.ArmorStandInventory;
import farn.armor_stand.world.inventory.ArmorStandScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Arrays;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 3)
public class ArmorStandEntity extends LivingEntity implements MobSpawnDataProvider {
    public final ArmorStandInventory inventory;

    public ArmorStandEntity(World world) {
        super(world);
        this.texture = "/assets/armor_stand/textures/entity/armor_stand.png";
        this.setBoundingBoxSpacing(0.5F, 1.975F);
        this.inventory = new ArmorStandInventory(this);
    }

    public ArmorStandEntity(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
    }

    public boolean interact(PlayerEntity player, Vec3d hitPos) {
        if (!world.isRemote) {
            ItemStack held = player.getHand();
            byte armorSlot = StationAPI.EVENT_BUS.post(
                    new MapItemToSlotEvent(player, this.inventory, held)
            ).slot;


            byte slot = 0;
            double yHit = hitPos.y;

            if (yHit >= 0D && yHit < 0.3D && inventory.getStack(1) != null)
                slot = 1;
            else if (yHit >= 0.6D && yHit < 1.6D && inventory.getStack(3) != null)
                slot = 3;
            else if (yHit >= 0.3D && yHit < 1.05D && inventory.getStack(2) != null)
                slot = 2;
            else if (yHit >= 1.6D && inventory.getStack(4) != null)
                slot = 4;

            if (held != null)
                return inventory.putEquipment(player, armorSlot);
            else if (inventory.getStack(slot) != null)
                return inventory.putEquipment(player, slot);
        }
        return true;
    }

    private void damageArmorStand(int amount) {
        int fakeHP = health;
        fakeHP -= amount;

        if (fakeHP <= 1F) {
            breakArmorStand();
        } else {
            this.world.playSound(this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.health -= amount;
        }
    }

    public void breakArmorStand() {
        this.world.playSound(this, this.getDeathSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        this.dropItems();
        this.markDead();
    }

    private void providerEntityId(MessagePacket msg) {
        if(msg.ints == null) msg.ints = new int[1];
        else msg.ints = Arrays.copyOf(msg.ints, msg.ints.length + 1);
        msg.ints[msg.ints.length - 1] = this.id;
    }

    @Override
    public boolean damage(Entity source, int amount) {
        if(world.isRemote) {
            this.world.playSound(this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            return false;
        }

        if(source instanceof PlayerEntity) {
            this.breakArmorStand();
        } else {
            this.damageArmorStand(amount);
        }

        return false;
    }

    @Override
    protected void dropItems() {
        super.dropItems();
        this.dropItem(ArmorStandMain.ARMOR_STAND_ITEM.id, 1);
        for(int index = 0; index < inventory.size(); index++) {
            ItemStack stack = inventory.getStack(index);
            if(stack != null) {
                this.dropItem(stack, 0.0F);
                inventory.setStack(index, null);
            }
        }
    }

    @Override
    public boolean interact(PlayerEntity player) {
        if(ArmorStandMain.isGuiMode()) {
            if(!world.isRemote)
                GuiHelper.openGUI(
                        player,
                        ArmorStandMain.id("armor_stand_screen"),
                        inventory,
                        new ArmorStandScreenHandler(player.inventory, inventory),
                        this::providerEntityId
                );
            return true;
        } else {
            float distance = player.getDistance(this);
            Vec3d origin = Vec3d.createCached(player.x, player.y + player.getEyeHeight(), player.z);
            Vec3d lookV = player.getLookVector();
            Vec3d hitV = origin.add(lookV.x * distance, lookV.y * distance, lookV.z * distance);
            Vec3d hitDiff = Vec3d.createCached(hitV.x - this.x, hitV.y - this.y, hitV.z - this.z);
            return interact(player, hitDiff);
        }
    }

    @Environment(EnvType.CLIENT)
    public void setEquipmentStack(int armorSlot, int itemId, int meta) {
        inventory.setStack(armorSlot, itemId > 0 ? new ItemStack(itemId, 1, meta) : null);
    }

    @Override
    @Environment(EnvType.SERVER)
    public ItemStack[] getEquipment() {
        return inventory.equipments;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtList list = new NbtList();

        for(int slot = 0; slot < inventory.size(); ++slot) {
            ItemStack stack = inventory.getStack(slot);
            if (stack != null) {
                NbtCompound data = new NbtCompound();
                data.putByte("Slot", (byte)slot);
                stack.writeNbt(data);
                list.add(data);
            }
        }

        nbt.put("Items", list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtList list = nbt.getList("Items");
        inventory.clear();

        for(int slotI = 0; slotI < list.size(); ++slotI) {
            NbtCompound com = (NbtCompound)list.get(slotI);
            int slot = com.getByte("Slot") & 255;
            if (slot < inventory.size()) {
                inventory.setStack(slot, new ItemStack(com));
            }
        }
    }

    @Override
    protected String getRandomSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return "armor_stand:entity.armor_stand_hit";
    }

    @Override
    protected String getDeathSound() {
        return "armor_stand:entity.armor_stand_break";
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void tryDespawn() {
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    public void addVelocity(double x, double y, double z) {
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    protected void onLanding(float fallDistance) {
        super.onLanding(0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.bodyYaw = this.yaw;
        this.lastBodyYaw = this.prevYaw;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ArmorStandMain.id("armor_stand");
    }
}
