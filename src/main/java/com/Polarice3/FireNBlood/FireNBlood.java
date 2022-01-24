package com.Polarice3.FireNBlood;

import com.Polarice3.FireNBlood.entities.ally.*;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.init.ModItems;
import com.Polarice3.FireNBlood.potions.ModPotions;
import com.Polarice3.FireNBlood.utils.RegistryFeatures;
import com.Polarice3.FireNBlood.world.features.ConfiguredFeatures;
import com.Polarice3.FireNBlood.world.structures.ConfiguredStructures;
import com.Polarice3.FireNBlood.tileentities.ModTileEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.Polarice3.FireNBlood.utils.RegistryStructures;
import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod("firenblood")
public class FireNBlood
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "firenblood";

    public FireNBlood() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModEntityType.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModTileEntityType.TILEENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FNBConfig.SPEC, "firenblood.toml");

        FNBConfig.loadConfig(FNBConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("firenblood.toml").toString());

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

        RegistryHandler.init();
        RegistryStructures.init();
        RegistryFeatures.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_WRETCH.get(), TaillessWretchEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_DRUID.get(), TaillessDruidEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_HORROR.get(), TaillessHorrorEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BLACK_BULL.get(), BlackBullEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BULLET.get(), BulletEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ROYALBULLET.get(), RoyalBulletEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MINOTAUR.get(), MinotaurEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_PROPHET.get(), TaillessProphetEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ANATHEMA.get(), TaillessAnathemaEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TANK.get(), TankEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDTANK.get(), FriendlyTankEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CALLER.get(), CallerEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.REDEMPTOR.get(), RedemptorEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PROTECTOR.get(), ProtectorEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BREWER.get(), BrewerEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.HEXER.get(), HexerEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.QUELL.get(), QuellEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MIRAGE.get(), MirageEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SAVAGER.get(), SavagerEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.NEOPHYTE.get(), NeophyteEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ACOLYTE.get(), AcolyteEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CHANNELLER.get(), ChannellerEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_COW.get(), MutatedCowEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_PIG.get(), MutatedPigEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PARASITE.get(), ParasiteEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyScorchEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SKELETON_MINION.get(), SkeletonMinionEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.VIZIER.get(), VizierEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.IRK.get(), IrkEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SCORCH.get(), ScorchEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PENANCE.get(), PenanceEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FAKESEAT.get(), FakeSeatEntity.setCustomAttributes().create());
        });

        event.enqueueWork(() -> {
            RegistryStructures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });

    }

    public void biomeModification(final BiomeLoadingEvent event) {

        if (event.getCategory() == Biome.Category.TAIGA
        || event.getCategory() == Biome.Category.PLAINS
        || event.getCategory() == Biome.Category.SAVANNA) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_TAVERN);
        }
        if (event.getCategory() == Biome.Category.PLAINS
        || event.getCategory() == Biome.Category.FOREST
        || event.getCategory() == Biome.Category.SAVANNA
        || event.getCategory() == Biome.Category.TAIGA
        || event.getCategory() == Biome.Category.DESERT) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PROFANEDSHRINE);
        }
        if (event.getCategory() != Biome.Category.THEEND
                && event.getCategory() != Biome.Category.MUSHROOM
                && event.getCategory() != Biome.Category.NETHER
                && event.getCategory() != Biome.Category.OCEAN
                && event.getCategory() != Biome.Category.RIVER){
            event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.CONFIGURED_CURSEDTOTEM);
        }
        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PROFANEDTOWER);
    }

    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                FireNBlood.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)){
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.putIfAbsent(RegistryStructures.TAVERN.get(), DimensionStructuresSettings.field_236191_b_.get(RegistryStructures.TAVERN.get()));
            tempMap.putIfAbsent(RegistryStructures.PROFANEDTOWER.get(), DimensionStructuresSettings.field_236191_b_.get(RegistryStructures.PROFANEDTOWER.get()));
            tempMap.putIfAbsent(RegistryStructures.PROFANEDSHRINE.get(), DimensionStructuresSettings.field_236191_b_.get(RegistryStructures.PROFANEDSHRINE.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }

    public static final ItemGroup TAB = new ItemGroup("firenbloodTab") {
        @Override
        public ItemStack createIcon(){
            return new ItemStack(Items.GOLD_INGOT);
        }

    };


}
