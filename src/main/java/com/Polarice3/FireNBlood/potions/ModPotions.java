package com.Polarice3.FireNBlood.potions;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, FireNBlood.MOD_ID);

    public static final RegistryObject<Potion> MINING_FATIGUE = POTIONS.register("mining_fatigue",
            () -> new Potion("mining_fatigue", new EffectInstance(Effects.MINING_FATIGUE, 1800, 1)));

    public static final RegistryObject<Potion> CURSED = POTIONS.register("cursed",
            () -> new Potion("cursed", new EffectInstance(RegistryHandler.CURSED.get(), 1800)));

    public static final RegistryObject<Potion> MINOR_HARM = POTIONS.register("minorharm",
            () -> new Potion("minorharm", new EffectInstance(RegistryHandler.MINOR_HARM.get(), 1)));
}
