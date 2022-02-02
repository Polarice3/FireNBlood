package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.SpellcastingTaillessEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class SpellcastingCultistEntity extends AbstractCultistEntity{
    private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(SpellcastingCultistEntity.class, DataSerializers.BYTE);
    protected int spellTicks;
    private SpellcastingCultistEntity.SpellType activeSpell = SpellcastingCultistEntity.SpellType.NONE;

    protected SpellcastingCultistEntity(EntityType<? extends SpellcastingCultistEntity> type, World p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SPELL, (byte)0);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.spellTicks = compound.getInt("SpellTicks");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("SpellTicks", this.spellTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractCultistEntity.ArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return ArmPose.BOW_AND_ARROW;
        } else {
            return AbstractCultistEntity.ArmPose.NEUTRAL;
        }
    }

    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpellType(SpellcastingCultistEntity.SpellType spellType) {
        this.activeSpell = spellType;
        this.dataManager.set(SPELL, (byte)spellType.id);
    }

    protected SpellcastingCultistEntity.SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : SpellcastingCultistEntity.SpellType.getFromId(this.dataManager.get(SPELL));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.isSpellcasting()) {
            SpellcastingCultistEntity.SpellType SpellcastingCultistEntity$spelltype = this.getSpellType();
            double d0 = SpellcastingCultistEntity$spelltype.particleSpeed[0];
            double d1 = SpellcastingCultistEntity$spelltype.particleSpeed[1];
            double d2 = SpellcastingCultistEntity$spelltype.particleSpeed[2];
            for (int i = 0; i < this.world.rand.nextInt(35) + 10; ++i) {
                double d = this.world.rand.nextGaussian() * 0.2D;
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX(), this.getPosYEye(), this.getPosZ(), d0, d1, d2);
            }
        }

    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected abstract SoundEvent getSpellSound();

    public class CastingASpellGoal extends Goal {
        public CastingASpellGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return SpellcastingCultistEntity.this.getSpellTicks() > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            SpellcastingCultistEntity.this.navigator.clearPath();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            SpellcastingCultistEntity.this.setSpellType(SpellcastingCultistEntity.SpellType.NONE);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (SpellcastingCultistEntity.this.getAttackTarget() != null) {
                SpellcastingCultistEntity.this.getLookController().setLookPositionWithEntity(SpellcastingCultistEntity.this.getAttackTarget(), (float) SpellcastingCultistEntity.this.getHorizontalFaceSpeed(), (float) SpellcastingCultistEntity.this.getVerticalFaceSpeed());
            }

        }
    }

    public enum SpellType {
        NONE(0, 0.0D, 0.0D, 0.0D),
        FIRE(1, 0.1D, 0.1D, 0.2D),
        ZOMBIE(3, 0.7D, 0.5D, 0.2D),
        CRIPPLE(4, 0.7D, 0.5D, 0.2D);

        private final int id;
        private final double[] particleSpeed;

        SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
            this.id = idIn;
            this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
        }

        public static SpellcastingCultistEntity.SpellType getFromId(int idIn) {
            for(SpellcastingCultistEntity.SpellType SpellcastingCultistEntity$spelltype : values()) {
                if (idIn == SpellcastingCultistEntity$spelltype.id) {
                    return SpellcastingCultistEntity$spelltype;
                }
            }

            return NONE;
        }
    }

    public abstract class UseSpellGoal extends Goal {
        protected int spellWarmup;
        protected int spellCooldown;

        protected UseSpellGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = SpellcastingCultistEntity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (SpellcastingCultistEntity.this.isSpellcasting()) {
                    return false;
                } else {
                    return SpellcastingCultistEntity.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = SpellcastingCultistEntity.this.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.spellWarmup > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            SpellcastingCultistEntity.this.spellTicks = this.getCastingTime();
            this.spellCooldown = SpellcastingCultistEntity.this.ticksExisted + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                SpellcastingCultistEntity.this.playSound(soundevent, 1.0F, 1.0F);
            }

            SpellcastingCultistEntity.this.setSpellType(this.getSpellType());
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                SpellcastingCultistEntity.this.playSound(SpellcastingCultistEntity.this.getSpellSound(), 1.0F, 1.0F);
            }

        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract SpellcastingCultistEntity.SpellType getSpellType();
    }
}
