package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.blocks.MutateTotemBlock;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.particles.ModParticleTypes;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MutateTotemTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    private int levels;
    @Nullable
    private LivingEntity target;
    @Nullable
    private UUID targetUuid;

    public MutateTotemTileEntity() {
        this(ModTileEntityType.MUTATE_TOTEM.get());
    }

    public MutateTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return MutateTotemTileEntity.this.level;
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.hasUUID("Target")) {
            this.targetUuid = nbt.getUUID("Target");
        } else {
            this.targetUuid = null;
        }

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.target != null) {
            compound.putUUID("Target", this.target.getUUID());
        }

        return compound;
    }

    private void updateClientTarget() {
        this.target = this.findExistingTarget();
    }

    @Nullable
    private LivingEntity findExistingTarget() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        assert this.level != null;
        List<AnimalEntity> list = this.level.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D));
        if (list.size() > 0) {
            AnimalEntity animal = list.get(this.level.random.nextInt(list.size()));
            if (animal instanceof PigEntity || animal instanceof SheepEntity || animal instanceof CowEntity || animal instanceof ChickenEntity || animal instanceof RabbitEntity){
                return animal;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide()) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            int j1 = this.levels;
            this.checkBeaconLevel(i, j, k);
            if (j1 >= 3) {
                this.updateClientTarget();
                this.SpawnParticles();
                long t = this.level.getGameTime();
                if (t % 40L == 0L && this.target != null){
                    this.activated = 20;
                    this.mutateMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(MutateTotemBlock.POWERED, true), 3);
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(MutateTotemBlock.POWERED, false), 3);
                }
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(MutateTotemBlock.POWERED, false), 3);
            }
        }
    }

    private void checkBeaconLevel(int beaconXIn, int beaconYIn, int beaconZIn) {
        this.levels = 0;

        for(int i = 1; i <= 3; this.levels = i++) {
            int j = beaconYIn - i;
            if (j < 0) {
                break;
            }

            assert this.level != null;
            boolean flag = this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn)).is(RegistryHandler.CURSED_TOTEM_BLOCK.get());

            if (!flag) {
                break;
            }
        }

    }

    public void playSound(SoundEvent sound) {
        this.level.playSound(null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void setRemoved() {
        this.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE);
        super.setRemoved();
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 3, this.getUpdateTag());
    }

    private void SpawnParticles(){
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.activated != 0) {
                for (int p = 0; p < 4; ++p) {
                    MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.7, 0.7, 0.7);
                    MINECRAFT.level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                }
            } else {
                if (t % 40L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.45, 0.45, 0.45);
                    }
                }
            }
        }
    }

    public void mutateMobs(){
        this.playSound(SoundEvents.ILLUSIONER_CAST_SPELL);
        if (this.target instanceof CowEntity){
            MutatedCowEntity mutatedCowEntity = new MutatedCowEntity(ModEntityType.MUTATED_COW.get(), this.level);
            mutatedCowEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
            mutatedCowEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(mutatedCowEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedCowEntity.setCustomName(target.getCustomName());
                mutatedCowEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedCowEntity.setPersistenceRequired();
            this.level.addFreshEntity(mutatedCowEntity);
            target.remove();
        } else if (this.target instanceof ChickenEntity){
            MutatedChickenEntity mutatedChickenEntity = new MutatedChickenEntity(ModEntityType.MUTATED_CHICKEN.get(), this.level);
            mutatedChickenEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
            mutatedChickenEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(mutatedChickenEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedChickenEntity.setCustomName(target.getCustomName());
                mutatedChickenEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedChickenEntity.setPersistenceRequired();
            this.level.addFreshEntity(mutatedChickenEntity);
            target.remove();
        } else if (this.target instanceof SheepEntity){
            MutatedSheepEntity mutatedSheepEntity = new MutatedSheepEntity(ModEntityType.MUTATED_SHEEP.get(), this.level);
            mutatedSheepEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
            mutatedSheepEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(mutatedSheepEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedSheepEntity.setCustomName(target.getCustomName());
                mutatedSheepEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedSheepEntity.setPersistenceRequired();
            this.level.addFreshEntity(mutatedSheepEntity);
            target.remove();
        } else if (this.target instanceof PigEntity){
            MutatedPigEntity mutatedPigEntity = new MutatedPigEntity(ModEntityType.MUTATED_PIG.get(), this.level);
            mutatedPigEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
            mutatedPigEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(mutatedPigEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedPigEntity.setCustomName(target.getCustomName());
                mutatedPigEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedPigEntity.setPersistenceRequired();
            this.level.addFreshEntity(mutatedPigEntity);
            target.remove();
        } else if (this.target instanceof RabbitEntity){
            RabbitEntity rabbit = (RabbitEntity) this.target;
            MutatedRabbitEntity mutatedRabbitEntity = new MutatedRabbitEntity(ModEntityType.MUTATED_RABBIT.get(), this.level);
            mutatedRabbitEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
            mutatedRabbitEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(mutatedRabbitEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedRabbitEntity.setCustomName(target.getCustomName());
                mutatedRabbitEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            if (rabbit.getRabbitType() != 99) {
                mutatedRabbitEntity.setRabbitType(rabbit.getRabbitType());
            } else {
                mutatedRabbitEntity.setRabbitType(1);
            }
            mutatedRabbitEntity.setPersistenceRequired();
            this.level.addFreshEntity(mutatedRabbitEntity);
            target.remove();
        }
    }
}
