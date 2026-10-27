package farn.armor_stand.world.inventory;

import farn.armor_stand.world.ArmorStandEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;

import java.util.Arrays;

public class ArmorStandInventory implements Inventory {
    public ItemStack[] equipments = new ItemStack[5];
    public ArmorStandEntity entity;

    public ArmorStandInventory() {
    }

    public ArmorStandInventory(ArmorStandEntity entity) {
        this.entity = entity;
    }

    public void clear() {
        Arrays.fill(equipments, null);
    }

    public boolean putEquipment(PlayerEntity player, int slot) {
        ItemStack occupiedItem = getStack(slot);

        int selectedSlot = player.inventory.selectedSlot;
        ItemStack held = player.inventory.getStack(selectedSlot);

        if (held != null && held.count > 1) {
            if (occupiedItem == null) {
                ItemStack heldCopy = held.copy();
                heldCopy.count = 1;
                this.setStack(slot, heldCopy);
                held.count--;
            }
        } else {
            this.setStack(slot, held);
            player.inventory.setStack(selectedSlot, occupiedItem);
        }
        return true;
    }

    @Override
    public int size() {
        return equipments.length;
    }

    @Override
    public ItemStack getStack(int slot) {
        if(slot >= 0 && slot < equipments.length)
            return equipments[slot];
        else
            return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (this.equipments[slot] != null) {
            ItemStack splitOff;
            if (this.equipments[slot].count <= amount) {
                splitOff = this.equipments[slot];
                this.equipments[slot] = null;
            } else {
                splitOff = this.equipments[slot].split(amount);
                if (this.equipments[slot].count == 0) {
                    this.equipments[slot] = null;
                }
            }

            if(entity != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                PacketHelper.sendToAllTracking(this.entity, new StationEntityEquipmentUpdateS2CPacket(this.entity.id, slot, equipments[slot]));
            }

            return splitOff;
        } else {
            return null;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot < 0 || slot >= equipments.length) return;
        equipments[slot] = stack;
        if (stack != null && stack.count > this.getMaxCountPerStack()) {
            stack.count = this.getMaxCountPerStack();
        }

        if(entity != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(this.entity, new StationEntityEquipmentUpdateS2CPacket(this.entity.id, slot, equipments[slot]));
        }
    }

    @Override
    public String getName() {
        return "Armor Stand";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return entity != null && entity.isAlive() && player.getSquaredDistance(entity) < 64;
    }
}
