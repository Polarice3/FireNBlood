package com.Polarice3.FireNBlood.client.render.layers;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.TaillessWretchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TaillessHeldItemLayer<T extends MobEntity, M extends EntityModel<T> & IHasArm> extends LayerRenderer<T, M> {
        public TaillessHeldItemLayer(IEntityRenderer<T, M> p_i50934_1_) {
            super(p_i50934_1_);
        }

        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            boolean flag = entitylivingbaseIn.getPrimaryHand() == HandSide.RIGHT;
            ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
            ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
            if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
                matrixStackIn.push();
                // y - = ^, + = v  z - = >, + = <
                if (!entitylivingbaseIn.isAggressive()) {
                    matrixStackIn.translate((flag ? -0.125D : 0.125D), 1.5D, -0.2D);
                } else {
                    matrixStackIn.translate((flag ? -0.125D : 0.125D), 1.45D, -0.05D);
                }
                this.func_229135_a_(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
                this.func_229135_a_(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.pop();
            }
        }

        private void func_229135_a_(MobEntity mobEntity, ItemStack itemStack, ItemCameraTransforms.TransformType p_229135_3_, HandSide handSide, MatrixStack matrixStack, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
            if (!itemStack.isEmpty()) {
                matrixStack.push();
                this.getEntityModel().translateHand(handSide, matrixStack);
                matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
                boolean flag = handSide == HandSide.LEFT;
                matrixStack.translate(0, 0, -1.25);
                Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(mobEntity, itemStack, p_229135_3_, flag, matrixStack, p_229135_6_, p_229135_7_);
                matrixStack.pop();
            }
        }
    }
