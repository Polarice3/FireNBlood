package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.inventory.container.SoulItemContainer;
import com.Polarice3.FireNBlood.items.capability.SoulUsingItemCapability;
import com.Polarice3.FireNBlood.spells.*;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.Polarice3.FireNBlood.items.handler.SoulUsingItemHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.Polarice3.FireNBlood.items.MagicFocusItem.FOCUS;

public class SoulWand extends Item{
    private static final String SOULUSE = "Soul Use";
    private static final String CASTTIME = "Cast Time";
    private static final String CURRENTFOCUS = "Focus";
    private static final String SOULCOST = "Soul Cost";
    private static final String DURATION = "Duration";
    private static final String COOLDOWN = "Cooldown";
    private static final String CASTSOUND = "Cast Sound";
    private static final String SPELL = "Spell";
    private static final String COOL = "Cool";
    private static final String FOCUSBAG = "Focus Bag";

    public SoulWand() {
        super(new Properties().tab(FireNBlood.TAB).stacksTo(1).setNoRepair().rarity(Rarity.RARE));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;
            if (stack.getTag() == null) {
                CompoundNBT compound = stack.getOrCreateTag();
                compound.putInt(SOULUSE, SoulUse(livingEntity, stack));
                compound.putInt(SOULCOST, 0);
                compound.putInt(CASTTIME, CastTime(livingEntity, stack));
                compound.putInt(COOL, 0);
            }
            if (getFocus(stack) != ItemStack.EMPTY && getFocus(stack).getTag() != null) {
                stack.getTag().putString(CURRENTFOCUS, FOCUS);
                this.ChangeFocus(stack);
                stack.getTag().putString(CURRENTFOCUS, FOCUS);
            } else {
                stack.getTag().putString(CURRENTFOCUS, "none");
                this.setSpellConditions(null, stack);
            }
            stack.getTag().putInt(SOULUSE, SoulUse(livingEntity, stack));
            stack.getTag().putInt(CASTTIME, CastTime(livingEntity, stack));
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == RegistryHandler.FOCUSBAG.get();
    }

    public boolean SoulDiscount(LivingEntity entityLiving){
        return entityLiving.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.DARKROBE.get()
                || entityLiving.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.NECROROBE.get();
    }

    public boolean SoulCostUp(LivingEntity entityLiving){
        return entityLiving.hasEffect(RegistryHandler.SUMMONDOWN.get());
    }

    public boolean ReduceCastTime(LivingEntity entityLiving){
        return entityLiving.getItemBySlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.DARKHELM.get()
                || entityLiving.getItemBySlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROHELM.get();
    }

    public int SoulUse(LivingEntity entityLiving, ItemStack stack){
        if (SoulCostUp(entityLiving)){
            int amp = Objects.requireNonNull(entityLiving.getEffect(RegistryHandler.SUMMONDOWN.get())).getAmplifier() + 2;
            return SoulCost(stack) * amp;
        } else if (SoulDiscount(entityLiving)){
            return SoulCost(stack)/2;
        } else {
            return SoulCost(stack);
        }
    }

    public int CastTime(LivingEntity entityLiving, ItemStack stack){
        if (ReduceCastTime(entityLiving)){
            return CastDuration(stack)/2;
        } else {
            return CastDuration(stack);
        }
    }

    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isClientSide) {
            SoundEvent soundevent = this.CastingSound(stack);
            int CastTime = stack.getUseDuration() - count;
            if (CastTime == 1) {
                if (soundevent != null) {
                    worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
                } else {
                    worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
            }
            if (this.getSpell(stack) instanceof ChargingSpells){
                assert stack.getTag() != null;
                stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                if (stack.getTag().getInt(COOL) > Cooldown(stack)){
                    stack.getTag().putInt(COOL, 0);
                    this.MagicResults(stack, worldIn, livingEntityIn);
                }
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(CASTTIME);
        } else {
            return this.CastDuration(stack);
        }
    }

    @Nonnull
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Nonnull
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (!(this.getSpell(stack) instanceof ChargingSpells)){
            this.MagicResults(stack, worldIn, entityLiving);
        }
        if (stack.getTag().getInt(COOL) > 0){
            stack.getTag().putInt(COOL, 0);
        }
        return stack;
    }

    @Nonnull
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!playerIn.isCrouching()) {
            if (this.getSpell(itemstack) != null) {
                playerIn.startUsingItem(handIn);
                for (int i = 0; i < playerIn.level.random.nextInt(35) + 10; ++i) {
                    double d = worldIn.random.nextGaussian() * 0.2D;
                    playerIn.level.addParticle(ParticleTypes.ENTITY_EFFECT, playerIn.getX(), playerIn.getEyeY(), playerIn.getZ(), d, d, d);
                }
            }
            return ActionResult.consume(itemstack);
        } else {
            if (!worldIn.isClientSide) {
                SimpleNamedContainerProvider provider = new SimpleNamedContainerProvider(
                        (id, inventory, player) -> new SoulItemContainer(id, inventory, SoulUsingItemHandler.get(itemstack), itemstack, handIn), getName(itemstack));
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, provider, (buffer) -> buffer.writeBoolean(handIn == Hand.MAIN_HAND));
            }
            return ActionResult.pass(itemstack);
        }

    }

    public void ChangeFocus(ItemStack itemStack){
        if (getFocus(itemStack).getTag().getString(FOCUS).contains("vexing")) {
            this.setSpellConditions(new VexSpell(), itemStack);
            this.setSpell(0, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("biting")) {
            this.setSpellConditions(new FangSpell(), itemStack);
            this.setSpell(1, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("roaring")) {
            this.setSpellConditions(new RoarSpell(), itemStack);
            this.setSpell(2, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("necroturgy")) {
            this.setSpellConditions(new ZombieSpell(), itemStack);
            this.setSpell(3, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("osseous")) {
            this.setSpellConditions(new SkeletonSpell(), itemStack);
            this.setSpell(4, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("crippling")) {
            this.setSpellConditions(new CrippleSpell(), itemStack);
            this.setSpell(5, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("spiderling")) {
            this.setSpellConditions(new SpiderlingSpell(), itemStack);
            this.setSpell(6, itemStack);
        } else if (getFocus(itemStack).getTag().getString(FOCUS).contains("brain")) {
            this.setSpellConditions(new BrainEaterSpell(), itemStack);
            this.setSpell(7, itemStack);
        }
    }

    public void setSpellConditions(Spells spell, ItemStack stack){
        assert stack.getTag() != null;
        if (spell != null) {
            stack.getTag().putInt(SOULCOST, spell.SoulCost());
            stack.getTag().putInt(DURATION, spell.CastDuration());
            if (spell instanceof ChargingSpells){
                stack.getTag().putInt(COOLDOWN, ((ChargingSpells) spell).Cooldown());
            }
        } else {
            stack.getTag().putInt(SOULCOST, 0);
            stack.getTag().putInt(DURATION, 0);
            stack.getTag().putInt(COOLDOWN, 0);
        }
    }

    public void setSpell(int spellint, ItemStack stack) {
        assert stack.getTag() != null;
        stack.getTag().putInt(SPELL, spellint);
    }

    public Spells getSpell(ItemStack stack){
        assert stack.getTag() != null;
        return new CastSpells(stack.getTag().getInt(SPELL)).getSpell();
    }

    public int SoulCost(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return 0;
        } else {
            return itemStack.getTag().getInt(SOULCOST);
        }
    }

    public int CastDuration(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        return itemStack.getTag().getInt(DURATION);
    }

    public int Cooldown(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        return itemStack.getTag().getInt(COOLDOWN);
    }

    public SoundEvent CastingSound(ItemStack stack) {
        return this.getSpell(stack).CastingSound();
    }

    public static ItemStack getFocus(ItemStack itemstack) {
        SoulUsingItemHandler handler = SoulUsingItemHandler.get(itemstack);
        return handler.getSlot();
    }

    public void MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack foundStack = ItemStack.EMPTY;
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                foundStack = itemStack;
                break;
            }
        }
        if (this.getSpell(stack) != null && !foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) >= SoulUse(entityLiving, stack)) {
            GoldTotemItem.decreaseSouls(foundStack, SoulUse(entityLiving, stack));
            assert stack.getTag() != null;
            this.getSpell(stack).WandResult(worldIn, entityLiving);
        } else {
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                double d = worldIn.random.nextGaussian() * 0.2D;
                entityLiving.level.addParticle(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), d, d, d);
            }
        }
    }

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT result = new CompoundNBT();
        CompoundNBT tag = super.getShareTag(stack);
        CompoundNBT cap = SoulUsingItemHandler.get(stack).serializeNBT();
        if (tag != null) {
            result.put("tag", tag);
        }
        if (cap != null) {
            result.put("cap", cap);
        }
        return result;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        assert nbt != null;
        stack.setTag(nbt.getCompound("tag"));
        SoulUsingItemHandler.get(stack).deserializeNBT(nbt.getCompound("cap"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new SoulUsingItemCapability(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int SoulUse = stack.getTag().getInt(SOULUSE);
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.cost", SoulUse));
        } else {
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.cost", SoulCost(stack)));
        }
        if (getFocus(stack) != ItemStack.EMPTY){
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.focus", getFocus(stack).getItem().getDescription()));
        } else {
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.focus", "Empty"));
        }
    }
}
