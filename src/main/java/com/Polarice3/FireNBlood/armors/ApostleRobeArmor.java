package com.Polarice3.FireNBlood.armors;

import com.Polarice3.FireNBlood.client.model.ApostleRobeModel;
import com.Polarice3.FireNBlood.client.model.RobeModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ApostleRobeArmor extends ArmorItem {

    public ApostleRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        ApostleRobeModel model = new ApostleRobeModel(1.0F);
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
        return "firenblood:textures/models/armor/apostlerobearmor.png";
    }
}
