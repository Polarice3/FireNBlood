package com.Polarice3.FireNBlood.entities.neutral.protectors;

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

public abstract class SpellcastingProtectorEntity extends AbstractProtectorEntity{
    private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(SpellcastingProtectorEntity.class, DataSerializers.BYTE);
    protected int spellTicks;
    private SpellcastingProtectorEntity.SpellType activeSpell = SpellcastingProtectorEntity.SpellType.NONE;

    protected SpellcastingProtectorEntity(EntityType<? extends SpellcastingProtectorEntity> type, World worldIn) {
        super(type, worldIn);
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
    public AbstractProtectorEntity.ArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return AbstractProtectorEntity.ArmPose.SPELLCASTING;
        } else {
            return AbstractProtectorEntity.ArmPose.CROSSED;
        }
    }

    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpellType(SpellcastingProtectorEntity.SpellType spellType) {
        this.activeSpell = spellType;
        this.dataManager.set(SPELL, (byte)spellType.id);
    }

    protected SpellcastingProtectorEntity.SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : SpellcastingProtectorEntity.SpellType.getFromId(this.dataManager.get(SPELL));
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
            SpellcastingProtectorEntity.SpellType spellcastingprotectorentity$spelltype = this.getSpellType();
            double d0 = spellcastingprotectorentity$spelltype.particleSpeed[0];
            double d1 = spellcastingprotectorentity$spelltype.particleSpeed[1];
            double d2 = spellcastingprotectorentity$spelltype.particleSpeed[2];
            float f = this.renderYawOffset * ((float)Math.PI / 180F) + MathHelper.cos((float)this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX() + (double)f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double)f2 * 0.6D, d0, d1, d2);
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX() - (double)f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double)f2 * 0.6D, d0, d1, d2);
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
            return SpellcastingProtectorEntity.this.getSpellTicks() > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            SpellcastingProtectorEntity.this.navigator.clearPath();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            SpellcastingProtectorEntity.this.setSpellType(SpellcastingProtectorEntity.SpellType.NONE);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (SpellcastingProtectorEntity.this.getAttackTarget() != null) {
                SpellcastingProtectorEntity.this.getLookController().setLookPositionWithEntity(SpellcastingProtectorEntity.this.getAttackTarget(), (float)SpellcastingProtectorEntity.this.getHorizontalFaceSpeed(), (float)SpellcastingProtectorEntity.this.getVerticalFaceSpeed());
            }

        }
    }

    public static enum SpellType {
        NONE(0, 0.0D, 0.0D, 0.0D),
        SUMMON_HELP(1, 0.7D, 0.7D, 0.8D),
        SPIKES(2, 0.4D, 0.3D, 0.35D),
        FLAME(3, 0.7D, 0.5D, 0.2D),
        WOLOLO(4, 0.7D, 0.5D, 0.2D);

        private final int id;
        private final double[] particleSpeed;

        private SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
            this.id = idIn;
            this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
        }

        public static SpellcastingProtectorEntity.SpellType getFromId(int idIn) {
            for(SpellcastingProtectorEntity.SpellType spellcastingprotectorentity$spelltype : values()) {
                if (idIn == spellcastingprotectorentity$spelltype.id) {
                    return spellcastingprotectorentity$spelltype;
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
            LivingEntity livingentity = SpellcastingProtectorEntity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive() && !SpellcastingProtectorEntity.this.isDying()) {
                if (SpellcastingProtectorEntity.this.isSpellcasting()) {
                    return false;
                } else {
                    return SpellcastingProtectorEntity.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = SpellcastingProtectorEntity.this.getAttackTarget();
            return livingentity != null && !SpellcastingProtectorEntity.this.isDying() && livingentity.isAlive() && this.spellWarmup > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            SpellcastingProtectorEntity.this.spellTicks = this.getCastingTime();
            this.spellCooldown = SpellcastingProtectorEntity.this.ticksExisted + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                SpellcastingProtectorEntity.this.playSound(soundevent, 1.0F, 1.0F);
            }

            SpellcastingProtectorEntity.this.setSpellType(this.getSpellType());
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                SpellcastingProtectorEntity.this.playSound(SpellcastingProtectorEntity.this.getSpellSound(), 1.0F, 1.0F);
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

        protected abstract SpellcastingProtectorEntity.SpellType getSpellType();
    }

}
