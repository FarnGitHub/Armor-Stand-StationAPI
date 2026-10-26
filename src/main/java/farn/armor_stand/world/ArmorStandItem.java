package farn.armor_stand.world;

import farn.armor_stand.ArmorStandMain;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.List;

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
		int block = world.getBlockId(clickX, clickY, clickZ);
		if (block > 0 && world.canPlace(block, clickX, clickY, clickZ, true, side)) {
			side = 0;
			x = clickX;
			y = clickY;
			z = clickZ;
		} else {
			Direction direction = Direction.values()[side];
			x = clickX + direction.getOffsetX();
			y = clickY + direction.getOffsetY();
			z = clickZ + direction.getOffsetZ();
		}
		ItemPlacementContext context = new ItemPlacementContext(player, stack, new HitResult(clickX, clickY, clickZ, side, Vec3d.createCached(x, y, z)));
		ItemPlacementContext contextAbove = ItemPlacementContext.offset(context, context.getBlockPos(), Direction.UP);
		boolean cantPlace = !world.isAir(x, y, z) && !world.getBlockState(x,y,z).canReplace(context);
		cantPlace |= !world.isAir(x, y + 1, z) && !world.getBlockState(x, y + 1, z).canReplace(contextAbove);

		if (cantPlace) return false;

		List<Entity> list = world.getEntities(null, Box.create(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));

		if (list.isEmpty()) {
			if(!world.isRemote) {
				world.setBlock(x, y, z, 0);
				world.setBlock(x, y + 1, z, 0);
				ArmorStandEntity stand = new ArmorStandEntity(world, x + 0.5D, y, z + 0.5D);
				float f3 = MathHelper.floor((wrapAngleTo180_float(player.yaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
				stand.setPositionAndAnglesKeepPrevAngles(x + 0.5D, y, z + 0.5D, f3, 0.0F);
				world.spawnEntity(stand);
			}
			--stack.count;
		}
		return list.isEmpty();
	}

	public static float wrapAngleTo180_float(float value)
	{
		value %= 360.0F;

		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

}