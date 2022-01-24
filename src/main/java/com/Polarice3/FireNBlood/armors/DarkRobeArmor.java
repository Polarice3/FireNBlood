package com.Polarice3.FireNBlood.armors;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.NeophyteEntity;
import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.entities.neutral.AcolyteEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DarkRobeArmor extends ArmorItem {
    private static final String SOULSAMOUNT = "Souls";
    public static final int MAXSOULS = FNBConfig.MaxSouls.get();

    public DarkRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(ModArmorMaterial.DARKMAGE, slot, builderIn);
        ItemModelsProperties.registerProperty(this, new ResourceLocation("souls"), (stack, world, living) -> ((float) currentSouls(stack)) / MAXSOULS);
        ItemModelsProperties.registerProperty(this, new ResourceLocation("activated"), (stack, world, living) -> {
            return isActivated(stack) ? 1.0F : 0.0F;
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULSAMOUNT, 0);
        }
        if (stack.getTag().getInt(SOULSAMOUNT) > MAXSOULS){
            stack.getTag().putInt(SOULSAMOUNT, MAXSOULS);
        }
        if (stack.getTag().getInt(SOULSAMOUNT) < 0){
            stack.getTag().putInt(SOULSAMOUNT, 0);
        }
        if (entityIn instanceof LivingEntity){
            if (stack.getTag().getInt(SOULSAMOUNT) == MAXSOULS){
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RegistryHandler.DEATHPROTECT.get(), 100));
            }
            if (((LivingEntity) entityIn).isPotionActive(RegistryHandler.SOULDRAIN.get())){
                stack.getTag().putInt(SOULSAMOUNT, 0);
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public static boolean isActivated(ItemStack itemStack){
        return itemStack.getTag() != null;
    }

    private static boolean isFull(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        return Soulcount == MAXSOULS;
    }

    private static boolean isEmpty(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        return Soulcount == 0;
    }

    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == RegistryHandler.GOLDTOTEM.get();
    }

    public static int currentSouls(ItemStack itemStack){
        assert itemStack.getTag() != null;
        return itemStack.getTag().getInt(SOULSAMOUNT);
    }

    public static void handleKill(PlayerEntity playerEntity, LivingEntity victim) {
        ItemStack foundStack = ItemStack.EMPTY;

        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        if (!foundStack.isEmpty() && !(victim instanceof PlayerEntity)) {
            if (victim instanceof AbstractRaiderEntity || victim instanceof AbstractProtectorEntity){
                increaseSouls(foundStack, 5);
            } else
            if (victim instanceof VillagerEntity){
                increaseSouls(foundStack, 10);
            } else
            if (victim instanceof NeophyteEntity || victim instanceof AcolyteEntity){
                increaseSouls(foundStack, 8);
            } else
            if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity){
                increaseSouls(foundStack, 2);
            } else
            if (victim instanceof AbstractTaillessEntity){
                increaseSouls(foundStack, 6);
            } else {
                increaseSouls(foundStack, 1);
            }
        }
    }

    public static void increaseSouls(ItemStack itemStack, int souls) {
        if (itemStack.getItem() != RegistryHandler.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        if (!isFull(itemStack)) {
            Soulcount += souls;
            itemStack.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

    public static void decreaseSouls(ItemStack itemStack, int souls) {
        if (itemStack.getItem() != RegistryHandler.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        if (!isEmpty(itemStack)) {
            Soulcount -= souls;
            itemStack.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        if (stack.getTag() != null) {
            int Soulcount = stack.getTag().getInt(SOULSAMOUNT);
            return 1.0D - (Soulcount / (double) MAXSOULS);
        } else {
            return 1.0D;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int Soulcounts = stack.getTag().getInt(SOULSAMOUNT);
            tooltip.add(new TranslationTextComponent("info.firenblood.goldtotem.souls", Soulcounts, MAXSOULS));
        }
    }


}
