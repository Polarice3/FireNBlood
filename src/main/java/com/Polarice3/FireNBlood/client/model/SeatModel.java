package com.Polarice3.FireNBlood.client.model;// Made with Blockbench 4.1.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SeatModel extends EntityModel<FakeSeatEntity> {
	private final ModelRenderer Seat;

	public SeatModel() {
		textureWidth = 16;
		textureHeight = 16;

		Seat = new ModelRenderer(this);
		Seat.setRotationPoint(0.0F, 24.0F, 0.0F);
		Seat.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(FakeSeatEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}


	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Seat.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}