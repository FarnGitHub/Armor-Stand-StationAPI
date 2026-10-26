package farn.armor_stand.client;

import farn.armor_stand.ArmorStandMain;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.listeners.VanillaTabListener;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class BHCreativeListener {

    @EventListener
    public void addToCreativeTab(TabRegistryEvent event) {
        VanillaTabListener.tabOtherBlocks.addItem(new ItemStack(ArmorStandMain.ARMOR_STAND_ITEM));
    }
}
