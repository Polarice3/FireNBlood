package com.Polarice3.FireNBlood.world.structures;

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
    public static StructureFeature<?, ?> CONFIGURED_TAVERN = RegistryStructures.TAVERN.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PROFANEDTOWER = RegistryStructures.PROFANEDTOWER.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PROFANEDSHRINE= RegistryStructures.PROFANEDSHRINE.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_DARKMANOR= RegistryStructures.DARKMANOR.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PORTAL_OUTPOST= RegistryStructures.PORTAL_OUTPOST.get().configured(NoFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_CURSED_GRAVEYARD= RegistryStructures.CURSED_GRAVEYARD.get().configured(NoFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_tavern"), CONFIGURED_TAVERN);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.TAVERN.get(), CONFIGURED_TAVERN);
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_profanedtower"), CONFIGURED_PROFANEDTOWER);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.PROFANEDTOWER.get(), CONFIGURED_PROFANEDTOWER);
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_profanedshrine"), CONFIGURED_PROFANEDSHRINE);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.PROFANEDSHRINE.get(), CONFIGURED_PROFANEDSHRINE);
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_darkmanor"), CONFIGURED_DARKMANOR);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.DARKMANOR.get(), CONFIGURED_DARKMANOR);
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_portal_outpost"), CONFIGURED_PORTAL_OUTPOST);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.PORTAL_OUTPOST.get(), CONFIGURED_PORTAL_OUTPOST);
        Registry.register(registry, new ResourceLocation(FireNBlood.MOD_ID, "configured_cursed_graveyard"), CONFIGURED_CURSED_GRAVEYARD);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(RegistryStructures.CURSED_GRAVEYARD.get(), CONFIGURED_CURSED_GRAVEYARD);
    }
}
