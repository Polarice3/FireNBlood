package com.Polarice3.FireNBlood.entities.neutral;

import com.Polarice3.FireNBlood.entities.hostile.ParasiteEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MutatedEntity extends AnimalEntity {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(CreeperEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(CreeperEntity.class, DataSerializers.BOOLEAN);
    private int timeSinceIgnited;
    private int lastActiveTime;
    private int fuseTime = 20;

    protected MutatedEntity(EntityType<? extends MutatedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(IGNITED, false);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, true);
    }

    public int getMutatedState() {
        return this.dataManager.get(STATE);
    }

    public void setMutatedState(int state) {
        this.dataManager.set(STATE, state);
    }

    @OnlyIn(Dist.CLIENT)
    public float getMutatedFlashIntensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    public void tick() {
        if (this.isAlive()) {
            if (!this.hasIgnited()){
                int random = this.world.rand.nextInt(36000) + 12000;
                if (this.ticksExisted >= random){
                    this.ignite();
                }
            }
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setMutatedState(1);
            }

            int i = this.getMutatedState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_SKELETON_HORSE_DEATH, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.tick();
    }

    private void explode() {
        if (!this.world.isRemote) {
            this.dead = true;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 1.0F, Explosion.Mode.NONE);
            this.remove();
            for (int i = 0; i < 4 + this.world.rand.nextInt(4); ++i) {
                ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), world);
                parasiteEntity.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                parasiteEntity.setAttackAll(true);
                world.addEntity(parasiteEntity);
            }
        }

    }


    public void onDeath(DamageSource cause) {
        int random = this.world.rand.nextInt(8);
        if (random == 0){
            for (int i = 0; i < 2 + this.world.rand.nextInt(2); ++i) {
                ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), world);
                parasiteEntity.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                parasiteEntity.setAttackAll(true);
                world.addEntity(parasiteEntity);
            }
        }
        super.onDeath(cause);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }
}
