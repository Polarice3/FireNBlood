package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class SoulUsingItem extends Item {
    private static final String SOULUSE = "Soul Use";
    private static final String CASTTIME = "Cast Time";

    public SoulUsingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        LivingEntity livingEntity = (LivingEntity) entityIn;
        if (stack.getTag() == null){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULUSE, SoulUse(livingEntity));
            compound.putInt(CASTTIME, CastTime(livingEntity));
        }
        stack.getTag().putInt(SOULUSE, SoulUse(livingEntity));
        stack.getTag().putInt(CASTTIME, CastTime(livingEntity));
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public abstract int SoulCost();

    public abstract int CastDuration();

    public abstract SoundEvent CastingSound();

    public boolean SoulDiscount(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.DARKROBE.get()
                || entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROROBE.get();
    }

    public boolean SoulCostUp(LivingEntity entityLiving){
        return entityLiving.isPotionActive(RegistryHandler.SUMMONDOWN.get());
    }

    public boolean ReduceCastTime(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.DARKHELM.get()
                || entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROHELM.get();
    }

    public int SoulUse(LivingEntity entityLiving){
        if (SoulCostUp(entityLiving)){
            int amp = Objects.requireNonNull(entityLiving.getActivePotionEffect(RegistryHandler.SUMMONDOWN.get())).getAmplifier() + 2;
            return SoulCost() * amp;
        } else if (SoulDiscount(entityLiving)){
            return SoulCost()/2;
        } else {
            return SoulCost();
        }
    }

    public int CastTime(LivingEntity entityLiving){
        if (ReduceCastTime(entityLiving)){
            return CastDuration()/2;
        } else {
            return CastDuration();
        }
    }

    public abstract ItemStack MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving);

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        ItemStack foundStack = ItemStack.EMPTY;
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                foundStack = itemStack;
                break;
            }
        }

        if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) >= SoulUse(entityLiving)) {
            GoldTotemItem.decreaseSouls(foundStack, SoulUse(entityLiving));
            return MagicResults(stack, worldIn, entityLiving);
        } else {
            worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
                double d = worldIn.rand.nextGaussian() * 0.2D;
                entityLiving.world.addParticle(ParticleTypes.CLOUD, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), d, d, d);
            }
            return stack;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        for(int i = 0; i < playerIn.world.rand.nextInt(35) + 10; ++i) {
            double d = worldIn.rand.nextGaussian() * 0.2D;
            playerIn.world.addParticle(ParticleTypes.ENTITY_EFFECT, playerIn.getPosX(), playerIn.getPosYEye(), playerIn.getPosZ(), d, d, d);
        }        return ActionResult.resultConsume(itemstack);
    }

    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isRemote) {
            SoundEvent soundevent = this.CastingSound();
            int CastTime = stack.getUseDuration() - count;
            if (CastTime == 1) {
                if (soundevent != null) {
                    worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
                } else {
                    worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(CASTTIME);
        } else {
            return this.CastDuration();
        }
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int SoulUse = stack.getTag().getInt(SOULUSE);
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.cost", SoulUse));
        } else {
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.cost", SoulCost()));
        }
    }

}
