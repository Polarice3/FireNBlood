package com.Polarice3.FireNBlood.client.model;


import com.Polarice3.FireNBlood.entities.neutral.MutatedSheepEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class MutatedSheepWoolModel <T extends MutatedSheepEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer rightleg;
    private final ModelRenderer leftleg;

    public MutatedSheepWoolModel() {
        texWidth = 128;
        texHeight = 128;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 5.0F, 0.0F);
        setRotationAngle(body, 0.0F, 0.0F, 0.7854F);
        body.texOffs(0, 0).addBox(-8.0F, -8.0F, -9.0F, 16.0F, 16.0F, 18.0F, 1.0F, false);

        rightleg = new ModelRenderer(this);
        rightleg.setPos(-3.0F, 12.0F, 7.0F);
        rightleg.texOffs(0, 46).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        leftleg = new ModelRenderer(this);
        leftleg.setPos(3.0F, 12.0F, 7.0F);
        leftleg.texOffs(32, 34).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.body.xRot = ((float)Math.PI / 2F);
        this.rightleg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftleg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        rightleg.render(matrixStack, buffer, packedLight, packedOverlay);
        leftleg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
