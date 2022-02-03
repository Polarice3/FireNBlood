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
		texWidth = 64;
		texHeight = 64;

		bigrune = new ModelRenderer(this);
		bigrune.setPos(0.0F, 24.0F, 0.0F);
		

		base = new ModelRenderer(this);
		base.setPos(0.0F, 0.0F, 0.0F);
		bigrune.addChild(base);
		base.texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 0.0F, 14.0F, 0.0F, false);

		sides = new ModelRenderer(this);
		sides.setPos(0.0F, 0.0F, 0.0F);
		bigrune.addChild(sides);
		sides.texOffs(28, 14).addBox(-7.0F, -5.0F, 7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.texOffs(0, 24).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.texOffs(0, 5).addBox(7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
		sides.texOffs(0, 0).addBox(-7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

	}
}