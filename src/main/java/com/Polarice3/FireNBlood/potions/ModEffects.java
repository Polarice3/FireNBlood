package com.Polarice3.FireNBlood.potions;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects extends Effect{
    public ModEffects(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }
}
