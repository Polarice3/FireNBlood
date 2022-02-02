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
        return this.write(new CompoundNBT());
    }

    public World getWorld() {
        return TLightningTotemTileEntity.this.world;
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
        assert this.world != null;
        List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).grow(16.0D, 16.0D, 16.0D));
        if (list.size() > 0) {
            PlayerEntity livingEntity = list.get(0);
            if (livingEntity.isCreative()) {
                if (list.size() > 1) {
                    return list.get(this.world.rand.nextInt(list.size()));
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
                    this.activated = 100;
                    this.attackMobs();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(TLightningTotemBlock.POWERED, true), 3);
                } else {
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(TLightningTotemBlock.POWERED, false), 3);
                }
            } else {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(TLightningTotemBlock.POWERED, false), 3);
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
        this.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL);
        for (PlayerEntity entity : this.getWorld().getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).grow(16.0D, 16.0D, 16.0D))) {
            LightningTrapEntity lightningTrap = new LightningTrapEntity(this.getWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ());
            lightningTrap.setDuration(60);
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.getWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ());
            areaeffectcloudentity.setParticleData(ParticleTypes.CLOUD);
            areaeffectcloudentity.setRadius(2.0F);
            areaeffectcloudentity.setDuration(60);
            this.getWorld().addEntity(areaeffectcloudentity);
            this.getWorld().addEntity(lightningTrap);
        }
    }
}