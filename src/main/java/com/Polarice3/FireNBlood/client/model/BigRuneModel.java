package com.Polarice3.FireNBlood.client.model;// Made with Blockbench 4.0.4
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Polarice3.FireNBlood.entities.utilities.LightningTrapEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BigRuneModel<T extends LightningTrapEntity> extends EntityModel<T> {
	private final ModelRenderer bigrune;
	private final ModelRenderer base;
	private final ModelRenderer sides;

	public BigRuneModel() {
		textureWidth = 64;
		textureHeight = 64;

		bigrune = new ModelRenderer(this);
		bigrune.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 0.0F, 0.0F);
		bigrune.addChild(base);
		base.setTextureOffset(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 0.0F, 14.0F, 0.0F, false);

		sides = new ModelRenderer(this);
		sides.setRotationPoint(0.0F, 0.0F, 0.0F);
		bigrune.addChild(sides);
		sides.setTextureOffset(28, 14).addBox(-7.0F, -5.0F, 7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.setTextureOffset(0, 24).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.setTextureOffset(0, 5).addBox(7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
		sides.setTextureOffset(0, 0).addBox(-7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

	}
}