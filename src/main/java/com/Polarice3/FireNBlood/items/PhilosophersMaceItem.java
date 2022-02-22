package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PhilosophersMaceItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> maceAttributes;

    public PhilosophersMaceItem() {
        super(new Item.Properties().durability(128).tab(FireNBlood.TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.maceAttributes = builder.build();
    }

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() == Items.GOLD_INGOT || super.isValidRepairItem(pToRepair, pRepair);
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        int i1;
        int i2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, stack);
        i1 = MathHelper.clamp(i2, 1, 3);
        if (target.hasEffect(RegistryHandler.GOLDTOUCHED.get())){
            EffectInstance effectInstance = target.getEffect(RegistryHandler.GOLDTOUCHED.get());
            int random = attacker.level.random.nextInt(6 / i1);
            if (random == 0){
                assert effectInstance != null;
                int amp = effectInstance.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                target.removeEffect(RegistryHandler.GOLDTOUCHED.get());
                target.addEffect(new EffectInstance(RegistryHandler.GOLDTOUCHED.get(), 300, i));
            }
        } else {
            target.addEffect(new EffectInstance(RegistryHandler.GOLDTOUCHED.get(), 300));
        }
        return true;
    }

    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.maceAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

}
