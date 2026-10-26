package farn.armor_stand.client.renderer;

import farn.armor_stand.client.renderer.model.ArmorStandArmorModel;
import farn.armor_stand.client.renderer.model.ArmorStandModel;
import farn.armor_stand.world.ArmorStandEntity;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.item.BlockItemForm;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

public class ArmorStandEntityRenderer extends LivingEntityRenderer {
	private final ArmorStandModel baseModel;
	private final ArmorStandArmorModel armor1;
	private final ArmorStandArmorModel armor2;

	@Unique
	private static final Map<Identifier, String[]> STATIONAPI$ARMOR_CACHE = new Reference2ObjectOpenHashMap<>();

	public ArmorStandEntityRenderer() {
		super(new ArmorStandModel(), 0.0F);
		baseModel = (ArmorStandModel) model;
		armor1 = new ArmorStandArmorModel(1.0F);
		armor2 = new ArmorStandArmorModel(0.5F);
	}

	public void render(ArmorStandEntity armorStand, double d, double e, double f, float g, float h) {
		ItemStack heldStack = armorStand.inventory.getStack(0);
		this.armor1.rightArmPose = this.armor2.rightArmPose = this.baseModel.rightArmPose = heldStack != null;
		super.render(armorStand, d, e, f, g, h);
	}

	protected boolean bindTexture(ArmorStandEntity armorStand, int layer) {
		ItemStack stack = armorStand.inventory.getStack(4 - layer);
		if (stack != null) {
			if (stack.getItem() instanceof ArmorItem armor) {
				String path = "";
				if (armor instanceof ArmorTextureProvider provider) {
					Identifier id = provider.getTexture(armor);
					String[] textures = STATIONAPI$ARMOR_CACHE.computeIfAbsent(id, k -> new String[4]);
					if (textures[layer] == null)
						textures[layer] = stationapi_getTexturePath(id, layer);
				} else
					path = "/armor/" + PlayerEntityRenderer.armorTextureNames[armor.textureIndex] + "_" + (layer == 2 ? 2 : 1) + ".png";
				this.bindTexture(path);
				BipedEntityModel model = layer == 2 ? this.armor2 : this.armor1;
				model.head.visible = layer == 0;
				model.hat.visible = layer == 0;
				model.body.visible = layer == 1 || layer == 2;
				model.rightArm.visible = layer == 1;
				model.leftArm.visible = layer == 1;
				model.rightLeg.visible = layer == 2 || layer == 3;
				model.leftLeg.visible = layer == 2 || layer == 3;
				this.setDecorationModel(model);
				return true;
			}
		}

		return false;
	}

	protected void renderMore(ArmorStandEntity armorStand) {
		ItemStack headStack = armorStand.inventory.getStack(4);
		if (headStack != null && headStack.getItem() instanceof BlockItemForm) {
			GL11.glPushMatrix();
			this.baseModel.head.transform(0.0625F);
			if (BlockRenderManager.isSideLit(Block.BLOCKS[headStack.itemId].getRenderType())) {
				float var4 = 0.625F;
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			}

			this.dispatcher.heldItemRenderer.renderItem(armorStand, headStack);
			GL11.glPopMatrix();
		}

		ItemStack heldStack = armorStand.inventory.getStack(0);
		if (heldStack != null) {
			GL11.glPushMatrix();
			this.baseModel.rightArm.transform(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if(isJsonModel(heldStack)) GL11.glPushMatrix();
			if (heldStack.getItem() instanceof BlockItemForm blockItem && BlockRenderManager.isSideLit(blockItem.getBlock().getRenderType())) {
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(0.375F, -0.375F, 0.375F);
			} else if (Item.ITEMS[heldStack.itemId].isHandheld()) {
				if (Item.ITEMS[heldStack.itemId].isHandheldRod()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(0.625F, -0.625F, 0.625F);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(0.375F, 0.375F, 0.375F);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			if(isJsonModel(heldStack)) GL11.glPopMatrix();
			this.dispatcher.heldItemRenderer.renderItem(armorStand, heldStack);
			GL11.glPopMatrix();
		}

	}

	@Override
	protected void applyHandSwingRotation(LivingEntity entity, float x, float y, float z) {
		GL11.glRotatef(180.0F - y, 0.0F, 1.0F, 0.0F);
	}

	private String stationapi_getTexturePath(Identifier identifier, int armorIndex) {
		return "/assets/" + identifier.namespace + "/stationapi/textures/armor/" + identifier.path + (armorIndex == 2 ? "_2.png" : "_1.png");
	}

	private boolean isJsonModel(ItemStack stack) {
		return RendererAccess.INSTANCE.hasRenderer() &&
				!(RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().
						getItemModels().getModel(stack) instanceof VanillaBakedModel);
	}

	protected boolean bindTexture(LivingEntity mob, int layer, float tickDelta) {
		return this.bindTexture((ArmorStandEntity) mob, layer);
	}

	protected void renderMore(LivingEntity entity, float tickDelta) {
		this.renderMore((ArmorStandEntity)entity);
	}

	public void render(LivingEntity livingEntity, double d, double e, double f, float g, float h) {
		this.render((ArmorStandEntity)livingEntity, d, e, f, g, h);
	}
}