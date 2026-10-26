package farn.armor_stand.client.renderer.model;


public class ArmorStandModel extends ArmorStandArmorModel {

	public ReleaseModelPart standRightSide;
	public ReleaseModelPart standLeftSide;
	public ReleaseModelPart standWaist;
	public ReleaseModelPart standBase;

    public int texWidth = 64;
    public int texHeight = 64;

	public ArmorStandModel() {
		this(0.0F);
	}

	public ArmorStandModel(float size) {
		super(size);
		hat.hidden = true;
		head = new ReleaseModelPart(this, 0, 0);
		head.addCuboid(-1.0F, -7.0F, -1.0F, 2, 7, 2, size);
		head.setPivot(0.0F, 0.0F, 0.0F);
		body = new ReleaseModelPart(this, 0, 26);
		body.addCuboid(-6.0F, 0.0F, -1.5F, 12, 3, 3, size);
		body.setPivot(0.0F, 0.0F, 0.0F);
		rightArm = new ReleaseModelPart(this, 24, 0);
		rightArm.addCuboid(-2.0F, -2.0F, -1.0F, 2, 12, 2, size);
		rightArm.setPivot(-5.0F, 2.0F, 0.0F);
		leftArm = new ReleaseModelPart(this, 32, 16);
		leftArm.mirror = true;
		leftArm.addCuboid(0.0F, -2.0F, -1.0F, 2, 12, 2, size);
		leftArm.setPivot(5.0F, 2.0F, 0.0F);
		rightLeg = new ReleaseModelPart(this, 8, 0);
		rightLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2, 11, 2, size);
		rightLeg.setPivot(-1.9F, 12.0F, 0.0F);
		leftLeg = new ReleaseModelPart(this, 40, 16);
		leftLeg.mirror = true;
		leftLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2, 11, 2, size);
		leftLeg.setPivot(1.9F, 12.0F, 0.0F);
		standRightSide = new ReleaseModelPart(this, 16, 0);
		standRightSide.addCuboid(-3.0F, 3.0F, -1.0F, 2, 7, 2, size);
		standRightSide.setPivot(0.0F, 0.0F, 0.0F);
		standRightSide.visible = true;
		standLeftSide = new ReleaseModelPart(this, 48, 16);
		standLeftSide.addCuboid(1.0F, 3.0F, -1.0F, 2, 7, 2, size);
		standLeftSide.setPivot(0.0F, 0.0F, 0.0F);
		standWaist = new ReleaseModelPart(this, 0, 48);
		standWaist.addCuboid(-4.0F, 10.0F, -1.0F, 8, 2, 2, size);
		standWaist.setPivot(0.0F, 0.0F, 0.0F);
		standBase = new ReleaseModelPart(this, 0, 32);
		standBase.addCuboid(-6.0F, 11.0F, -6.0F, 12, 1, 12, size);
		standBase.setPivot(0.0F, 12.0F, 0.0F);
	}

	@Override
	public void setAngles(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
		super.setAngles(limbAngle, limbDistance, animationProgress, headYaw, headPitch, scale);
		leftLeg.setPivot(1.9F, 12.0F, 0.0F);
		rightLeg.setPivot(-1.9F, 12.0F, 0.0F);
		standRightSide.pitch = 0F;
		standRightSide.yaw = 0F;
		standRightSide.roll = 0F;
		standLeftSide.pitch = 0F;
		standLeftSide.yaw = 0F;
		standLeftSide.roll = 0F;
		standWaist.pitch = 0F;
		standWaist.yaw = 0F;
		standWaist.roll = 0F;
		standBase.pitch = 0.0F;
		standBase.yaw = 0F;
		standBase.roll = 0.0F;
	}

	@Override
	public void render(float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, float scale) {
		super.render(limbAngle, limbDistance, animationProgress, headYaw, headPitch, scale);
		standRightSide.render(scale);
		standLeftSide.render(scale);
		standWaist.render(scale);
		standBase.render(scale);
	}
}