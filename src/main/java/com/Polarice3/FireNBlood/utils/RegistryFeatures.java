package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.world.features.CursedTotemFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, FireNBlood.MOD_ID);
    public static final RegistryObject<Feature<NoFeatureConfig>> CURSEDTOTEM = FEATURES.register("cursedtotemfeature", () -> (new CursedTotemFeature(NoFeatureConfig.CODEC)));

    public static void init(){
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
