package com.Polarice3.FireNBlood.structures;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.Polarice3.FireNBlood.utils.RegistryStructures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_TAVERN = RegistryStructures.TAVERN.get().withConfiguration(NoFeatureConfig.field_236559_b_);
/*
    public static StructureFeature<?, ?> CONFIGURED_PROFANEDTOWER = RegistryStructures.PROFANEDTOWER.get().withConfiguration(NoFeatureConfig.field_236559_b_);
*/

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_tavern"), CONFIGURED_TAVERN);
        FlatGenerationSettings.STRUCTURES.put(RegistryStructures.TAVERN.get(), CONFIGURED_TAVERN);
/*        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_profanedtower"), CONFIGURED_PROFANEDTOWER);
        FlatGenerationSettings.STRUCTURES.put(RegistryStructures.PROFANEDTOWER.get(), CONFIGURED_PROFANEDTOWER);*/
    }
}
