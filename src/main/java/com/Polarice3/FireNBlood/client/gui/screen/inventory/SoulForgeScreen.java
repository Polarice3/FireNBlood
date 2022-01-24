package com.Polarice3.FireNBlood.client.gui.screen.inventory;
/*

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.inventory.container.SoulForgeContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SoulForgeScreen extends ContainerScreen<SoulForgeContainer> {
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(FireNBlood.MOD_ID, "textures/gui/soulforge.png");

    public SoulForgeScreen(SoulForgeContainer p_i51097_1_, PlayerInventory p_i51097_2_, ITextComponent p_i51097_3_) {
        super(p_i51097_1_, p_i51097_2_, p_i51097_3_);
    }

    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled();
            this.blit(matrixStack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled();
        this.blit(matrixStack, i + 79, j + 34, 176, 14, l + 1, 16);
    }
}
*/
