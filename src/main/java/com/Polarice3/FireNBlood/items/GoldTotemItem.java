package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.enchantments.ModEnchantmentsType;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.NeophyteEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.entities.neutral.AcolyteEntity;
import com.Polarice3.FireNBlood.entities.neutral.MutatedEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GoldTotemItem extends Item {
    public static final String SOULSAMOUNT = "Souls";
    public static final int MAXSOULS = FNBConfig.MaxSouls.get();

    public GoldTotemItem() {
        super(new Item.Properties().tab(FireNBlood.TAB).stacksTo(1).rarity(Rarity.RARE));
        ItemModelsProperties.register(this, new ResourceLocation("souls"),
                (stack, world, living) -> ((float) currentSouls(stack)) / MAXSOULS);
        ItemModelsProperties.register(this, new ResourceLocation("activated"),
                (stack, world, living) -> isActivated(stack) ? 1.0F : 0.0F);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        assert container.getTag() != null;
        if (container.getTag().getInt(SOULSAMOUNT) > FNBConfig.CraftingSouls.get()) {
            GoldTotemItem.decreaseSouls(container, FNBConfig.CraftingSouls.get());
            return container;
        } else {
            return new ItemStack(RegistryHandler.SPENTTOTEM.get());
        }
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
        if (entityIn instanceof PlayerEntity){
            if (stack.getTag().getInt(SOULSAMOUNT) == MAXSOULS){
                ItemStack foundStack = FindTotem((PlayerEntity) entityIn);
                if (!foundStack.isEmpty()) {
                    ((LivingEntity) entityIn).addEffect(new EffectInstance(RegistryHandler.DEATHPROTECT.get(), 20));
                }
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

    public static ItemStack FindTotem(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;

        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        return foundStack;
    }

    public static void handleKill(PlayerEntity playerEntity, LivingEntity victim) {
        ItemStack foundStack = FindTotem(playerEntity);

        if (!foundStack.isEmpty()) {
            if (!(victim instanceof SummonedEntity || victim instanceof FriendlyVexEntity)) {
                if (victim.getMobType() == CreatureAttribute.UNDEAD) {
                    increaseSouls(foundStack, FNBConfig.UndeadSouls.get() * SoulMultiply(playerEntity));
                } else if (victim.getMobType() == CreatureAttribute.ARTHROPOD) {
                    increaseSouls(foundStack, FNBConfig.AnthropodSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof AbstractRaiderEntity || victim instanceof AbstractProtectorEntity) {
                    increaseSouls(foundStack, FNBConfig.IllagerSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof VillagerEntity && !victim.isBaby()) {
                    increaseSouls(foundStack, FNBConfig.VillagerSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof NeophyteEntity || victim instanceof AcolyteEntity || victim instanceof AbstractTaillessEntity) {
                    increaseSouls(foundStack, FNBConfig.TaillessSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity || victim instanceof MutatedEntity) {
                    increaseSouls(foundStack, FNBConfig.PiglinSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof EnderDragonEntity) {
                    increaseSouls(foundStack, FNBConfig.EnderDragonSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof PlayerEntity) {
                    increaseSouls(foundStack, FNBConfig.PlayerSouls.get() * SoulMultiply(playerEntity));
                } else {
                    increaseSouls(foundStack, FNBConfig.DefaultSouls.get() * SoulMultiply(playerEntity));
                }
            }
        }
    }

    public static void EmptySoulTotem(PlayerEntity playerEntity){
        ItemStack foundStack = FindTotem(playerEntity);
        foundStack.setCount(0);
        playerEntity.addItem(new ItemStack(RegistryHandler.SPENTTOTEM.get()));
    }

    public static int SoulMultiply(PlayerEntity playerEntity){
        ItemStack weapon= playerEntity.getMainHandItem();
        int i = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantmentsType.SOULEATER.get(), weapon);
        if (i > 0){
            return i + 1;
        } else {
            return 1;
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

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (playerIn.isCreative()){
            assert itemstack.getTag() != null;
            itemstack.getTag().putInt(SOULSAMOUNT, MAXSOULS);
            return ActionResult.consume(itemstack);
        } else {
            return ActionResult.fail(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int Soulcounts = stack.getTag().getInt(SOULSAMOUNT);
            tooltip.add(new TranslationTextComponent("info.firenblood.goldtotem.souls", Soulcounts, MAXSOULS));
        }
    }

}
