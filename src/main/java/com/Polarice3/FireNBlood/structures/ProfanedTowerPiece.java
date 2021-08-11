package com.Polarice3.FireNBlood.structures;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

/*public class ProfanedTowerPiece {
    private static final BlockPos STRUCTURE_OFFSET = new BlockPos(0, 0, 0);
    private static final ResourceLocation PROFANEDTOWER = new ResourceLocation(FireNBlood.MOD_ID,"profanedtower");

    public static void func_236991_a_(TemplateManager p_236991_0_, BlockPos p_236991_1_, Rotation p_236991_2_, List<StructurePiece> p_236991_3_, Random p_236991_4_) {
        p_236991_3_.add(new ProfanedTowerPiece.Piece(p_236991_0_, PROFANEDTOWER, p_236991_1_, p_236991_2_, 0));
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final Rotation rotation;

        public Piece(TemplateManager p_i49313_1_, ResourceLocation p_i49313_2_, BlockPos p_i49313_3_, Rotation p_i49313_4_, int p_i49313_5_) {
            super(StructurePieces.PROFANEDTOWER_PIECE, 0);
            this.resourceLocation = p_i49313_2_;
            this.templatePosition = p_i49313_3_;
            this.rotation = p_i49313_4_;
            this.func_204754_a(p_i49313_1_);
        }

        public Piece(TemplateManager p_i50566_1_, CompoundNBT p_i50566_2_) {
            super(StructurePieces.PROFANEDTOWER_PIECE, p_i50566_2_);
            this.resourceLocation = new ResourceLocation(p_i50566_2_.getString("Template"));
            this.rotation = Rotation.valueOf(p_i50566_2_.getString("Rot"));
            this.func_204754_a(p_i50566_1_);
        }

        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        private void func_204754_a(TemplateManager p_204754_1_) {
            Template template = p_204754_1_.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setCenterOffset(ProfanedTowerPiece.STRUCTURE_OFFSET).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if ("chest".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                TileEntity tileentity = worldIn.getTileEntity(pos.down());
                if (tileentity instanceof ChestTileEntity) {
                    ((ChestTileEntity) tileentity).setLootTable(LootTables.CHESTS_WOODLAND_MANSION, rand.nextLong());
                }

            }
        }

        public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
*//*            int i = 256;
            int j = 0;
            BlockPos blockpos = this.template.getSize();
            Heightmap.Type heightmap$type = Heightmap.Type.WORLD_SURFACE_WG;
            int k = blockpos.getX() * blockpos.getZ();
            if (k == 0) {
                j = p_230383_1_.getHeight(heightmap$type, this.templatePosition.getX(), this.templatePosition.getZ());
            } else {
                BlockPos blockpos1 = this.templatePosition.add(blockpos.getX() - 1, 0, blockpos.getZ() - 1);

                for(BlockPos blockpos2 : BlockPos.getAllInBoxMutable(this.templatePosition, blockpos1)) {
                    int l = p_230383_1_.getHeight(heightmap$type, blockpos2.getX(), blockpos2.getZ());
                    j += l;
                    i = Math.min(i, l);
                }

                j = j / k;
            }

            int i1 = i - blockpos.getY() / 2 - p_230383_4_.nextInt(3);
            this.templatePosition = new BlockPos(this.templatePosition.getX(), j, this.templatePosition.getZ());*//*
            return super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
        }
    }

}*/
