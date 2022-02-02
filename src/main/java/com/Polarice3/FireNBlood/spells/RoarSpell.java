package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class RoarSpell extends Spells {
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;

    public int SoulCost() {
        return FNBConfig.RoarCost.get();
    }

    public int CastDuration() {
        return FNBConfig.RoarDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
    }

    public ItemStack WandResult(World worldIn, LivingEntity entityLiving) {
        for(Entity entity : worldIn.getEntitiesWithinAABB(LivingEntity.class, entityLiving.getBoundingBox().grow(8.0D), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(entityLiving), 8.0F);
                this.launch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.rand.nextGaussian() * 0.2D;
            double d1 = worldIn.rand.nextGaussian() * 0.2D;
            double d2 = worldIn.rand.nextGaussian() * 0.2D;
            entityLiving.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        return null;
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        for(Entity entity : worldIn.getEntitiesWithinAABB(LivingEntity.class, entityLiving.getBoundingBox().grow(16.0D), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(entityLiving), 16.0F);
                this.superlaunch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.rand.nextGaussian() * 0.2D;
            double d1 = worldIn.rand.nextGaussian() * 0.2D;
            double d2 = worldIn.rand.nextGaussian() * 0.2D;
            entityLiving.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            double d = worldIn.rand.nextGaussian() * 0.2D;
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), d, d, d);
        }
    }

    private void superlaunch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getPosX() - livingEntity.getPosX();
        double d1 = p_213688_1_.getPosZ() - livingEntity.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 8.0D, 0.2D, d1 / d2 * 8.0D);
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getPosX() - livingEntity.getPosX();
        double d1 = p_213688_1_.getPosZ() - livingEntity.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }
}
