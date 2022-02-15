package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.blocks.FangTotemBlock;
import com.Polarice3.FireNBlood.particles.ModParticleTypes;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FangTotemTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    private int levels;
    @Nullable
    private LivingEntity target;
    @Nullable
    private UUID targetUuid;

    public FangTotemTileEntity() {
        this(ModTileEntityType.FANG_TOTEM.get());
    }

    public FangTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return FangTotemTileEntity.this.level;
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
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D));
        if (list.size() > 0) {
            LivingEntity livingEntity = list.get(0);
            if (livingEntity instanceof PlayerEntity) {
                if (((PlayerEntity) livingEntity).isCreative()) {
                    if (list.size() > 1) {
                        return list.get(this.level.random.nextInt(list.size()));
                    } else {
                        return null;
                    }
                } else {
                    return livingEntity;
                }
            } else {
                return livingEntity;
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
                    this.attackMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(FangTotemBlock.POWERED, true), 3);
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(FangTotemBlock.POWERED, false), 3);
                }
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(FangTotemBlock.POWERED, false), 3);
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

    public void attackMobs(){
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK);
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D))) {
            float f = (float) MathHelper.atan2(entity.getZ() - this.getBlockPos().getZ(), entity.getX() - this.getBlockPos().getX());
            if (entity instanceof PlayerEntity) {
                if (!((PlayerEntity) entity).isCreative()) {
                    this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f, 1);
                }
            } else {
                this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f, 1);
            }
        }
    }

    private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.getLevel().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.getLevel(), blockpos1, Direction.UP)) {
                if (!this.getLevel().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.getLevel().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.getLevel(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

        if (flag) {
            this.getLevel().addFreshEntity(new EvokerFangsEntity(this.getLevel(), p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, null));
        }

    }
}
