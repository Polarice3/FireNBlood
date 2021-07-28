package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.structures.StructurePieces;
import com.Polarice3.FireNBlood.structures.TavernStructure;
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
    public static final RegistryObject<Structure<NoFeatureConfig>> TAVERN = STRUCTURES.register("tavern", () -> (new TavernStructure(NoFeatureConfig.field_236558_a_)));

    public static void init(){
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(
                TAVERN.get(), /* The instance of the structure */
                new StructureSeparationSettings(10 /* average distance apart in chunks between spawn attempts */,
                        5 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        1234567890 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                true);
        StructurePieces.registerStructurePieces();

        // Add more structures here and so on
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
