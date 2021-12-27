package com.Polarice3.FireNBlood;

import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.init.ModItems;
import com.Polarice3.FireNBlood.potions.ModPotions;
import com.Polarice3.FireNBlood.structures.ConfiguredStructures;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("firenblood")
public class FireNBlood
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "firenblood";

    public FireNBlood() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModEntityType.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);

        // For events that happen after initialization. This is probably going to be use a lot.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        // The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

        RegistryHandler.init();
        RegistryStructures.init();
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
            GlobalEntityTypeAttributes.put(ModEntityType.FRIENDLY_VEX.get(), FriendlyVexEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.VIZIER.get(), VizierEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.IRK.get(), IrkEntity.setCustomAttributes().create());
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityType.PENANCE.get(), PenanceEntity.setCustomAttributes().create());
        });

        event.enqueueWork(() -> {
            RegistryStructures.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });

    }

    public void biomeModification(final BiomeLoadingEvent event) {
        /*
         * Add our structure to all biomes including other modded biomes.
         * You can skip or add only to certain biomes based on stuff like biome category,
         * temperature, scale, precipitation, mod id, etc. All kinds of options!
         *
         * You can even use the BiomeDictionary as well! To use BiomeDictionary, do
         * RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()) to get the biome's
         * registrykey. Then that can be fed into the dictionary to get the biome's types.
         */
        if (event.getCategory() == Biome.Category.TAIGA
        || event.getCategory() == Biome.Category.PLAINS
        || event.getCategory() == Biome.Category.SAVANNA) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_TAVERN);
        }
/*
        event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_PROFANEDTOWER);
*/
    }

    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            /*
             * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
             * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
             * This here is done with reflection as this tutorial is not about setting up and using Mixins.
             * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
             */
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                FireNBlood.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            /*
             * Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also that vanilla superflat is really tricky and buggy to work with in my experience.
             */
            if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)){
                return;
            }

            /*
             * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
             * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
             *
             * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
             * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
             * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
             */
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.putIfAbsent(RegistryStructures.TAVERN.get(), DimensionStructuresSettings.field_236191_b_.get(RegistryStructures.TAVERN.get()));
/*
            tempMap.putIfAbsent(RegistryStructures.PROFANEDTOWER.get(), DimensionStructuresSettings.field_236191_b_.get(RegistryStructures.PROFANEDTOWER.get()));
*/
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
