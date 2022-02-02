package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ai.MoveTowardsTargetGoal;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessDruidEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessHorrorEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EvilEyeEvent {

    @SubscribeEvent
    public static void EvilEyeEvent(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = event.player.world;
        if (player.isPotionActive(RegistryHandler.EVIL_EYE.get()) && player.world.getDimensionType().doesBedWork()) {
            int evileyeLevel = 0;
            evileyeLevel += player.getActivePotionEffect(RegistryHandler.EVIL_EYE.get()).getAmplifier() + 1;
            evileyeLevel = MathHelper.clamp(evileyeLevel, 0, 5);
            int random = world.rand.nextInt(120);
            if (random == 0) {
                int random2 = world.rand.nextInt(40);
                float f = world.rand.nextFloat() * ((float) Math.PI * 2F);
                double d = (world.rand.nextBoolean() ? 1: -1);
                double e = (world.rand.nextBoolean() ? 1: -1);
                double g = (world.rand.nextBoolean() ? 32: 16);
                double d0 = player.getPosX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (g * d);
                double d2 = player.getPosZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (g * e);
                double d1 = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d0, (int) d2);
                if (evileyeLevel == 1) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackbull.setPosition(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                        wretch.setPosition(d0, d1, d2);
                        wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                        wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                        world.addEntity(wretch);
                    }
                }
                if (evileyeLevel == 2) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackbull.setPosition(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                        wretch.setPosition(d0, d1, d2);
                        wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                        wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                        world.addEntity(wretch);
                    }
                    if (random2 == 4) {
                        TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                        horror.setPosition(player.getPosXRandom(4.0F), d1 + 16.0D, player.getPosZRandom(4.0F));
                        horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                        world.addEntity(horror);
                    }
                }
                if (evileyeLevel == 3) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackbull.setPosition(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                        horror.setPosition(player.getPosXRandom(4.0F), d1 + 16.0D, player.getPosZRandom(4.0F));
                        horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                        world.addEntity(horror);
                    }
                    if (random2 == 6) {
                        TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), world);
                        druid.setPosition(d0, d1, d2);
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        world.addEntity(druid);
                    }
                }
                if (evileyeLevel == 4) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackbull.setPosition(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                            horror.setPosition(player.getPosXRandom(4.0F), d1 + 16.0D, player.getPosZRandom(4.0F));
                            horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                            world.addEntity(horror);
                        }
                    }
                    if (random2 == 6) {
                        TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), world);
                        druid.setPosition(d0, d1, d2);
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        world.addEntity(druid);
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), world);
                        tank.setPosition(d0, d1, d2);
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        world.addEntity(tank);
                    }
                }
                if (evileyeLevel == 5) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackbull.setPosition(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 4; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            wretch.setPosition(d0, d1, d2);
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                            horror.setPosition(player.getPosXRandom(4.0F), d1 + 16.0D, player.getPosZRandom(4.0F));
                            horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                            world.addEntity(horror);
                        }
                    }
                    if (random2 == 6) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), world);
                            druid.setPosition(d0, d1, d2);
                            druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                            world.addEntity(druid);
                        }
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), world);
                        tank.setPosition(d0, d1, d2);
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        world.addEntity(tank);
                    }
                    if (random2 == 10) {
                        int random3 = world.rand.nextInt(120);
                        if (random3 == 2) {
                            MinotaurEntity minotaur = new MinotaurEntity(ModEntityType.MINOTAUR.get(), world);
                            minotaur.setPosition(d0, d1, d2);
                            minotaur.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.DIAMOND_MACE.get()));
                            minotaur.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(minotaur));
                            world.addEntity(minotaur);
                        }
                        if (random3 == 4) {
                            TaillessProphetEntity prophet = new TaillessProphetEntity(ModEntityType.TAILLESS_PROPHET.get(), world);
                            prophet.setPosition(d0, d1, d2);
                            prophet.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(prophet));
                            world.addEntity(prophet);
                        }
                        if (random3 == 6) {
                            TaillessAnathemaEntity anathema = new TaillessAnathemaEntity(ModEntityType.ANATHEMA.get(), world);
                            anathema.setPosition(d0, d1, d2);
                            anathema.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(anathema));
                            world.addEntity(anathema);
                        }
                    }
                }
            }

        }
    }

}
