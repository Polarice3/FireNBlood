package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.enchantments.ModEnchantmentsType;
import com.Polarice3.FireNBlood.utils.GoldTotemFinder;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PhilosophersMaceItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> maceAttributes;
    private int tickCount;

    public PhilosophersMaceItem() {
        super(new Item.Properties().durability(128).tab(FireNBlood.TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.maceAttributes = builder.build();
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            if (stack.isDamaged()) {
                ++ this.tickCount;
                ItemStack foundStack = GoldTotemFinder.FindTotem(player);
                if (!foundStack.isEmpty()){
                    if (this.tickCount % 20 == 0) {
                        this.tickCount = 0;
                        stack.setDamageValue(stack.getDamageValue() - 1);
                        GoldTotemItem.decreaseSouls(foundStack, 5);
                    }
                }

            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
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

    public boolean mineBlock(ItemStack pStack, World pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(1, pEntityLiving, (p_220038_0_) -> {
                p_220038_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        Material material = pBlock.getMaterial();
        return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return 8.0F;
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
        return enchantment.category == EnchantmentType.DIGGER || enchantment.category == EnchantmentType.WEAPON || enchantment.category == EnchantmentType.BREAKABLE;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.maceAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

}
