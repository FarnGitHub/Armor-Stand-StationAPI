package farn.armor_stand.world;

import farn.armor_stand.ArmorStandMain;
import farn.armor_stand.event.MapItemToSlotEvent;
import farn.armor_stand.util.Util;
import farn.armor_stand.world.inventory.ArmorStandInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

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

    public boolean interact(PlayerEntity player, double yHit) {
        if (!world.isRemote) {
            ItemStack held = player.getHand();
            byte armorSlot = StationAPI.EVENT_BUS.post(
                    new MapItemToSlotEvent(player, this.inventory, held)
            ).slot;


            byte slot = 0;

            if (yHit > 0.1D && yHit < 0.55D && inventory.getStack(1) != null)
                slot = 1;
            else if (yHit >= 0.9D && yHit < 1.6D && inventory.getStack(3) != null)
                slot = 3;
            else if (yHit >= 0.4D && yHit < 1.2D && inventory.getStack(2) != null)
                slot = 2;
            else if (yHit >= 1.6D && inventory.getStack(4) != null)
                slot = 4;

            if (held != null)
                return inventory.putEquipment(player, armorSlot);
            else if (inventory.getStack(slot) != null)
                return inventory.putEquipment(player, slot);
        }
        return false;
    }

    private void damageArmorStand(int amount) {
        int fakeHP = health;
        fakeHP -= amount;

        if (fakeHP <= 1)
            this.breakArmorStand();
        else {
            this.world.broadcastEntityEvent(this, (byte)0);
            this.world.playSound(this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.health -= amount;
        }
    }

    public void breakArmorStand() {
        if(!world.isRemote) this.world.broadcastEntityEvent(this, (byte)1);
        this.world.playSound(this, this.getDeathSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        this.markDead();
    }

    @Override
    public boolean damage(Entity source, int amount) {
        if(world.isRemote) return false;

        if(source instanceof PlayerEntity)
            this.breakArmorStand();
        else
            this.damageArmorStand(amount);

        return false;
    }

    @Override
    public void markDead() {
        if(!world.isRemote) {
            this.dropItem(ArmorStandMain.ARMOR_STAND_ITEM.id, 1);
            for(int index = 0; index < inventory.size(); index++) {
                ItemStack stack = inventory.getStack(index);
                if(stack != null) {
                    this.dropItem(stack, 0.0F);
                    inventory.setStack(index, null);
                }
            }
        }
        super.markDead();
    }

    @Override
    public boolean interact(PlayerEntity player) {
        if (world.isRemote) return true;
        else if(ArmorStandMain.isGuiMode()) {
            return Util.openScreen(player, this);
        } else {
            float yOffset = player.getEyeHeight();
            if(Util.getEnvType() == EnvType.SERVER) yOffset += 0.12F;
            Vec3d hit = Util.getHitVector(player, this, 32D, yOffset);
            double yDiff = hit != null ? hit.y - this.boundingBox.minY : 0D;
            return interact(player, yDiff);
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

    @Environment(EnvType.CLIENT)
    public void processServerEntityStatus(byte status) {
        if (status == 0) {
            this.world.playSound(this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        } else if (status == 1) {
            this.breakArmorStand();
        }
    }
}
