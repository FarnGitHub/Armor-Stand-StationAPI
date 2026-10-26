package farn.armor_stand.client;

import farn.armor_stand.ArmorStandMain;
import farn.armor_stand.client.renderer.ArmorStandEntityRenderer;
import farn.armor_stand.world.ArmorStandEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;

public class ArmorStandClient {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerEntityModel(EntityRendererRegisterEvent event) {
        event.register(ArmorStandEntity.class, new ArmorStandEntityRenderer());
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerItemTexture(TextureRegisterEvent event) {
        ArmorStandMain.ARMOR_STAND_ITEM.setTexture(ArmorStandMain.id("item/armor_stand"));
    }
}
