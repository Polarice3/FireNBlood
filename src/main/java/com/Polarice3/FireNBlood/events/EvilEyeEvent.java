package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ai.MoveTowardsTargetGoal;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.init.ModItems;
import com.Polarice3.FireNBlood.items.DiamondMaceItem;
import com.Polarice3.FireNBlood.items.GoldenMaceItem;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Random;


@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
                int random0 = world.rand.nextInt(2);
                float f = world.rand.nextFloat() * ((float) Math.PI * 2F);
                double d0 = player.getPosX() + MathHelper.floor(MathHelper.cos(f) * 32.0D + world.rand.nextInt(5)) + 32.0D;
                double d2 = player.getPosZ() + MathHelper.floor(MathHelper.cos(f) * 32.0D + world.rand.nextInt(5)) + 32.0D;
                double d1 = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d0, (int) d2);
                double d7 = player.getPosX() - MathHelper.floor(MathHelper.cos(f) * 32.0D + world.rand.nextInt(5)) + 32.0D;
                double d9 = player.getPosZ() - MathHelper.floor(MathHelper.cos(f) * 32.0D + world.rand.nextInt(5)) + 32.0D;
                double d8 = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d7, (int) d9);
                if (evileyeLevel == 1) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            if (random0 == 1) {
                                blackbull.setPosition(d0, d1, d2);
                            } else {
                                blackbull.setPosition(d7, d8, d9);
                            }
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                        if (random0 == 1) {
                            wretch.setPosition(d0, d1, d2);
                        } else {
                            wretch.setPosition(d7, d8, d9);
                        }
                        wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                        wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                        world.addEntity(wretch);
                    }
                }
                if (evileyeLevel == 2) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            if (random0 == 1) {
                                blackbull.setPosition(d0, d1, d2);
                            } else {
                                blackbull.setPosition(d7, d8, d9);
                            }
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                        if (random0 == 1) {
                            wretch.setPosition(d0, d1, d2);
                        } else {
                            wretch.setPosition(d7, d8, d9);
                        }
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
                            if (random0 == 1) {
                                blackbull.setPosition(d0, d1, d2);
                            } else {
                                blackbull.setPosition(d7, d8, d9);
                            }
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
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
                        if (random0 == 1) {
                            druid.setPosition(d0, d1, d2);
                        } else {
                            druid.setPosition(d7, d8, d9);
                        }
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        world.addEntity(druid);
                    }
                }
                if (evileyeLevel == 4) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            if (random0 == 1) {
                                blackbull.setPosition(d0, d1, d2);
                            } else {
                                blackbull.setPosition(d7, d8, d9);
                            }
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
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
                        if (random0 == 1) {
                            druid.setPosition(d0, d1, d2);
                        } else {
                            druid.setPosition(d7, d8, d9);
                        }
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        world.addEntity(druid);
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), world);
                        if (random0 == 1) {
                            tank.setPosition(d0, d1, d2);
                        } else {
                            tank.setPosition(d7, d8, d9);
                        }
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        world.addEntity(tank);
                    }
                }
                if (evileyeLevel == 5) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            if (random0 == 1) {
                                blackbull.setPosition(d0, d1, d2);
                            } else {
                                blackbull.setPosition(d7, d8, d9);
                            }
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            world.addEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
                            wretch.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            world.addEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 4; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            if (random0 == 1) {
                                wretch.setPosition(d0, d1, d2);
                            } else {
                                wretch.setPosition(d7, d8, d9);
                            }
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
                            if (random0 == 1) {
                                druid.setPosition(d0, d1, d2);
                            } else {
                                druid.setPosition(d7, d8, d9);
                            }
                            druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                            world.addEntity(druid);
                        }
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), world);
                        if (random0 == 1) {
                            tank.setPosition(d0, d1, d2);
                        } else {
                            tank.setPosition(d7, d8, d9);
                        }
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        world.addEntity(tank);
                    }
                    if (random2 == 10) {
                        int random3 = world.rand.nextInt(120);
                        if (random3 == 2) {
                            MinotaurEntity minotaur = new MinotaurEntity(ModEntityType.MINOTAUR.get(), world);
                            if (random0 == 1) {
                                minotaur.setPosition(d0, d1, d2);
                            } else {
                                minotaur.setPosition(d7, d8, d9);
                            }
                            minotaur.setHeldItem(Hand.MAIN_HAND, new ItemStack(RegistryHandler.DIAMOND_MACE.get()));
                            minotaur.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(minotaur));
                            world.addEntity(minotaur);
                        }
                        if (random3 == 4) {
                            TaillessProphetEntity prophet = new TaillessProphetEntity(ModEntityType.TAILLESS_PROPHET.get(), world);
                            if (random0 == 1) {
                                prophet.setPosition(d0, d1, d2);
                            } else {
                                prophet.setPosition(d7, d8, d9);
                            }
                            prophet.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(prophet));
                            world.addEntity(prophet);
                        }
                        if (random3 == 6) {
                            TaillessAnathemaEntity anathema = new TaillessAnathemaEntity(ModEntityType.ANATHEMA.get(), world);
                            if (random0 == 1) {
                                anathema.setPosition(d0, d1, d2);
                            } else {
                                anathema.setPosition(d7, d8, d9);
                            }
                            anathema.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(anathema));
                            world.addEntity(anathema);
                        }
                    }
                }
            }

        }
    }

/*    @Nullable
    private BlockPos func_234561_a_(IWorldReader p_234561_1_, BlockPos p_234561_2_, int p_234561_3_) {
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            int j = p_234561_2_.getX() + this.random.nextInt(p_234561_3_ * 2) - p_234561_3_;
            int k = p_234561_2_.getZ() + this.random.nextInt(p_234561_3_ * 2) - p_234561_3_;
            int l = p_234561_1_.getHeight(Heightmap.Type.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, p_234561_1_, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }*/
}
