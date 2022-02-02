package com.Polarice3.FireNBlood.armors;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.client.model.RobeModel;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NecroRobeArmor extends ArmorItem {
    private static final String COOL = "Cool";

    public NecroRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (FNBConfig.SoulRepair.get()) {
            if (stack.getTag() == null) {
                CompoundNBT compound = stack.getOrCreateTag();
                compound.putInt(COOL, 0);
            }
            ItemStack foundStack = ItemStack.EMPTY;
            for (int i = 0; i <= 9; i++) {
                ItemStack itemStack = player.inventory.getStackInSlot(i);
                if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                    foundStack = itemStack;
                    break;
                }
            }
            if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > 0 && stack.isDamaged()) {
                stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                if (stack.getTag().getInt(COOL) > 60) {
                    stack.getTag().putInt(COOL, 0);
                    GoldTotemItem.decreaseSouls(foundStack, 1);
                    stack.setDamage(stack.getDamage() - 1);
                }
            }
        }
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        RobeModel model = new RobeModel(1.0F);
        model.Headwear.showModel = armorSlot == EquipmentSlotType.HEAD;
        model.Body.showModel = armorSlot == EquipmentSlotType.CHEST;
        model.RightArm.showModel = armorSlot == EquipmentSlotType.CHEST;
        model.LeftArm.showModel = armorSlot == EquipmentSlotType.CHEST;

        model.isChild = _default.isChild;
        model.isSneak = _default.isSneak;
        model.isSitting = _default.isSitting;
        model.rightArmPose = _default.rightArmPose;
        model.leftArmPose = _default.leftArmPose;

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "firenblood:textures/models/armor/necrorobearmor.png";
    }
}
