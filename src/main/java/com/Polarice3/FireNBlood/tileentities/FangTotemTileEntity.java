package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.blocks.FangTotemBlock;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
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
        return this.write(new CompoundNBT());
    }

    public World getWorld() {
        return FangTotemTileEntity.this.world;
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
        List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).grow(10.0D, 10.0D, 10.0D));
        if (list.size() > 0) {
            LivingEntity livingEntity = list.get(0);
            if (livingEntity instanceof PlayerEntity) {
                if (((PlayerEntity) livingEntity).isCreative()) {
                    if (list.size() > 1) {
                        return list.get(this.world.rand.nextInt(list.size()));
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
                    this.attackMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FangTotemBlock.POWERED, true), 3);
                } else {
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FangTotemBlock.POWERED, false), 3);
                }
            } else {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FangTotemBlock.POWERED, false), 3);
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

    public void attackMobs(){
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        this.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK);
        for (LivingEntity entity : this.getWorld().getEntitiesWithinAABB(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).grow(10.0D, 10.0D, 10.0D))) {
            float f = (float) MathHelper.atan2(entity.getPosZ() - this.getPos().getZ(), entity.getPosX() - this.getPos().getX());

            this.spawnFangs(entity.getPosX(), entity.getPosZ(), entity.getPosY(), entity.getPosY() + 1.0D, f, 1);
        }
    }

    private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate = this.getWorld().getBlockState(blockpos1);
            if (blockstate.isSolidSide(this.getWorld(), blockpos1, Direction.UP)) {
                if (!this.getWorld().isAirBlock(blockpos)) {
                    BlockState blockstate1 = this.getWorld().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.getWorld(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.down();
        } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

        if (flag) {
            this.getWorld().addEntity(new EvokerFangsEntity(this.getWorld(), p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, null));
        }

    }
}
