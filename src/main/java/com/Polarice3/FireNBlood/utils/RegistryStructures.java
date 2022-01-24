package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.world.structures.ProfanedShrineStructure;
import com.Polarice3.FireNBlood.world.structures.ProfanedTowerStructure;
import com.Polarice3.FireNBlood.world.structures.StructurePieces;
import com.Polarice3.FireNBlood.world.structures.TavernStructure;
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
                new StructureSeparationSettings(60,
                        40,
                        1432143214),
                true);
        StructurePieces.registerStructurePieces();

    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            Structure.field_236384_t_ =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.field_236384_t_)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();

        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().getStructures().field_236193_d_ = tempMap;
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
