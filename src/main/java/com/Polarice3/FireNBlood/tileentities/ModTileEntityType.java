package com.Polarice3.FireNBlood.tileentities;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityType {
    public static DeferredRegister<TileEntityType<?>> TILEENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FireNBlood.MOD_ID);

    public static final RegistryObject<TileEntityType<FangTotemTileEntity>> FANG_TOTEM = TILEENTITY_TYPES.register("fang_totem",
            () -> TileEntityType.Builder.create(FangTotemTileEntity::new, RegistryHandler.FANG_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<MutateTotemTileEntity>> MUTATE_TOTEM = TILEENTITY_TYPES.register("mutation_totem",
            () -> TileEntityType.Builder.create(MutateTotemTileEntity::new, RegistryHandler.MUTATE_TOTEM.get()).build(null));
/*
    public static final RegistryObject<TileEntityType<SoulForgeTileEntity>> SOULFORGE = TILEENTITY_TYPES.register("soulforge",
            () -> TileEntityType.Builder.create(SoulForgeTileEntity::new, RegistryHandler.FANG_TOTEM.get()).build(null));
*/
}
