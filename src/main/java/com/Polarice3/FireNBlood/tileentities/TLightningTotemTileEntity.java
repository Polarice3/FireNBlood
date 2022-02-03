package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.blocks.TLightningTotemBlock;
import com.Polarice3.FireNBlood.entities.utilities.LightningTrapEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TLightningTotemTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    private int levels;
    @Nullable
    private LivingEntity target;
    @Nullable
    private UUID targetUuid;

    public TLightningTotemTileEntity() {
        this(ModTileEntityType.TLIGHTNING_TOTEM.get());
    }

    public TLightningTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return TLightningTotemTileEntity.this.level;
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
        List<PlayerEntity> list = this.level.getEntitiesOfClass(PlayerEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(16.0D, 16.0D, 16.0D));
        if (list.size() > 0) {
            PlayerEntity livingEntity = list.get(0);
            if (livingEntity.isCreative()) {
                if (list.size() > 1) {
                    return list.get(this.level.random.nextInt(list.size()));
                } else {
                    return null;
                }
            } else {
                return livingEntity;
            }
        } else {
            return null;
        }
    }

    public double ParticleSpeed(){
        long t = this.level.getGameTime();
        if (t % 40L == 0L && this.target != null){
            return 0.7D;
        } else {
            return 0.45D;
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
                    this.activated = 100;
                    this.attackMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TLightningTotemBlock.POWERED, true), 3);
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TLightningTotemBlock.POWERED, false), 3);
                }
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TLightningTotemBlock.POWERED, false), 3);
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
        double d0 = worldPosition.getX() + 0.5;
        double d1 = worldPosition.getY();
        double d2 = worldPosition.getZ() + 0.5;

        for (int p = 0; p < 4; ++p) {
            this.level.addParticle(ParticleTypes.FLAME, d0, d1, d2, this.ParticleSpeed(), this.ParticleSpeed(), this.ParticleSpeed());
        }
    }

    public void attackMobs(){
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.ILLUSIONER_CAST_SPELL);
        for (PlayerEntity entity : this.getLevel().getEntitiesOfClass(PlayerEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(16.0D, 16.0D, 16.0D))) {
            LightningTrapEntity lightningTrap = new LightningTrapEntity(this.getLevel(), entity.getX(), entity.getY(), entity.getZ());
            lightningTrap.setDuration(60);
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.getLevel(), entity.getX(), entity.getY(), entity.getZ());
            areaeffectcloudentity.setParticle(ParticleTypes.CLOUD);
            areaeffectcloudentity.setRadius(2.0F);
            areaeffectcloudentity.setDuration(60);
            this.getLevel().addFreshEntity(areaeffectcloudentity);
            this.getLevel().addFreshEntity(lightningTrap);
        }
    }
}
