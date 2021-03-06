package com.Polarice3.FireNBlood.armors;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.client.model.RobeModel;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.GoldTotemFinder;
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

public class NecroArmoredRobeArmor extends ArmorItem {
    private static final String COOL = "Cool";

    public NecroArmoredRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
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
            ItemStack foundStack = GoldTotemFinder.FindTotem(player);
            if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > 0 && stack.isDamaged()) {
                stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                if (stack.getTag().getInt(COOL) > 20) {
                    stack.getTag().putInt(COOL, 0);
                    GoldTotemItem.decreaseSouls(foundStack, 1);
                    stack.setDamageValue(stack.getDamageValue() - FNBConfig.NecroArmoredRobeRepairAmount.get());
                }
            }
        }
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        RobeModel model = new RobeModel(1.0F);
        model.hat.visible = armorSlot == EquipmentSlotType.HEAD;
        model.Body.visible = armorSlot == EquipmentSlotType.CHEST;
        model.RightArm.visible = armorSlot == EquipmentSlotType.CHEST;
        model.LeftArm.visible = armorSlot == EquipmentSlotType.CHEST;
        model.Pants.visible = armorSlot == EquipmentSlotType.LEGS;
        model.RightLeg.visible = armorSlot == EquipmentSlotType.LEGS;
        model.LeftLeg.visible = armorSlot == EquipmentSlotType.LEGS;
        model.RightFeet.visible = false;
        model.LeftFeet.visible = false;

        model.young = _default.young;
        model.crouching = _default.crouching;
        model.riding = _default.riding;
        model.rightArmPose = _default.rightArmPose;
        model.leftArmPose = _default.leftArmPose;

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "firenblood:textures/models/armor/necroarmoredrobearmor.png";
    }
}
