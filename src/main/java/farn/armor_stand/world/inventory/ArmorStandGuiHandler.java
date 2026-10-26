package farn.armor_stand.world.inventory;

import farn.armor_stand.client.gui.ArmorStandScreen;
import farn.armor_stand.world.ArmorStandEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.util.SideUtil;

public class ArmorStandGuiHandler {

    public static GuiHandler INSTANCE = new GuiHandler(
            (player, inventory, packet) -> {
                ArmorStandInventory armorStandInventory = (ArmorStandInventory) inventory;
                if(player.world.isRemote)
                    SideUtil.runClient(() -> setEntityFromId(armorStandInventory, packet.ints[packet.ints.length - 1]));
                return new ArmorStandScreen(player.inventory,armorStandInventory);
            },
            ArmorStandInventory::new
    );

    @Environment(EnvType.CLIENT)
    private static void setEntityFromId(ArmorStandInventory inv, int id) {
        Entity entity  = ((ClientWorld) PlayerHelper.getPlayerFromGame().world).getEntity(id);
        if(entity instanceof ArmorStandEntity armorStand) inv.entity = armorStand;
    }
}
