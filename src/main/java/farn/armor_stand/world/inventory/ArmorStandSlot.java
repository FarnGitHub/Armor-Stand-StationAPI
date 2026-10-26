package farn.armor_stand.world.inventory;

import farn.armor_stand.event.MapItemToSlotEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.StationAPI;

public class ArmorStandSlot extends Slot {
    public ArmorStandInventory armorStandInventory;

    public ArmorStandSlot(ArmorStandInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.armorStandInventory = (ArmorStandInventory)this.inventory;
    }

    public boolean canInsert(ItemStack stack) {
        return findSlotFor(stack) == this.index;
    }

    public int findSlotFor(ItemStack stack) {
        return StationAPI.EVENT_BUS.post(
                new MapItemToSlotEvent(null, this.armorStandInventory, stack)
        ).slot;
    }
}
