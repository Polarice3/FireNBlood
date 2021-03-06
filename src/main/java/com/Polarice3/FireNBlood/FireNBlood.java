package com.Polarice3.FireNBlood;

import com.Polarice3.FireNBlood.compat.CuriosCompat;
import com.Polarice3.FireNBlood.enchantments.ModEnchantmentsType;
import com.Polarice3.FireNBlood.entities.ally.*;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.hostile.cultists.*;
import com.Polarice3.FireNBlood.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.*;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.entities.neutral.protectors.*;
import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.init.ModItems;
import com.Polarice3.FireNBlood.inventory.container.ModContainerType;
import com.Polarice3.FireNBlood.particles.ModParticleTypes;
import com.Polarice3.FireNBlood.potions.ModPotions;
import com.Polarice3.FireNBlood.tileentities.ModTileEntityType;
import com.Polarice3.FireNBlood.utils.*;
import com.Polarice3.FireNBlood.world.features.ConfiguredFeatures;
import com.Polarice3.FireNBlood.world.structures.ConfiguredStructures;
import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.params.CoreProtocolPNames.PROTOCOL_VERSION;

@Mod("firenblood")
public class FireNBlood
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "firenblood";

    public static SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(location("general"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static ResourceLocation location(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    public FireNBlood() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModEntityType.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModTileEntityType.TILEENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModContainerType.CONTAINER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModEnchantmentsType.ENCHANTMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModParticleTypes.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

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
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_WRETCH.get(), TaillessWretchEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_DRUID.get(), TaillessDruidEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_HORROR.get(), TaillessHorrorEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BLACK_BULL.get(), BlackBullEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BULLET.get(), BulletEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ROYALBULLET.get(), RoyalBulletEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MINOTAUR.get(), MinotaurEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TAILLESS_PROPHET.get(), TaillessProphetEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ANATHEMA.get(), TaillessAnathemaEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.TANK.get(), AbstractTankEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDTANK.get(), AbstractTankEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CALLER.get(), CallerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.REDEMPTOR.get(), RedemptorEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PROTECTOR.get(), ProtectorEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.BREWER.get(), BrewerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.HEXER.get(), HexerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MIRAGE.get(), MirageEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SAVAGER.get(), SavagerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.NEOPHYTE.get(), NeophyteEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ACOLYTE.get(), AcolyteEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CHANNELLER.get(), ChannellerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FANATIC.get(), FanaticEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZEALOT.get(), ZealotEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.DISCIPLE.get(), DiscipleEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.APOSTLE.get(), ApostleEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ZombieVillagerMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SKELETON_VILLAGER_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ENVIOKER.get(), EnviokerEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_COW.get(), MutatedCowEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_CHICKEN.get(), MutatedChickenEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_SHEEP.get(), MutatedSheepEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_PIG.get(), MutatedPigEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.MUTATED_RABBIT.get(), MutatedRabbitEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SACRED_FISH.get(), SacredFishEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PARASITE.get(), ParasiteEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_SCORCH.get(), FriendlyScorchEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.ZOMBIE_MINION.get(), ZombieMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SKELETON_MINION.get(), SkeletonMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SPIDERLING_MINION.get(), SpiderlingMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.CREEPERLING_MINION.get(), CreeperlingMinionEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.VIZIER.get(), VizierEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.IRK.get(), IrkEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.SCORCH.get(), ScorchEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.NETHERNAL.get(), NethernalEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PENANCE.get(), PenanceEntity.setCustomAttributes().build());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.FAKESEAT.get(), FakeSeatEntity.setCustomAttributes().build());
        });

        CuriosCompat.setup(event);

        event.enqueueWork(() -> {
            RegistryStructures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });

    }

    public void biomeModification(final BiomeLoadingEvent event) {

        if (FNBConfig.TavernGen.get()) {
            if (event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.PLAINS
                    || event.getCategory() == Biome.Category.SAVANNA
                    || event.getCategory() == Biome.Category.ICY) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_TAVERN);
            }
        }
        if (FNBConfig.ProfanedShrineGen.get()) {
            if (event.getCategory() == Biome.Category.PLAINS
                    || event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.SAVANNA
                    || event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.DESERT) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PROFANEDSHRINE);
            }
        }
        if (FNBConfig.ProfanedTowerGen.get()) {
            if (event.getCategory() == Biome.Category.PLAINS
                    || event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.SAVANNA
                    || event.getCategory() == Biome.Category.TAIGA) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PROFANEDTOWER);
            }
        }
        if (FNBConfig.DarkManorGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.ICY
                    || event.getCategory() == Biome.Category.TAIGA) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_DARKMANOR);
            }
        }
        if (FNBConfig.CursedGraveyardGen.get()) {
            if (event.getCategory() == Biome.Category.FOREST
                    || event.getCategory() == Biome.Category.SAVANNA
                    || event.getCategory() == Biome.Category.TAIGA
                    || event.getCategory() == Biome.Category.PLAINS
                    || event.getCategory() == Biome.Category.EXTREME_HILLS
                    || event.getCategory() == Biome.Category.SWAMP) {
                event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_CURSED_GRAVEYARD);
            }
        }
        if (FNBConfig.TotemGen.get()) {
            if (event.getCategory() != Biome.Category.THEEND
                    && event.getCategory() != Biome.Category.MUSHROOM
                    && event.getCategory() != Biome.Category.NETHER
                    && event.getCategory() != Biome.Category.OCEAN
                    && event.getCategory() != Biome.Category.RIVER) {
                event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.CONFIGURED_CURSEDTOTEM);
            }
        }
        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PORTAL_OUTPOST);

    }

    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                FireNBlood.LOGGER.error("Was unable to check if " + serverWorld.dimension().getRegistryName() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.dimension().equals(World.OVERWORLD)){
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(RegistryStructures.TAVERN.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.TAVERN.get()));
            tempMap.putIfAbsent(RegistryStructures.PROFANEDTOWER.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.PROFANEDTOWER.get()));
            tempMap.putIfAbsent(RegistryStructures.PROFANEDSHRINE.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.PROFANEDSHRINE.get()));
            tempMap.putIfAbsent(RegistryStructures.DARKMANOR.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.DARKMANOR.get()));
            tempMap.putIfAbsent(RegistryStructures.PORTAL_OUTPOST.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.PORTAL_OUTPOST.get()));
            tempMap.putIfAbsent(RegistryStructures.CURSED_GRAVEYARD.get(), DimensionStructuresSettings.DEFAULTS.get(RegistryStructures.CURSED_GRAVEYARD.get()));

            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    public static final ItemGroup TAB = new ItemGroup("firenbloodTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegistryHandler.GOLDTOTEM.get());
        }
    };
}
