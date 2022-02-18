package com.Polarice3.FireNBlood.events;

/*import com.Polarice3.FireNBlood.FireNBlood;
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
        World level = event.player.level;
        if (player.hasEffect(RegistryHandler.EVIL_EYE.get()) && player.level.dimensionType().bedWorks()) {
            int evileyeLevel = 0;
            evileyeLevel += player.getEffect(RegistryHandler.EVIL_EYE.get()).getAmplifier() + 1;
            evileyeLevel = MathHelper.clamp(evileyeLevel, 0, 5);
            int random = level.random.nextInt(120);
            if (random == 0) {
                int random2 = level.random.nextInt(40);
                float f = level.random.nextFloat() * ((float) Math.PI * 2F);
                double d = (level.random.nextBoolean() ? 1: -1);
                double e = (level.random.nextBoolean() ? 1: -1);
                double g = (level.random.nextBoolean() ? 32: 16);
                double d0 = player.getX() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (g * d);
                double d2 = player.getZ() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (g * e);
                double d1 = level.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d0, (int) d2);
                if (evileyeLevel == 1) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackbull.setPos(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            level.addFreshEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                        wretch.setPos(d0, d1, d2);
                        wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                        wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                        level.addFreshEntity(wretch);
                    }
                }
                if (evileyeLevel == 2) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackbull.setPos(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            level.addFreshEntity(blackbull);
                        }
                    }
                    if (random2 == 2) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                        wretch.setPos(d0, d1, d2);
                        wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                        wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                        level.addFreshEntity(wretch);
                    }
                    if (random2 == 4) {
                        TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), level);
                        horror.setPos(player.getRandomX(4.0F), d1 + 16.0D, player.getRandomZ(4.0F));
                        horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                        level.addFreshEntity(horror);
                    }
                }
                if (evileyeLevel == 3) {
                    if (random2 == 0) {
                        for (int i = 0; i < 3; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackbull.setPos(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            level.addFreshEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), level);
                        horror.setPos(player.getRandomX(4.0F), d1 + 16.0D, player.getRandomZ(4.0F));
                        horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                        level.addFreshEntity(horror);
                    }
                    if (random2 == 6) {
                        TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), level);
                        druid.setPos(d0, d1, d2);
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        level.addFreshEntity(druid);
                    }
                }
                if (evileyeLevel == 4) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackbull.setPos(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            level.addFreshEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), level);
                            horror.setPos(player.getRandomX(4.0F), d1 + 16.0D, player.getRandomZ(4.0F));
                            horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                            level.addFreshEntity(horror);
                        }
                    }
                    if (random2 == 6) {
                        TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), level);
                        druid.setPos(d0, d1, d2);
                        druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                        level.addFreshEntity(druid);
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), level);
                        tank.setPos(d0, d1, d2);
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        level.addFreshEntity(tank);
                    }
                }
                if (evileyeLevel == 5) {
                    if (random2 == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackbull.setPos(d0, d1, d2);
                            blackbull.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackbull));
                            level.addFreshEntity(blackbull);
                        }
                        for (int i = 0; i < 2; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 2) {
                        for (int i = 0; i < 4; ++i) {
                            TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            wretch.setPos(d0, d1, d2);
                            wretch.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            wretch.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(wretch));
                            level.addFreshEntity(wretch);
                        }
                    }
                    if (random2 == 4) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), level);
                            horror.setPos(player.getRandomX(4.0F), d1 + 16.0D, player.getRandomZ(4.0F));
                            horror.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(horror));
                            level.addFreshEntity(horror);
                        }
                    }
                    if (random2 == 6) {
                        for (int i = 0; i < 2; ++i) {
                            TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), level);
                            druid.setPos(d0, d1, d2);
                            druid.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(druid));
                            level.addFreshEntity(druid);
                        }
                    }
                    if (random2 == 8) {
                        TankEntity tank = new TankEntity(ModEntityType.TANK.get(), level);
                        tank.setPos(d0, d1, d2);
                        tank.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(tank));
                        level.addFreshEntity(tank);
                    }
                    if (random2 == 10) {
                        int random3 = level.random.nextInt(120);
                        if (random3 == 2) {
                            MinotaurEntity minotaur = new MinotaurEntity(ModEntityType.MINOTAUR.get(), level);
                            minotaur.setPos(d0, d1, d2);
                            minotaur.setItemInHand(Hand.MAIN_HAND, new ItemStack(RegistryHandler.DIAMOND_MACE.get()));
                            minotaur.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(minotaur));
                            level.addFreshEntity(minotaur);
                        }
                        if (random3 == 4) {
                            TaillessProphetEntity prophet = new TaillessProphetEntity(ModEntityType.TAILLESS_PROPHET.get(), level);
                            prophet.setPos(d0, d1, d2);
                            prophet.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(prophet));
                            level.addFreshEntity(prophet);
                        }
                        if (random3 == 6) {
                            TaillessAnathemaEntity anathema = new TaillessAnathemaEntity(ModEntityType.ANATHEMA.get(), level);
                            anathema.setPos(d0, d1, d2);
                            anathema.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(anathema));
                            level.addFreshEntity(anathema);
                        }
                    }
                }
            }

        }
    }

}*/
