package farn.armor_stand.world;

import farn.armor_stand.ArmorStandMain;
import farn.armor_stand.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.List;
import java.util.Optional;

public class ArmorStandItem extends TemplateItem {

	public ArmorStandItem() {
		super(ArmorStandMain.id("armor_stand"));
		setMaxCount(16);
	}

	@Override
	public boolean useOnBlock(ItemStack stack, PlayerEntity player, World world, int clickX, int clickY, int clickZ, int side) {
		final int x;
		final int y;
		final int z;
		if (Util.canPlaceAt(world, clickX, clickY, clickZ)) {
			x = clickX;
			y = clickY;
			z = clickZ;
		} else {
			Direction direction = Direction.values()[side];
			x = clickX + direction.getOffsetX();
			y = clickY + direction.getOffsetY();
			z = clickZ + direction.getOffsetZ();
		}

		if(!(Util.canPlaceAt(world, x, y, z) && Util.canPlaceAt(world, x, y + 1, z))) {
			return false;
		}

		@SuppressWarnings("unchecked")
		List<Entity> list = world.getEntities(null, Box.create(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));

		if (list.isEmpty()) {
			if(!world.isRemote) {
				world.setBlock(x, y, z, 0);
				world.setBlock(x, y + 1, z, 0);
				ArmorStandEntity stand = new ArmorStandEntity(world, x + 0.5D, y, z + 0.5D);
				float angle = MathHelper.floor((Util.wrapAngle(player.yaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
				stand.setPositionAndAnglesKeepPrevAngles(x + 0.5D, y, z + 0.5D, angle, 0.0F);
				world.spawnEntity(stand);
			}
			world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, Block.STONE_SOUND_GROUP.getSound(), 1.0F, 1.0F);
			--stack.count;
			return true;
		}
		return false;
	}
}