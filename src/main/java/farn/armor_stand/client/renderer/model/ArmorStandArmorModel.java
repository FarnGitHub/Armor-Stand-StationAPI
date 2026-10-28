package farn.armor_stand.client.renderer.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class ArmorStandArmorModel extends BipedEntityModel {
    public ArmorStandArmorModel(float dilation) {
        super(dilation);
        this.rearrangePose();
    }

    protected void rearrangePose() {
        leftLeg.setPivot(1.9F, 11.0F, 0.0F);
        rightLeg.setPivot(-1.9F, 11.0F, 0.0F);
        leftArm.pitch = 0.017453292F * -10.0F;
        leftArm.roll = 0.017453292F * -10.0F;
        rightArm.pitch = 0.017453292F * -15.0F;
        rightArm.roll = 0.017453292F * 10.0F;
        leftLeg.pitch = -0.017453292F;
        leftLeg.roll = -0.017453292F;
        rightLeg.pitch = 0.017453292F;
        rightLeg.roll = 0.017453292F;
        copyModelAngles(head, hat);
    }

    @Override
    public void setAngles(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
        //Treat the model as non-living entity
    }

    public static void copyModelAngles(ModelPart source, ModelPart dest) {
        dest.pitch = source.pitch;
        dest.yaw = source.yaw;
        dest.roll = source.roll;
        dest.pivotX = source.pivotX;
        dest.pivotY = source.pivotY;
        dest.pivotZ = source.pivotZ;
    }
}
