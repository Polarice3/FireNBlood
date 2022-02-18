package com.Polarice3.FireNBlood.client.gui.overlay;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.GoldTotemFinder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class SoulEnergyGui extends AbstractGui {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/gui/soulenergy.png");
    private final Minecraft minecraft;
    private final PlayerEntity player;
    protected int screenWidth;
    protected int screenHeight;

    public SoulEnergyGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        ItemStack stack = GoldTotemFinder.FindTotem(player);
        return !stack.isEmpty();
    }

    public FontRenderer getFont() {
        return this.minecraft.font;
    }

    public void drawHUD(MatrixStack ms, float pt) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if(!shouldDisplayBar()) {
            return;
        }

        ItemStack stack = GoldTotemFinder.FindTotem(player);
        int SoulEnergy = 0;
        if (!stack.isEmpty()) {
            if (stack.getTag() != null) {
                SoulEnergy = Objects.requireNonNull(stack.getTag()).getInt(GoldTotemItem.SOULSAMOUNT);
            }
        }
        int SoulEnergyTotal = FNBConfig.MaxSouls.get();

        int i = (this.screenWidth/2) + 100;
        int energylength = 117;
        energylength = (int)((energylength) * (SoulEnergy / (double)SoulEnergyTotal));

        int height = this.screenHeight - 5;

        Minecraft.getInstance().textureManager.bind(new ResourceLocation(FireNBlood.MOD_ID, "textures/gui/soulenergyborder.png"));
        blit(ms,i, height - 9, 0, 0, 128,9, 128, 9);
        Minecraft.getInstance().textureManager.bind(new ResourceLocation(FireNBlood.MOD_ID, "textures/gui/soulenergy.png"));
        blit(ms,i + 9, height - 7, 0, 0, energylength,5, 117, 5);
        if (FNBConfig.ShowNum.get()) {
            this.minecraft.getProfiler().push("soulenergy");
            String s = "" + SoulEnergy + "/" + "" + SoulEnergyTotal;
            int i1 = i + 36;
            int j1 = height - 8;
            this.getFont().draw(ms, s, (float) i1, (float) j1, 16777215);
            this.minecraft.getProfiler().pop();
        }
    }
}
