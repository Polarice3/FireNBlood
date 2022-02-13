package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.world.structures.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RegistryStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, FireNBlood.MOD_ID);
    public static final RegistryObject<Structure<NoFeatureConfig>> TAVERN = STRUCTURES.register("tavern", () -> (new TavernStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> PROFANEDTOWER = STRUCTURES.register("profanedtower", () -> (new ProfanedTowerStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> PROFANEDSHRINE = STRUCTURES.register("profanedshrine", () -> (new ProfanedShrineStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> DARKMANOR = STRUCTURES.register("darkmanor", () -> (new DarkManorStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> PORTAL_OUTPOST = STRUCTURES.register("portal_outpost", () -> (new PortalOutpostStructure(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> CURSED_GRAVEYARD = STRUCTURES.register("cursed_graveyard", () -> (new CursedGraveyardStructure(NoFeatureConfig.CODEC)));

    public static void init(){
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(
                TAVERN.get(),
                new StructureSeparationSettings(60,
                        20,
                        1234567890),
                true);
        setupMapSpacingAndLand(
                PROFANEDTOWER.get(),
                new StructureSeparationSettings(100,
                        80,
                        1321321320),
                true);
        setupMapSpacingAndLand(
                PROFANEDSHRINE.get(),
                new StructureSeparationSettings(80,
                        60,
                        1432143214),
                true);
        setupMapSpacingAndLand(
                DARKMANOR.get(),
                new StructureSeparationSettings(60,
                        20,
                        1543212345),
                true);
        setupMapSpacingAndLand(
                PORTAL_OUTPOST.get(),
                new StructureSeparationSettings(40,
                        20,
                        1654323456),
                true);
        setupMapSpacingAndLand(
                CURSED_GRAVEYARD.get(),
                new StructureSeparationSettings(32,
                        5,
                        1765434567),
                true);

    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
