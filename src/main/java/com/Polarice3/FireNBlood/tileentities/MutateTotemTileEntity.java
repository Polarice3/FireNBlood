package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.blocks.FangTotemBlock;
import com.Polarice3.FireNBlood.blocks.MutateTotemBlock;
import com.Polarice3.FireNBlood.entities.hostile.ScorchEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
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
        return this.write(new CompoundNBT());
    }

    public World getWorld() {
        return MutateTotemTileEntity.this.world;
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (nbt.hasUniqueId("Target")) {
            this.targetUuid = nbt.getUniqueId("Target");
        } else {
            this.targetUuid = null;
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.target != null) {
            compound.putUniqueId("Target", this.target.getUniqueID());
        }

        return compound;
    }

    private void updateClientTarget() {
        this.target = this.findExistingTarget();
    }

    @Nullable
    private LivingEntity findExistingTarget() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        List<AnimalEntity> list = this.world.getEntitiesWithinAABB(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).grow(10.0D, 10.0D, 10.0D));
        if (list.size() > 0) {
            AnimalEntity animal = list.get(this.world.rand.nextInt(list.size()));
            if (animal instanceof PigEntity || animal instanceof SheepEntity || animal instanceof CowEntity || animal instanceof ChickenEntity){
                return animal;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public double ParticleSpeed(){
        long t = this.world.getGameTime();
        if (t % 40L == 0L && this.target != null){
            return 0.7D;
        } else {
            return 0.45D;
        }
    }

    @Override
    public void tick() {
        assert this.world != null;
        if (!this.world.isRemote()) {
            int i = this.pos.getX();
            int j = this.pos.getY();
            int k = this.pos.getZ();
            int j1 = this.levels;
            this.checkBeaconLevel(i, j, k);
            if (j1 >= 3) {
                this.updateClientTarget();
                this.SpawnParticles();
                long t = this.world.getGameTime();
                if (t % 40L == 0L && this.target != null){
                    this.activated = 20;
                    this.mutateMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MutateTotemBlock.POWERED, true), 3);
                } else {
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MutateTotemBlock.POWERED, false), 3);
                }
            } else {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MutateTotemBlock.POWERED, false), 3);
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

            assert this.world != null;
            boolean flag = this.world.getBlockState(new BlockPos(beaconXIn, j, beaconZIn)).matchesBlock(RegistryHandler.CURSED_TOTEM_BLOCK.get());

            if (!flag) {
                break;
            }
        }

    }

    public void playSound(SoundEvent sound) {
        this.world.playSound(null, this.pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void remove() {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE);
        super.remove();
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    private void SpawnParticles(){
        double d0 = pos.getX() + 0.5;
        double d1 = pos.getY();
        double d2 = pos.getZ() + 0.5;

        for (int p = 0; p < 4; ++p) {
            this.world.addParticle(ParticleTypes.FLAME, d0, d1, d2, this.ParticleSpeed(), this.ParticleSpeed(), this.ParticleSpeed());
        }
    }

    public void mutateMobs(){
        this.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL);
        if (this.target instanceof CowEntity){
            MutatedCowEntity mutatedCowEntity = new MutatedCowEntity(ModEntityType.MUTATED_COW.get(), this.world);
            mutatedCowEntity.setLocationAndAngles(target.getPosX(), target.getPosY(), target.getPosZ(), target.rotationYaw, target.rotationPitch);
            mutatedCowEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mutatedCowEntity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedCowEntity.setCustomName(target.getCustomName());
                mutatedCowEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedCowEntity.enablePersistence();
            this.world.addEntity(mutatedCowEntity);
            target.remove();
        } else if (this.target instanceof ChickenEntity){
            MutatedChickenEntity mutatedChickenEntity = new MutatedChickenEntity(ModEntityType.MUTATED_CHICKEN.get(), this.world);
            mutatedChickenEntity.setLocationAndAngles(target.getPosX(), target.getPosY(), target.getPosZ(), target.rotationYaw, target.rotationPitch);
            mutatedChickenEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mutatedChickenEntity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedChickenEntity.setCustomName(target.getCustomName());
                mutatedChickenEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedChickenEntity.enablePersistence();
            this.world.addEntity(mutatedChickenEntity);
            target.remove();
        } else if (this.target instanceof SheepEntity){
            MutatedSheepEntity mutatedSheepEntity = new MutatedSheepEntity(ModEntityType.MUTATED_SHEEP.get(), this.world);
            mutatedSheepEntity.setLocationAndAngles(target.getPosX(), target.getPosY(), target.getPosZ(), target.rotationYaw, target.rotationPitch);
            mutatedSheepEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mutatedSheepEntity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedSheepEntity.setCustomName(target.getCustomName());
                mutatedSheepEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedSheepEntity.enablePersistence();
            this.world.addEntity(mutatedSheepEntity);
            target.remove();
        } else if (this.target instanceof PigEntity){
            MutatedPigEntity mutatedPigEntity = new MutatedPigEntity(ModEntityType.MUTATED_PIG.get(), this.world);
            mutatedPigEntity.setLocationAndAngles(target.getPosX(), target.getPosY(), target.getPosZ(), target.rotationYaw, target.rotationPitch);
            mutatedPigEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mutatedPigEntity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            if (target.hasCustomName()) {
                mutatedPigEntity.setCustomName(target.getCustomName());
                mutatedPigEntity.setCustomNameVisible(target.isCustomNameVisible());
            }
            mutatedPigEntity.enablePersistence();
            this.world.addEntity(mutatedPigEntity);
            target.remove();
        }
    }
}
