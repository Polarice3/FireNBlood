package com.Polarice3.FireNBlood.events;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class HexerWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "firenblood_hexer";
    private World world;
    private int tickCounter;
    private int hexerSpawnDelay;
    private int hexerSpawnChance;

    public HexerWorldData() {
        super(IDENTIFIER);
    }

    public static HexerWorldData get(World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(world.getDimensionKey());

            DimensionSavedDataManager storage = overworld.getSavedData();
            HexerWorldData data = storage.getOrCreate(HexerWorldData::new, IDENTIFIER);
            if(data != null){
                data.world = world;
                data.markDirty();
            }
            return data;
        }
        return null;
    }

    public int getHexerSpawnDelay() {
        return this.hexerSpawnDelay;
    }

    public void setHexerSpawnDelay(int delay) {
        this.hexerSpawnDelay = delay;
    }

    public int getHexerSpawnChance() {
        return this.hexerSpawnChance;
    }

    public void setHexerSpawnChance(int chance) {
        this.hexerSpawnChance = chance;
    }

    public void tick() {
        ++this.tickCounter;
    }

    @Override
    public void read(CompoundNBT nbt) {
        if (nbt.contains("HexerSpawnDelay", 99)) {
            this.hexerSpawnDelay = nbt.getInt("HexerSpawnDelay");
        }

        if (nbt.contains("HexerSpawnChance", 99)) {
            this.hexerSpawnChance = nbt.getInt("HexerSpawnChance");
        }

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("HexerSpawnDelay", this.hexerSpawnDelay);
        compound.putInt("HexerSpawnChance", this.hexerSpawnChance);
        return compound;
    }
}
