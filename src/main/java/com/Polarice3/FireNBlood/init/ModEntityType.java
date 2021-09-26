package com.Polarice3.FireNBlood.init;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.entities.hostile.*;
import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.entities.projectiles.EtherealPunchEntity;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.entities.projectiles.WarpedSpearEntity;
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
                    .func_233608_b_(20)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "warped_spear").toString()));

    public static final RegistryObject<EntityType<SlowBombEntity>> SLOWBOMB = ENTITY_TYPES.register("slowbomb",
            () -> EntityType.Builder.<SlowBombEntity>create(SlowBombEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(0.98F, 0.98F)
                    .trackingRange(10)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "slowbomb").toString()));

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

    public static final RegistryObject<EntityType<QuellEntity>> QUELL = ENTITY_TYPES.register("quell",
            () -> EntityType.Builder.create(QuellEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "quell").toString()));

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

    public static final RegistryObject<EntityType<PenanceEntity>> PENANCE = ENTITY_TYPES.register("penance",
            () -> EntityType.Builder.create(PenanceEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "penance").toString()));

    public static final RegistryObject<EntityType<FakeSeatEntity>> FAKESEAT = ENTITY_TYPES.register("fakeseat",
            () -> EntityType.Builder.create(FakeSeatEntity::new, EntityClassification.MISC)
                    .size(0.1F, 0.1F)
                    .build(new ResourceLocation(FireNBlood.MOD_ID, "fakeseat").toString()));

}