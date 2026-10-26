package farn.armor_stand.client.renderer.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ArmorStandArmorModel extends BipedEntityModel {
    public ArmorStandArmorModel(float dilation) {
        super(dilation, 0.0F);
    }

    @Override
    public void setAngles(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
        head.pitch = 0F;
        head.yaw = 0F;
        head.roll = 0F;
        body.pitch = 0F;
        body.yaw = 0F;
        body.roll = 0F;
        leftArm.pitch = 0.017453292F * -10.0F;
        leftArm.yaw = 0F;
        leftArm.roll = 0.017453292F * -10.0F;
        rightArm.pitch = 0.017453292F * -15.0F;
        rightArm.yaw = 0F;
        rightArm.roll = 0.017453292F * 10.0F;
        leftLeg.pitch = -0.017453292F;
        leftLeg.yaw = 0F;
        leftLeg.roll = -0.017453292F;
        leftLeg.setPivot(1.9F, 11.0F, 0.0F);
        rightLeg.pitch = 0.017453292F;
        rightLeg.yaw = 0F;
        rightLeg.roll = 0.017453292F;
        rightLeg.setPivot(-1.9F, 11.0F, 0.0F);
        copyModelAngles(head, hat);
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
