package com.Polarice3.FireNBlood.entities.hostile;

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

    public abstract class SpellcastingTaillessEntity extends AbstractTaillessEntity {
        private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(SpellcastingTaillessEntity.class, DataSerializers.BYTE);
        protected int spellTicks;
        protected int prayingTicks;
        private SpellcastingTaillessEntity.SpellType activeSpell = SpellcastingTaillessEntity.SpellType.NONE;

        protected SpellcastingTaillessEntity(EntityType<? extends SpellcastingTaillessEntity> type, World p_i48551_2_) {
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
            this.prayingTicks = compound.getInt("PrayingTicks");
        }

        public void writeAdditional(CompoundNBT compound) {
            super.writeAdditional(compound);
            compound.putInt("SpellTicks", this.spellTicks);
            compound.putInt("PrayingTicks", this.prayingTicks);
        }

        @OnlyIn(Dist.CLIENT)
        public AbstractTaillessEntity.ArmPose getArmPose() {
            if (this.isSpellcasting() || this.isPraying()) {
                return AbstractTaillessEntity.ArmPose.SPELLCASTING;
            } else {
                return AbstractTaillessEntity.ArmPose.NEUTRAL;
            }
        }

        public boolean isPraying(){
            return this.prayingTicks > 0;
        }

        public boolean isSpellcasting() {
            if (this.world.isRemote) {
                return this.dataManager.get(SPELL) > 0;
            } else {
                return this.spellTicks > 0;
            }
        }

        public void setSpellType(SpellcastingTaillessEntity.SpellType spellType) {
            this.activeSpell = spellType;
            this.dataManager.set(SPELL, (byte)spellType.id);
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return !this.world.isRemote ? this.activeSpell : SpellcastingTaillessEntity.SpellType.getFromId(this.dataManager.get(SPELL));
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
                SpellcastingTaillessEntity.SpellType SpellcastingTaillessEntity$spelltype = this.getSpellType();
                double d0 = SpellcastingTaillessEntity$spelltype.particleSpeed[0];
                double d1 = SpellcastingTaillessEntity$spelltype.particleSpeed[1];
                double d2 = SpellcastingTaillessEntity$spelltype.particleSpeed[2];
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
                return SpellcastingTaillessEntity.this.getSpellTicks() > 0;
            }

            /**
             * Execute a one shot task or start executing a continuous task
             */
            public void startExecuting() {
                super.startExecuting();
                SpellcastingTaillessEntity.this.navigator.clearPath();
            }

            /**
             * Reset the task's internal state. Called when this task is interrupted by another one
             */
            public void resetTask() {
                super.resetTask();
                SpellcastingTaillessEntity.this.setSpellType(SpellcastingTaillessEntity.SpellType.NONE);
            }

            /**
             * Keep ticking a continuous task that has already been started
             */
            public void tick() {
                if (SpellcastingTaillessEntity.this.getAttackTarget() != null) {
                    SpellcastingTaillessEntity.this.getLookController().setLookPositionWithEntity(SpellcastingTaillessEntity.this.getAttackTarget(), (float) SpellcastingTaillessEntity.this.getHorizontalFaceSpeed(), (float) SpellcastingTaillessEntity.this.getVerticalFaceSpeed());
                }

            }
        }

        public enum SpellType {
            NONE(0, 0.0D, 0.0D, 0.0D),
            SLOWNESS(1, 0.1D, 0.1D, 0.2D),
            REGEN(3, 0.7D, 0.5D, 0.2D),
            TEMPT(4, 0.7D, 0.5D, 0.2D),
            LIGHTNING(5, 0.7D, 0.5D, 0.2D),
            SUMMON_BULLET(6, 0.7D, 0.5D, 0.2D),
            TELEPORT(7, 0.7D, 0.5D, 0.2D),
            BARRAGE(8, 0.7D, 0.5D, 0.2D),
            WITHER(9, 0.7D, 0.5D, 0.2D);

            private final int id;
            private final double[] particleSpeed;

            SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
                this.id = idIn;
                this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
            }

            public static SpellcastingTaillessEntity.SpellType getFromId(int idIn) {
                for(SpellcastingTaillessEntity.SpellType SpellcastingTaillessEntity$spelltype : values()) {
                    if (idIn == SpellcastingTaillessEntity$spelltype.id) {
                        return SpellcastingTaillessEntity$spelltype;
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
                LivingEntity livingentity = SpellcastingTaillessEntity.this.getAttackTarget();
                if (livingentity != null && livingentity.isAlive()) {
                    if (SpellcastingTaillessEntity.this.isSpellcasting()) {
                        return false;
                    } else {
                        return SpellcastingTaillessEntity.this.ticksExisted >= this.spellCooldown;
                    }
                } else {
                    return false;
                }
            }

            /**
             * Returns whether an in-progress EntityAIBase should continue executing
             */
            public boolean shouldContinueExecuting() {
                LivingEntity livingentity = SpellcastingTaillessEntity.this.getAttackTarget();
                return livingentity != null && livingentity.isAlive() && this.spellWarmup > 0;
            }

            /**
             * Execute a one shot task or start executing a continuous task
             */
            public void startExecuting() {
                this.spellWarmup = this.getCastWarmupTime();
                SpellcastingTaillessEntity.this.spellTicks = this.getCastingTime();
                this.spellCooldown = SpellcastingTaillessEntity.this.ticksExisted + this.getCastingInterval();
                SoundEvent soundevent = this.getSpellPrepareSound();
                if (soundevent != null) {
                    SpellcastingTaillessEntity.this.playSound(soundevent, 1.0F, 1.0F);
                }

                SpellcastingTaillessEntity.this.setSpellType(this.getSpellType());
            }

            /**
             * Keep ticking a continuous task that has already been started
             */
            public void tick() {
                --this.spellWarmup;
                if (this.spellWarmup == 0) {
                    this.castSpell();
                    SpellcastingTaillessEntity.this.playSound(SpellcastingTaillessEntity.this.getSpellSound(), 1.0F, 1.0F);
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

            protected abstract SpellcastingTaillessEntity.SpellType getSpellType();
        }
    }
