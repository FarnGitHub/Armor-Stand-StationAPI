package farn.armor_stand.util;

import farn.armor_stand.ArmorStandMain;
import farn.armor_stand.world.ArmorStandEntity;
import farn.armor_stand.world.inventory.ArmorStandScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;

import java.util.Arrays;


public class Util {

    public static Vec3d getHitVector(LivingEntity origEnt, Entity targetEnt, double distance, float yOffset) {
        Vec3d look = origEnt.getLookVector();
        Vec3d origin = Vec3d.createCached(origEnt.x, origEnt.y + yOffset, origEnt.z);
        Vec3d ray  = origin.add(look.x * distance, look.y * distance, look.z * distance);
        HitResult result = targetEnt.boundingBox.raycast(origin, ray);
        return result != null ? result.pos : null;
    }

    public static boolean openScreen(PlayerEntity player, ArmorStandEntity stand) {
        GuiHelper.openGUI(
                player,
                ArmorStandMain.id("armor_stand_screen"),
                stand.inventory,
                new ArmorStandScreenHandler(player.inventory, stand.inventory),
                (msg) -> {
                    if(msg.ints == null) msg.ints = new int[1];
                    else msg.ints = Arrays.copyOf(msg.ints, msg.ints.length + 1);
                    msg.ints[msg.ints.length - 1] = stand.id;
                }
        );
        return true;
    }

    public static float wrapAngle(float value) {
        value %= 360.0F;
        if (value >= 180.0F) value -= 360.0F;
        if (value < -180.0F) value += 360.0F;
        return value;
    }

    public static EnvType getEnvType() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    public static boolean canPlaceAt(World view, int x, int y, int z) {
        BlockState state = view.getBlockState(x, y, z);
        return state.isAir() || state.getMaterial().isReplaceable();
    }

}
