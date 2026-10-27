package farn.armor_stand.world;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class VecUtils {

    public static double getHitDiff(PlayerEntity player, LivingEntity target) {
        Vec3d origin = Vec3d.createCached(player.x, player.y + player.getEyeHeight(), player.z);
        Vec3d end = Vec3d.createCached(target.x, target.boundingBox.minY, target.z);
        double distance = origin.distanceTo(end);
        Vec3d lookV = player.getLookVector();
        Vec3d hitVec  = origin.add(lookV.x * distance, lookV.y * distance, lookV.z * distance);
        HitResult result = target.boundingBox.raycast(origin, hitVec);
        return result != null ? result.pos.y - end.y : 0;
    }
}
