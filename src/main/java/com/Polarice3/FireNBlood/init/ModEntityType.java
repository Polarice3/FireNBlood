package com.Polarice3.FireNBlood.init;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.*;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.hostile.cultists.*;
import com.Polarice3.FireNBlood.entities.hostile.tailless.*;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.entities.neutral.protectors.*;
import com.Polarice3.FireNBlood.entities.projectiles.*;
import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import com.Polarice3.FireNBlood.entities.utilities.LightningTrapEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityType {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, FireNBlood.MOD_ID);

    public static final RegistryObject<EntityType<TaillessWretchEntity>> TAILLESS_WRETCH = ENTITY_TYPES.register("taillesswretch",
            () -> EntityType.Builder.create(TaillessWretchEntity::new, EntityClassification.MONSTER)
                    .size(1.5f, 2.8f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "taillesswretch").toString()));

    public static final RegistryObject<EntityType<TaillessDruidEntity>> TAILLESS_DRUID = ENTITY_TYPES.register("taillessdruid",
            () -> EntityType.Builder.create(TaillessDruidEntity::new, EntityClassification.MONSTER)
                    .size(1.5f, 2.8f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "taillessdruid").toString()));

    public static final RegistryObject<EntityType<TaillessHorrorEntity>> TAILLESS_HORROR = ENTITY_TYPES.register("taillesshorror",
            () -> EntityType.Builder.create(TaillessHorrorEntity::new, EntityClassification.MONSTER)
                    .size(2.0f, 1.0f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "taillesshorror").toString()));

    public static final RegistryObject<EntityType<BlackBullEntity>> BLACK_BULL = ENTITY_TYPES.register("blackbull",
            () -> EntityType.Builder.create(BlackBullEntity::new, EntityClassification.MONSTER)
                    .size(1.2f, 1.4f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "blackbull").toString()));

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITY_TYPES.register("bullet",
            () -> EntityType.Builder.create(BulletEntity::new, EntityClassification.MONSTER)
                    .size(0.8f, 0.8f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "bullet").toString()));

    public static final RegistryObject<EntityType<MinotaurEntity>> MINOTAUR = ENTITY_TYPES.register("minotaur",
            () -> EntityType.Builder.create(MinotaurEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(1.6f, 3.6f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "minotaur").toString()));

    public static final RegistryObject<EntityType<TankEntity>> TANK = ENTITY_TYPES.register("tank",
            () -> EntityType.Builder.create(TankEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(2.0f, 2.5f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "tank").toString()));

    public static final RegistryObject<EntityType<FriendlyTankEntity>> FRIENDTANK = ENTITY_TYPES.register("friendtank",
            () -> EntityType.Builder.create(FriendlyTankEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(2.0f, 2.5f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "friendtank").toString()));

    public static final RegistryObject<EntityType<TaillessProphetEntity>> TAILLESS_PROPHET = ENTITY_TYPES.register("taillessprophet",
            () -> EntityType.Builder.create(TaillessProphetEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(1.6f, 3.6f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "taillessprophet").toString()));

    public static final RegistryObject<EntityType<TaillessAnathemaEntity>> ANATHEMA = ENTITY_TYPES.register("anathema",
            () -> EntityType.Builder.create(TaillessAnathemaEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(4.0f, 4.0f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "anathema").toString()));

    public static final RegistryObject<EntityType<RoyalBulletEntity>> ROYALBULLET = ENTITY_TYPES.register("royalbullet",
            () -> EntityType.Builder.create(RoyalBulletEntity::new, EntityClassification.MONSTER)
                    .size(0.8f, 0.8f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "royalbullet").toString()));

    public static final RegistryObject<EntityType<CallerEntity>> CALLER = ENTITY_TYPES.register("caller",
            () -> EntityType.Builder.create(CallerEntity::new, EntityClassification.MONSTER)
                    .size(1.0f, 2.0f)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "caller").toString()));

    public static final RegistryObject<EntityType<SoulFireballEntity>> SOUL_FIREBALL = ENTITY_TYPES.register("soulfireball",
            () -> EntityType.Builder.<SoulFireballEntity>create(SoulFireballEntity::new, EntityClassification.MISC)
                    .size(1.0f,1.0f)
                    .trackingRange(4)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "soulfireball").toString()));

    public static final RegistryObject<EntityType<EtherealPunchEntity>> ETHEREAL_PUNCH = ENTITY_TYPES.register("etherealpunch",
            () -> EntityType.Builder.<EtherealPunchEntity>create(EtherealPunchEntity::new, EntityClassification.MISC)
                    .size(1.0f,1.0f)
                    .trackingRange(4)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "etherealpunch").toString()));

    public static final RegistryObject<EntityType<WarpedSpearEntity>> WARPED_SPEAR = ENTITY_TYPES.register("warped_spear",
            () -> EntityType.Builder.<WarpedSpearEntity>create(WarpedSpearEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .trackingRange(4)
                    .updateInterval(20)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "warped_spear").toString()));

    public static final RegistryObject<EntityType<WitchBombEntity>> WITCHBOMB = ENTITY_TYPES.register("witchbomb",
            () -> EntityType.Builder.<WitchBombEntity>create(WitchBombEntity::new, EntityClassification.MISC)
                    .size(0.25f,0.25f)
                    .trackingRange(4)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "witchbomb").toString()));

    public static final RegistryObject<EntityType<SlowBombEntity>> SLOWBOMB = ENTITY_TYPES.register("slowbomb",
            () -> EntityType.Builder.<SlowBombEntity>create(SlowBombEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(0.98F, 0.98F)
                    .trackingRange(10)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "slowbomb").toString()));

    public static final RegistryObject<EntityType<NetherBallEntity>> NETHERBALL = ENTITY_TYPES.register("scorchball",
            () -> EntityType.Builder.<NetherBallEntity>create(NetherBallEntity::new, EntityClassification.MISC)
                    .size(1.0f,1.0f)
                    .trackingRange(4)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "scorchball").toString()));

    public static final RegistryObject<EntityType<RedemptorEntity>> REDEMPTOR = ENTITY_TYPES.register("redemptor",
            () -> EntityType.Builder.create(RedemptorEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "redemptor").toString()));

    public static final RegistryObject<EntityType<ProtectorEntity>> PROTECTOR = ENTITY_TYPES.register("protector",
            () -> EntityType.Builder.create(ProtectorEntity::new, EntityClassification.MONSTER)
                    .func_225435_d()
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "protector").toString()));

    public static final RegistryObject<EntityType<BrewerEntity>> BREWER = ENTITY_TYPES.register("brewer",
            () -> EntityType.Builder.create(BrewerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "brewer").toString()));

    public static final RegistryObject<EntityType<HexerEntity>> HEXER = ENTITY_TYPES.register("hexer",
            () -> EntityType.Builder.create(HexerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "hexer").toString()));

    public static final RegistryObject<EntityType<MirageEntity>> MIRAGE = ENTITY_TYPES.register("mirage",
            () -> EntityType.Builder.create(MirageEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 0.85F)
                    .trackingRange(10)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "mirage").toString()));

    public static final RegistryObject<EntityType<SavagerEntity>> SAVAGER = ENTITY_TYPES.register("savager",
            () -> EntityType.Builder.create(SavagerEntity::new, EntityClassification.MONSTER)
                    .size(1.95F, 2.6F)
                    .trackingRange(10)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "savager").toString()));

    public static final RegistryObject<EntityType<NeophyteEntity>> NEOPHYTE = ENTITY_TYPES.register("neophyte",
            () -> EntityType.Builder.create(NeophyteEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "neophyte").toString()));

    public static final RegistryObject<EntityType<AcolyteEntity>> ACOLYTE = ENTITY_TYPES.register("acolyte",
            () -> EntityType.Builder.create(AcolyteEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "acolyte").toString()));

    public static final RegistryObject<EntityType<ChannellerEntity>> CHANNELLER = ENTITY_TYPES.register("channeller",
            () -> EntityType.Builder.create(ChannellerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "channeller").toString()));

    public static final RegistryObject<EntityType<FanaticEntity>> FANATIC = ENTITY_TYPES.register("fanatic",
            () -> EntityType.Builder.create(FanaticEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "fanatic").toString()));

    public static final RegistryObject<EntityType<ZealotEntity>> ZEALOT = ENTITY_TYPES.register("zealot",
            () -> EntityType.Builder.create(ZealotEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "zealot").toString()));

    public static final RegistryObject<EntityType<ApostleEntity>> APOSTLE = ENTITY_TYPES.register("apostle",
            () -> EntityType.Builder.create(ApostleEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "apostle").toString()));

    public static final RegistryObject<EntityType<ZombieVillagerMinionEntity>> ZOMBIE_VILLAGER_MINION = ENTITY_TYPES.register("zombievillagerminion",
            () -> EntityType.Builder.create(ZombieVillagerMinionEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "zombievillagerminion").toString()));

    public static final RegistryObject<EntityType<MutatedCowEntity>> MUTATED_COW = ENTITY_TYPES.register("mutatedcow",
            () -> EntityType.Builder.create(MutatedCowEntity::new, EntityClassification.MONSTER)
                    .size(0.9F, 1.95F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "mutatedcow").toString()));

    public static final RegistryObject<EntityType<MutatedChickenEntity>> MUTATED_CHICKEN = ENTITY_TYPES.register("mutatedchicken",
            () -> EntityType.Builder.create(MutatedChickenEntity::new, EntityClassification.MONSTER)
                    .size(0.5F, 1.0F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "mutatedchicken").toString()));

    public static final RegistryObject<EntityType<MutatedSheepEntity>> MUTATED_SHEEP = ENTITY_TYPES.register("mutatedsheep",
            () -> EntityType.Builder.create(MutatedSheepEntity::new, EntityClassification.MONSTER)
                    .size(0.9F, 1.95F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "mutatedsheep").toString()));

    public static final RegistryObject<EntityType<MutatedPigEntity>> MUTATED_PIG = ENTITY_TYPES.register("mutatedpig",
            () -> EntityType.Builder.create(MutatedPigEntity::new, EntityClassification.MONSTER)
                    .size(0.9F, 1.55F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "mutatedpig").toString()));

    public static final RegistryObject<EntityType<ParasiteEntity>> PARASITE = ENTITY_TYPES.register("parasite",
            () -> EntityType.Builder.create(ParasiteEntity::new, EntityClassification.MONSTER)
                    .size(0.4F, 0.3F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "parasite").toString()));

    public static final RegistryObject<EntityType<FriendlyVexEntity>> FRIENDLY_VEX = ENTITY_TYPES.register("friendly_vex",
            () -> EntityType.Builder.create(FriendlyVexEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.4F, 0.8F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "friendly_vex").toString()));

    public static final RegistryObject<EntityType<FriendlyScorchEntity>> FRIENDLY_SCORCH = ENTITY_TYPES.register("friendly_scorch",
            () -> EntityType.Builder.create(FriendlyScorchEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.4F, 0.8F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "friendly_scorch").toString()));

    public static final RegistryObject<EntityType<ZombieMinionEntity>> ZOMBIE_MINION = ENTITY_TYPES.register("zombie_minion",
            () -> EntityType.Builder.create(ZombieMinionEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "zombie_minion").toString()));

    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION = ENTITY_TYPES.register("skeleton_minion",
            () -> EntityType.Builder.create(SkeletonMinionEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "skeleton_minion").toString()));

    public static final RegistryObject<EntityType<SpiderlingMinionEntity>> SPIDERLING_MINION = ENTITY_TYPES.register("spiderling_minion",
            () -> EntityType.Builder.create(SpiderlingMinionEntity::new, EntityClassification.MONSTER)
                    .size(0.4F, 0.2F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "spiderling_minion").toString()));

    public static final RegistryObject<EntityType<VizierEntity>> VIZIER = ENTITY_TYPES.register("vizier",
            () -> EntityType.Builder.create(VizierEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "vizier").toString()));

    public static final RegistryObject<EntityType<IrkEntity>> IRK = ENTITY_TYPES.register("irk",
            () -> EntityType.Builder.create(IrkEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.4F, 0.8F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "irk").toString()));

    public static final RegistryObject<EntityType<ScorchEntity>> SCORCH = ENTITY_TYPES.register("scorch",
            () -> EntityType.Builder.create(ScorchEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.4F, 0.8F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "scorch").toString()));

    public static final RegistryObject<EntityType<NethernalEntity>> NETHERNAL = ENTITY_TYPES.register("nethernal",
            () -> EntityType.Builder.create(NethernalEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(1.4F, 2.7F)
                    .trackingRange(10)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "netheruin").toString()));

    public static final RegistryObject<EntityType<PenanceEntity>> PENANCE = ENTITY_TYPES.register("penance",
            () -> EntityType.Builder.create(PenanceEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "penance").toString()));

    public static final RegistryObject<EntityType<FakeSeatEntity>> FAKESEAT = ENTITY_TYPES.register("fakeseat",
            () -> EntityType.Builder.create(FakeSeatEntity::new, EntityClassification.MISC)
                    .size(0.1F, 0.1F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "fakeseat").toString()));

    public static final RegistryObject<EntityType<LightningTrapEntity>> LIGHTNINGTRAP = ENTITY_TYPES.register("lightningtrap",
            () -> EntityType.Builder.<LightningTrapEntity>create(LightningTrapEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(2.0F, 0.5F)
                    .trackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "lightningtrap").toString()));

}