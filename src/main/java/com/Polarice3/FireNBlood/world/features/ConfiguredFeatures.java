package com.Polarice3.FireNBlood.world.features;

import com.Polarice3.FireNBlood.utils.RegistryFeatures;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> CONFIGURED_CURSEDTOTEM = register("cursedtotemfeature", RegistryFeatures.CURSEDTOTEM.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(256));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}
