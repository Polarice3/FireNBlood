package com.Polarice3.FireNBlood.world.structures;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class StructurePieces {
    public static IStructurePieceType TAVERN_PIECE = TavernPiece.Piece::new;
/*
    public static IStructurePieceType PROFANEDTOWER_PIECE = ProfanedTowerPiece.Piece::new;
*/

    public StructurePieces(){
    }

    public static void registerStructurePieces(){
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(FireNBlood.MOD_ID, "tavern"), TAVERN_PIECE);
/*
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(FireNBlood.MOD_ID, "profanedtower"), PROFANEDTOWER_PIECE);
*/
    }
}
