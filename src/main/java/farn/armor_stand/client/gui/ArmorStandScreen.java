package farn.armor_stand.client.gui;

import farn.armor_stand.world.ArmorStandEntity;
import farn.armor_stand.world.inventory.ArmorStandInventory;
import farn.armor_stand.world.inventory.ArmorStandScreenHandler;
import farn.armor_stand.world.inventory.ArmorStandSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ArmorStandScreen extends HandledScreen {
	public final ArmorStandInventory armorStandInventory;
	public final Inventory inventory;
	public final ArmorStandScreenHandler armorHandler;

	public ArmorStandScreen(Inventory inv, ArmorStandInventory armorStandInventory) {
		super(new ArmorStandScreenHandler(inv, armorStandInventory));
		this.armorHandler = (ArmorStandScreenHandler)handler;
		this.inventory = inv;
		this.armorStandInventory = armorStandInventory;
		this.backgroundHeight = 174;
	}

	protected void drawBackground(float tick) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.textureManager.bindTexture(
				this.minecraft.textureManager.getTextureId(
						"/assets/armor_stand/textures/gui/armor_stand_inventory.png"));
		int xStart = this.width - this.backgroundWidth >> 1;
		int yStart = this.height - this.backgroundHeight >> 1;
		this.drawTexture(xStart, yStart, 0, 0,this.backgroundWidth, this.backgroundHeight);
		drawArmorStandEntity(xStart, yStart);
	}

	protected void drawForeground() {
		this.textRenderer.draw(this.armorStandInventory.getName(), 6, 6, 4210752);
	}

	protected void drawArmorStandEntity(int xStart, int yStart) {
		if(this.armorStandInventory.entity == null) return;
		ArmorStandEntity armorStandEntity = this.armorStandInventory.entity;
		GL11.glEnable(32826);
		GL11.glEnable(2903);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(xStart + (backgroundWidth / 2)), (float)(yStart + 85), 50.0F);
		GL11.glScalef(-30.0F, 30.0F, 30.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		Lighting.turnOn();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		float ogYaw = armorStandEntity.bodyYaw;
		float ogLastYaw = armorStandEntity.lastBodyYaw;
		armorStandEntity.bodyYaw = 0.0F;
		armorStandEntity.lastBodyYaw = 0.0F;
		armorStandEntity.minBrightness = 1.0F;
		GL11.glTranslatef(0.0F, armorStandEntity.standingEyeHeight, 0.0F);
		EntityRenderDispatcher.INSTANCE.render(armorStandEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
		armorStandEntity.minBrightness = 0.0F;
		armorStandEntity.bodyYaw = ogYaw;
		armorStandEntity.lastBodyYaw = ogLastYaw;
		GL11.glPopMatrix();
		Lighting.turnOff();
		GL11.glDisable(32826);
	}

}
