package farn.armor_stand.listener;

import farn.armor_stand.event.MapItemToSlotEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;

public class MapItemToSlotListener {

    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void mapItemToSlot(MapItemToSlotEvent event) {
        if(event.held == null) return;

        if (event.held.getItem() instanceof ArmorItem armor) {
            event.slot = switch(armor.equipmentSlot) {
                case 3 -> 1;
                case 2 -> 2;
                case 1 -> 3;
                case 0 -> 4;
                default -> 0;
            };
        }
        if (event.held.getItem() == Block.PUMPKIN.asItem())
            event.slot = 4;
    }
}
