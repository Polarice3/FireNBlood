package com.Polarice3.FireNBlood.armors;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.client.model.RobeModel;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.GoldTotemFinder;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class WanderBootsArmor extends ArmorItem {
    private static final UUID BOOTS_UUID = UUID.fromString("f46dd333-63a3-4c3b-a5d3-065de1e226cd");
    private static final AttributeModifier BOOTS_SPEED_MODIFIER = new AttributeModifier(BOOTS_UUID, "Wander Boots Speed bonus", 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final String COOL = "Cool";
    private final Multimap<Attribute, AttributeModifier> bootsModifier;

    public WanderBootsArmor(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ARMOR, new AttributeModifier(BOOTS_UUID, "Armor modifier", (double) this.getDefense(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(BOOTS_UUID, "Armor toughness", (double) this.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(BOOTS_UUID, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        builder.put(Attributes.MOVEMENT_SPEED, BOOTS_SPEED_MODIFIER);
        this.bootsModifier = builder.build();
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
                    stack.setDamageValue(stack.getDamageValue() - FNBConfig.WanderBootsRepairAmount.get());
                }
            }
        }
        player.maxUpStep = 1.0F;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.FEET ? bootsModifier : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        RobeModel model = new RobeModel(1.0F);
        model.RightLeg.visible = false;
        model.LeftLeg.visible = false;
        model.RightFeet.visible = armorSlot == EquipmentSlotType.FEET;
        model.LeftFeet.visible = armorSlot == EquipmentSlotType.FEET;

        model.young = _default.young;
        model.crouching = _default.crouching;
        model.riding = _default.riding;

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getItem() == RegistryHandler.DARKBOOTSOFWANDER.get()){
            return "firenblood:textures/models/armor/darkrobearmor.png";
        } else if (stack.getItem() == RegistryHandler.NECROBOOTSOFWANDER.get()){
            return "firenblood:textures/models/armor/necrorobearmor.png";
        } else {
            return "firenblood:textures/models/armor/darkrobearmor.png";
        }
    }

}
