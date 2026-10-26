package farn.armor_stand.world.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ArmorStandScreenHandler extends ScreenHandler {
    public ArmorStandInventory armorStandInventory;
    public final ArmorStandSlot[] armorStandSlots;

    public ArmorStandScreenHandler(Inventory inv, ArmorStandInventory armorStand) {
        this.armorStandInventory = armorStand;
        this.armorStandSlots = new ArmorStandSlot[armorStandInventory.size()];
        //armor slot
        for(int index = 0; index < 4; ++index) {
            int slot = 4 - index;
            this.addSlot(armorStandSlots[slot] = new ArmorStandSlot(armorStand, slot, 44, 16 + index * 18));
        }

        //item Slot
        this.addSlot(armorStandSlots[0] = new ArmorStandSlot(armorStand, 0, 116, 70));

        //player backpack
        for(int x = 0; x < 3; ++x) {
            for(int y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inv, y + (x + 1) * 9, 8 + y * 18, 92 + x * 18));
            }
        }

        //hotbar
        for(int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(inv, hotbar, 8 + hotbar * 18, 150));
        }
    }

    public boolean canUse(PlayerEntity plr) {
        return this.armorStandInventory.canPlayerUse(plr);
    }

    public ItemStack quickMove(int slotId) {
        ItemStack newStack = null;
        Slot slot = (Slot)this.slots.get(slotId);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();
            if(slotId >= 5 && slotId <= 41) {
                int armorSlotIndex = findSuitableSlot(stack);
                if (armorSlotIndex >= 0)
                    this.insertItem(stack, armorSlotIndex, armorSlotIndex + 1, false);
                else if (slotId <= 31)
                    this.insertItem(stack, 32, 41, false);
                else
                    this.insertItem(stack, 5, 31, false);
            } else
                this.insertItem(stack, 5, 41, false);

            if (stack.count <= 0)
                slot.setStack(null);
            else
                slot.markDirty();

            if (stack.count == newStack.count) return null;

            slot.onTakeItem(stack);
        }

        return newStack;
    }

    public int findSuitableSlot(ItemStack stack) {
        for(ArmorStandSlot slot : this.armorStandSlots) {
            if(!slot.hasStack() && slot.canInsert(stack))
                return slot.id;
        }
        return -1;
    }
}
