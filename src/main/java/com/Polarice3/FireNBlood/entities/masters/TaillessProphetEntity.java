package com.Polarice3.FireNBlood.entities.masters;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.RoyalBulletEntity;
import com.Polarice3.FireNBlood.entities.hostile.ServantTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.SpellcastingTaillessEntity;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)

public class TaillessProphetEntity extends SpellcastingTaillessEntity implements IChargeableMob {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TaillessProphetEntity);
    };
    private AbstractTaillessEntity AllyTarget;
    protected static final DataParameter<Byte> PROPHET_FLAGS = EntityDataManager.createKey(TaillessProphetEntity.class, DataSerializers.BYTE);
    public int Launch;

    public TaillessProphetEntity(EntityType<? extends SpellcastingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 150.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new TaillessProphetEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new TaillessProphetEntity.JumpSpellGoal());
        this.goalSelector.addGoal(4, new TaillessProphetEntity.BarrageSpellGoal());
        this.goalSelector.addGoal(5, new TaillessProphetEntity.RegenSpellGoal());
        this.goalSelector.addGoal(6, new TaillessProphetEntity.SummonSpellGoal());
        this.goalSelector.addGoal(7, new TaillessProphetEntity.WitherSpellGoal());
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());

    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 50 + this.world.rand.nextInt(50);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_POLAR_BEAR_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.25F, 1.0F);
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    private final EntityPredicate ally = (new EntityPredicate().setDistance(32.0D));

    @Nullable
    private AbstractTaillessEntity getAllyTarget() {
        return this.AllyTarget;
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected boolean SecondPhase(){
        return TaillessProphetEntity.this.getHealth() <= TaillessProphetEntity.this.getMaxHealth() / 2.0F;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(PROPHET_FLAGS, (byte)0);
    }

    private boolean getProphetFlag(int mask) {
        int i = this.dataManager.get(PROPHET_FLAGS);
        return (i & mask) != 0;
    }

    private void setProphetFlag(int mask, boolean value) {
        int i = this.dataManager.get(PROPHET_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(PROPHET_FLAGS, (byte)(i & 255));
    }

    public boolean isLaunching(){
        return this.getProphetFlag(1);
    }

    public void setLaunching(boolean launching){
        this.setProphetFlag(1,launching);
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() != Effects.WITHER && super.isPotionApplicable(potioneffectIn);
    }

    public boolean attackEntityFrom(DamageSource source, float amount){
        List<ServantTaillessEntity> list = TaillessProphetEntity.this.world.getTargettableEntitiesWithinAABB(ServantTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        if (list.isEmpty()) {
            boolean flag = super.attackEntityFrom(source, amount);
            if (TaillessProphetEntity.this.SecondPhase()) {
                this.teleportRandomly();
            }
            return flag;
        } else {
            return false;
        }
    }

    public boolean isCharged(){
        List<ServantTaillessEntity> list = TaillessProphetEntity.this.world.getTargettableEntitiesWithinAABB(ServantTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        }  else if (entityIn instanceof AbstractPiglinEntity){
            return this.isOnSameTeam(entityIn);
        }  else {
            return false;
        }
    }

    protected void updateAITasks(){
        if (SecondPhase()) {
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }
        }
        super.updateAITasks();
    }

    protected boolean teleportRandomly() {
        if (!this.world.isRemote() && this.isAlive()) {
            double d0 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getPosY() + (double)(this.rand.nextInt(32) - 16);
            double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 32.0D;
            return this.teleportTo(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while(blockpos$mutable.getY() > 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.world.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMovement();
        boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.world.playSound((PlayerEntity)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(RegistryHandler.PADRE_EFFIGY.get());
        if (itementity != null) {
            itementity.setNoDespawn();
        }

    }

    public void tick(){
        super.tick();
        if (this.isLaunching()) {
            ++this.Launch;
            for (Entity entity : TaillessProphetEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, TaillessProphetEntity.this.getBoundingBox().grow(8.0D, 0.0D, 8.0D), field_213690_b)) {
                if (!(entity instanceof AbstractTaillessEntity)) {
                    entity.addVelocity(0.0D, 4.0D, 0.0D);
                    entity.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_SHOOT, 1.0F, 2.0F);
                }
            }
        }
        if (this.Launch >= 1){
            this.Launch = 0;
            this.setLaunching(false);
        }
    }

    class CastingSpellGoal extends SpellcastingTaillessEntity.CastingASpellGoal{
        private CastingSpellGoal() {
        }

        public void tick() {
            if (TaillessProphetEntity.this.getAttackTarget() != null) {
                TaillessProphetEntity.this.getLookController().setLookPositionWithEntity(TaillessProphetEntity.this.getAttackTarget(), (float)TaillessProphetEntity.this.getHorizontalFaceSpeed(), (float)TaillessProphetEntity.this.getVerticalFaceSpeed());
            }
            else if (TaillessProphetEntity.this.getAllyTarget() != null) {
                TaillessProphetEntity.this.getLookController().setLookPositionWithEntity(TaillessProphetEntity.this.getAllyTarget(), (float)TaillessProphetEntity.this.getHorizontalFaceSpeed(), (float)TaillessProphetEntity.this.getVerticalFaceSpeed());
            }
        }
    }

    class JumpSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        public boolean shouldExecute() {
            if (TaillessProphetEntity.this.getAttackTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            else return TaillessProphetEntity.this.getAttackTarget().isAlive();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 200;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessProphetEntity.this.getAttackTarget();
            TaillessProphetEntity.this.setLaunching(true);
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }
        }

    }

    class LightningSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        private LightningSpellGoal(){
        }

        public boolean shouldExecute() {
            if (TaillessProphetEntity.this.getAttackTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getAttackTarget().isAlive()){
                return true;
            }
            else {
                return false;
            }
        }
        protected int getCastWarmupTime() {
            if (TaillessProphetEntity.this.SecondPhase()){
                return 60;
            }
             else {return 120;}
        }

        protected int getCastingTime() {
            if (TaillessProphetEntity.this.SecondPhase()){
                return 60;
            }
            else {return 120;}
        }

        protected int getCastingInterval() {
            if (TaillessProphetEntity.this.SecondPhase()){
                return 200;
            }
            else {return 400;}
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessProphetEntity.this.getAttackTarget();
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPosition(livingentity.prevPosX,livingentity.prevPosY,livingentity.prevPosZ);
            world.addEntity(lightning);
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }
        }
    }

    class RegenSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final EntityPredicate ally = (new EntityPredicate().setDistance(32.0D).allowInvulnerable().setCustomPredicate((health) -> health.getHealth() <= 30.0F));

        public boolean shouldExecute() {
            if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessProphetEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            } else {
                List<AbstractTaillessEntity> list = TaillessProphetEntity.this.world.getTargettableEntitiesWithinAABB(AbstractTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 340;
        }

        public void castSpell() {
            for (AbstractTaillessEntity ally : TaillessProphetEntity.this.world.getEntitiesWithinAABB(AbstractTaillessEntity.class, TaillessProphetEntity.this.getBoundingBox().grow(16.0D), field_213690_b)) {
                ally.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1000, 1));
                ally.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1000, 1));
            }
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.REGEN;
        }
    }

    class SummonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate field_220843_e = (new EntityPredicate()).setDistance(32.0D).setLineOfSiteRequired().setUseInvisibilityCheck().allowInvulnerable().allowFriendlyFire();

        private SummonSpellGoal() {
        }

        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else {
                int i = TaillessProphetEntity.this.world.getTargettableEntitiesWithinAABB(RoyalBulletEntity.class, this.field_220843_e, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().grow(32.0D)).size();
                return TaillessProphetEntity.this.rand.nextInt(8) + 1 > i;
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            if (isCharged()){
                return 600;
            }
            else {
                return 500;
            }
        }

        protected void castSpell() {
            ServerWorld serverworld = (ServerWorld)TaillessProphetEntity.this.world;

            for(int i = 0; i < 5; ++i) {
                BlockPos blockpos = TaillessProphetEntity.this.getPosition().add(-2 + TaillessProphetEntity.this.rand.nextInt(5), 1, -2 + TaillessProphetEntity.this.rand.nextInt(5));
                RoyalBulletEntity bulletEntity = new RoyalBulletEntity(ModEntityType.ROYALBULLET.get(), world);
                bulletEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                bulletEntity.onInitialSpawn(serverworld, TaillessProphetEntity.this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                bulletEntity.setOwner(TaillessProphetEntity.this);
                bulletEntity.setBoundOrigin(blockpos);
                bulletEntity.setLimitedLife(20 * (30 + TaillessProphetEntity.this.rand.nextInt(90)));
                serverworld.func_242417_l(bulletEntity);
            }
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }

        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SUMMON_BULLET;
        }
    }

    class BarrageSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        private final EntityPredicate field_220843_e = (new EntityPredicate()).setDistance(32.0D).setLineOfSiteRequired().setUseInvisibilityCheck().allowInvulnerable().allowFriendlyFire();

        private BarrageSpellGoal() {
        }

        public boolean shouldExecute() {
            if (TaillessProphetEntity.this.getAttackTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getAttackTarget().isAlive() && TaillessProphetEntity.this.SecondPhase()){
                return true;
            }
            else {
                return false;
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 120;
        }

        protected void castSpell() {
            LivingEntity livingentity = TaillessProphetEntity.this.getAttackTarget();
            ServerWorld serverworld = (ServerWorld) TaillessProphetEntity.this.world;
            assert livingentity != null;
/*            double d0 = TaillessProphetEntity.this.getDistanceSq(livingentity);
            Vector3d vector3d = TaillessProphetEntity.this.getLook(1.0F);
            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
            double d2 = livingentity.getPosX() - (TaillessProphetEntity.this.getPosX() + vector3d.x * 4.0D);
            double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + TaillessProphetEntity.this.getPosYHeight(0.5D));
            double d4 = livingentity.getPosZ() - (TaillessProphetEntity.this.getPosZ() + vector3d.z * 4.0D);
            SoulFireballEntity soulfireballEntity = new SoulFireballEntity(world, TaillessProphetEntity.this, d2 + TaillessProphetEntity.this.getRNG().nextGaussian() * (double) f, d3 + TaillessProphetEntity.this.getRNG().nextGaussian() * (double) f, d4 + TaillessProphetEntity.this.getRNG().nextGaussian() * (double) f);
            soulfireballEntity.setPosition(TaillessProphetEntity.this.getPosX() + vector3d.x * 4.0D, TaillessProphetEntity.this.getPosYHeight(0.5D) + 0.5D, soulfireballEntity.getPosZ() + vector3d.z * 4.0D);
            world.addEntity(soulfireballEntity);*/
            for(int i = 0; i < 3; ++i) {
                SoulFireballEntity soulfireballEntity = new SoulFireballEntity(world, TaillessProphetEntity.this, 0, -900D, 0);
                soulfireballEntity.setPosition(livingentity.getPosX() + TaillessProphetEntity.this.rand.nextInt(5), livingentity.getPosY() + 32.0D, livingentity.getPosZ() + TaillessProphetEntity.this.rand.nextInt(5));
                world.addEntity(soulfireballEntity);
            }
            if (!TaillessProphetEntity.this.isSilent()) {
                TaillessProphetEntity.this.world.playEvent(null, 1016, TaillessProphetEntity.this.getPosition(), 0);
            }
            TaillessProphetEntity.this.teleportRandomly();
            }



        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.BARRAGE;
        }
    }

    class WitherSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private int wait;

        private WitherSpellGoal(){
        }

        public boolean shouldExecute() {
            if (TaillessProphetEntity.this.getAttackTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getAttackTarget().isAlive() && TaillessProphetEntity.this.SecondPhase()){
                return true;
            }
            else {
                return false;
            }
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 200;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.WITHER;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessProphetEntity.this.getAttackTarget();
            if (!TaillessProphetEntity.this.world.isRemote) {
                assert livingentity != null;
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(TaillessProphetEntity.this.world, livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                areaeffectcloudentity.setParticleData(ParticleTypes.SOUL);
                areaeffectcloudentity.setRadius(6.0F);
                areaeffectcloudentity.setDuration(600);
                areaeffectcloudentity.setRadiusPerTick((7.0F - areaeffectcloudentity.getRadius()) / (float)areaeffectcloudentity.getDuration());
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.WITHER, 90));
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.SLOWNESS, 90, 2));
                TaillessProphetEntity.this.world.addEntity(areaeffectcloudentity);
                TaillessProphetEntity.this.teleportRandomly();
            }
        }
    }


}
