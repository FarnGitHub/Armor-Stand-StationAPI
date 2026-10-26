package farn.armor_stand.event;

import farn.armor_stand.world.inventory.ArmorStandInventory;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class MapItemToSlotEvent extends Event {
    public final ItemStack held;
    public final ArmorStandInventory armorStand;
    public @Nullable final PlayerEntity player;
    public byte slot = 0;

    public MapItemToSlotEvent(@Nullable PlayerEntity player, ArmorStandInventory armorStand, ItemStack item) {
        this.held = item;
        this.armorStand = armorStand;
        this.player = player;
    }

}
