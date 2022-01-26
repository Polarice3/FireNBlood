package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.armors.DarkRobeArmor;
import com.Polarice3.FireNBlood.armors.ModArmorMaterial;
import com.Polarice3.FireNBlood.armors.NecroRobeArmor;
import com.Polarice3.FireNBlood.blocks.*;
import com.Polarice3.FireNBlood.items.*;
import com.Polarice3.FireNBlood.potions.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FireNBlood.MOD_ID);
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FireNBlood.MOD_ID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, FireNBlood.MOD_ID);
    public static KeyBinding[] keyBindings;

    public static void init(){
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        keyBindings = new KeyBinding[1];

        keyBindings[0] = new KeyBinding("key.firenblood.magic", 67, "key.firenblood.category");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    //Items
    public static final RegistryObject<Item> BLAZE_CORE = ITEMS.register("blazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> BROKEN_BLAZE_CORE = ITEMS.register("brokenblazecore", BlazeCoreItem::new);
    public static final RegistryObject<Item> FURLEATHER = ITEMS.register("furleather", ItemBase::new);
    public static final RegistryObject<Item> ROCKETBOOSTER = ITEMS.register("rocketbooster", RocketBoosterItem::new);
    public static final RegistryObject<DarkSteak> DARKSTEAK = ITEMS.register("darksteak", DarkSteak::new);
    public static final RegistryObject<FlameGunItem> FLAMEGUN = ITEMS.register("flamegun", FlameGunItem::new);
    public static final RegistryObject<Item> PADRE_EFFIGY = ITEMS.register("padre_effigy", ItemBase::new);
    public static final RegistryObject<Item> LEAVEFORM = ITEMS.register("leaveform", ItemBase::new);
    public static final RegistryObject<Item> MOCKING_EFFIGY = ITEMS.register("mocking_effigy", MockingEffigyItem::new);
    public static final RegistryObject<SacredFishItem> SACRED_FISH = ITEMS.register("sacred_fish", SacredFishItem::new);
    public static final RegistryObject<Item> RALLYING_HORN = ITEMS.register("rallying_horn", RallyingHornItem::new);
    public static final RegistryObject<Item> DARK_SCROLL = ITEMS.register("dark_scroll", DarkScrollItem::new);
    public static final RegistryObject<Item> SOULRUBY = ITEMS.register("soulruby", ItemBase::new);
    public static final RegistryObject<Item> RIFTSHARD = ITEMS.register("riftshard", ItemBase::new);
    public static final RegistryObject<PhilosophersStoneItem> PHILOSOPHERSSTONE = ITEMS.register("philosophersstone", PhilosophersStoneItem::new);
    public static final RegistryObject<WitchBombItem> WITCHBOMB = ITEMS.register("witchbomb", WitchBombItem::new);
    public static final RegistryObject<GoldTotemItem> GOLDTOTEM = ITEMS.register("goldtotem", GoldTotemItem::new);
    public static final RegistryObject<Item> CURSED_INGOT = ITEMS.register("cursed_ingot", ItemBase::new);
    public static final RegistryObject<Item> SAVAGETOOTH = ITEMS.register("savagetooth", ItemBase::new);
    public static final RegistryObject<Item> EMPTYCORE = ITEMS.register("emptycore", ItemBase::new);
    public static final RegistryObject<Item> CRIPPLINGCORE = ITEMS.register("cripplingcore", ItemBase::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_STEAK_UNCOOKED = ITEMS.register("mutatedsteak_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedSteakItem> MUTATED_STEAK = ITEMS.register("mutatedsteak", MutatedSteakItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_CHICKEN_UNCOOKED = ITEMS.register("mutatedchicken_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedChickenItem> MUTATED_CHICKEN = ITEMS.register("mutatedchicken", MutatedChickenItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_MUTTON_UNCOOKED = ITEMS.register("mutatedmutton_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedMuttonItem> MUTATED_MUTTON = ITEMS.register("mutatedmutton", MutatedMuttonItem::new);
    public static final RegistryObject<UncookedMutatedItem> MUTATED_PORKCHOP_UNCOOKED = ITEMS.register("mutatedporkchop_uncooked", UncookedMutatedItem::new);
    public static final RegistryObject<MutatedPorkchopItem> MUTATED_PORKCHOP = ITEMS.register("mutatedporkchop", MutatedPorkchopItem::new);

    //Tools
    public static final RegistryObject<Item> GOLDEN_MACE = ITEMS.register("golden_mace", GoldenMaceItem::new);
    public static final RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace", DiamondMaceItem::new);
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> WANDOFVEXING = ITEMS.register("wandofvexing", WandofVexingItem::new);
    public static final RegistryObject<Item> STAFFOFVEXATIONS = ITEMS.register("staffofvexations", StaffofVexationsItem::new);
    public static final RegistryObject<Item> WANDOFBITES = ITEMS.register("wandofbites", WandofBitesItem::new);
    public static final RegistryObject<Item> STAFFOFCRUNCHES = ITEMS.register("staffofcrunches", StaffofCrunchesItem::new);
    public static final RegistryObject<Item> WANDOFROARING = ITEMS.register("wandofroaring", WandofRoaringItem::new);
    public static final RegistryObject<Item> STAFFOFROARS = ITEMS.register("staffofroars", StaffofRoarsItem::new);
    public static final RegistryObject<Item> WANDOFDEAD = ITEMS.register("wandofdead", WandofDeadItem::new);
    public static final RegistryObject<Item> STAFFOFNECROTURGY = ITEMS.register("staffofnecroturgy", StaffofNecroturgyItem::new);
    public static final RegistryObject<Item> WANDOFOSSEOUS = ITEMS.register("wandofosseous", WandofOsseousItem::new);
    public static final RegistryObject<Item> STAFFOFWALKINGBONES = ITEMS.register("staffofwalkingbones", StaffofWalkingBonesItem::new);
    public static final RegistryObject<Item> WANDOFCRIPPLE = ITEMS.register("wandofcripple", WandofCrippleItem::new);
    public static final RegistryObject<Item> STAFFOFCRIPPLING = ITEMS.register("staffofcrippling", StaffofCripplingItem::new);

    //Armors
    public static final RegistryObject<Item> FURRED_HELMET = ITEMS.register("furred_helmet", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.HEAD, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_CHESTPLATE = ITEMS.register("furred_chestplate", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.CHEST, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_LEGGINGS = ITEMS.register("furred_leggings", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.LEGS, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_BOOTS = ITEMS.register("furred_boots", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.FEET, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> DARKHELM = ITEMS.register("darkhelm", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.HEAD, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> DARKROBE = ITEMS.register("darkrobe", () ->
            new DarkRobeArmor(ModArmorMaterial.DARKMAGE, EquipmentSlotType.CHEST, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> NECROHELM = ITEMS.register("necrohelm", () ->
            new NecroRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.HEAD, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> NECROROBE = ITEMS.register("necrorobe", () ->
            new NecroRobeArmor(ModArmorMaterial.NECROTURGE, EquipmentSlotType.CHEST, new Item.Properties().group(FireNBlood.TAB)));
    //Blocks
    public static final RegistryObject<Block> BLAZE_CORE_BLOCK = BLOCKS.register("blazecoreblock", TankCoreBlock::new);
    public static final RegistryObject<Block> PORCUS_SHRINE = BLOCKS.register("porcusshrine", PorcusShrineBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BLOCK = BLOCKS.register("cursed_stone", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_BRICK_BLOCK = BLOCKS.register("cursed_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_CHISELED_BLOCK = BLOCKS.register("cursed_stone_chiseled", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_TILES_BLOCK = BLOCKS.register("cursed_tiles", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_CAGE_BLOCK = BLOCKS.register("cursed_cage", CursedCageBlock::new);
    public static final RegistryObject<Block> CURSED_TOTEM_BLOCK = BLOCKS.register("cursed_totem", CursedStoneBlock::new);
    public static final RegistryObject<Block> FANG_TOTEM = BLOCKS.register("fang_totem", FangTotemBlock::new);
    public static final RegistryObject<Block> MUTATE_TOTEM = BLOCKS.register("mutation_totem", MutateTotemBlock::new);

    //Slabs
    public static final RegistryObject<Block> CURSED_STONE_SLAB_BLOCK = BLOCKS.register("cursed_stone_slab",
        () -> new SlabBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE)
                .hardnessAndResistance(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_BRICK_SLAB_BLOCK = BLOCKS.register("cursed_bricks_slab",
            () -> new SlabBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_TILES_SLAB_BLOCK = BLOCKS.register("cursed_tiles_slab",
            () -> new SlabBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    //Stairs
    public static final RegistryObject<Block> CURSED_STONE_STAIRS_BLOCK = BLOCKS.register("cursed_stone_stairs",
            () -> new StairsBlock(CURSED_STONE_BLOCK.get().getDefaultState(), AbstractBlock.Properties.from(CURSED_STONE_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_BRICK_STAIRS_BLOCK = BLOCKS.register("cursed_bricks_stairs",
            () -> new StairsBlock(CURSED_BRICK_BLOCK.get().getDefaultState(), AbstractBlock.Properties.from(CURSED_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_TILES_STAIRS_BLOCK = BLOCKS.register("cursed_tiles_stairs",
            () -> new StairsBlock(CURSED_TILES_BLOCK.get().getDefaultState(), AbstractBlock.Properties.from(CURSED_TILES_BLOCK.get())));
    //Walls
    public static final RegistryObject<Block> CURSED_BRICK_WALL_BLOCK = BLOCKS.register("cursed_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.from(CURSED_BRICK_BLOCK.get())));
    //Panes
    public static final RegistryObject<Block> CURSED_BARS_BLOCK = BLOCKS.register("cursed_bars",
            () -> new PaneBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.AIR)
                    .setRequiresTool()
                    .hardnessAndResistance(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .notSolid()));
    //Block Items
    public static final RegistryObject<Item> BLAZE_CORE_BLOCK_ITEM = ITEMS.register("blazecoreblock",
            () -> new BlockItemBase(BLAZE_CORE_BLOCK.get()));
    public static final RegistryObject<Item> PORCUS_SHRINE_ITEM = ITEMS.register("porcusshrine",
            () -> new BlockItemBase(PORCUS_SHRINE.get()));
    public static final RegistryObject<Item> CURSED_STONE_ITEM = ITEMS.register("cursed_stone",
            () -> new BlockItemBase(CURSED_STONE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_SLAB_ITEM = ITEMS.register("cursed_stone_slab",
            () -> new BlockItemBase(CURSED_STONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_STAIRS_ITEM = ITEMS.register("cursed_stone_stairs",
            () -> new BlockItemBase(CURSED_STONE_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_ITEM = ITEMS.register("cursed_bricks",
            () -> new BlockItemBase(CURSED_BRICK_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_SLAB_ITEM = ITEMS.register("cursed_bricks_slab",
            () -> new BlockItemBase(CURSED_BRICK_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_STAIRS_ITEM = ITEMS.register("cursed_bricks_stairs",
            () -> new BlockItemBase(CURSED_BRICK_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_WALL_ITEM = ITEMS.register("cursed_bricks_wall",
            () -> new BlockItemBase(CURSED_BRICK_WALL_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_CHISELED_ITEM = ITEMS.register("cursed_stone_chiseled",
            () -> new BlockItemBase(CURSED_STONE_CHISELED_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_ITEM = ITEMS.register("cursed_tiles",
            () -> new BlockItemBase(CURSED_TILES_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_SLAB_ITEM = ITEMS.register("cursed_tiles_slab",
            () -> new BlockItemBase(CURSED_TILES_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_STAIRS_ITEM = ITEMS.register("cursed_tiles_stairs",
            () -> new BlockItemBase(CURSED_TILES_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BARS_ITEM = ITEMS.register("cursed_bars",
            () -> new BlockItemBase(CURSED_BARS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_CAGE_ITEM = ITEMS.register("cursed_cage",
            () -> new BlockItemBase(CURSED_CAGE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TOTEM_ITEM = ITEMS.register("cursed_totem",
            () -> new BlockItemBase(CURSED_TOTEM_BLOCK.get()));
    public static final RegistryObject<Item> FANG_TOTEM_ITEM = ITEMS.register("fang_totem",
            () -> new BlockItemBase(FANG_TOTEM.get()));
    public static final RegistryObject<Item> MUTATE_TOTEM_ITEM = ITEMS.register("mutation_totem",
            () -> new BlockItemBase(MUTATE_TOTEM.get()));
    //Effects
    public static final RegistryObject<Effect> EVIL_EYE = EFFECTS.register("evileye",
            EvilEyeEffect::new);
    public static final RegistryObject<Effect> CURSED = EFFECTS.register("cursed",
            CursedEffect::new);
    public static final RegistryObject<Effect> MINOR_HARM = EFFECTS.register("minorharm",
            MinorHarmEffect::new);
    public static final RegistryObject<Effect> DEATHPROTECT = EFFECTS.register("deathprotect",
            DeathProtectEffect::new);
    public static final RegistryObject<Effect> SOULDRAIN = EFFECTS.register("souldrain",
            SoulDrainEffect::new);
    public static final RegistryObject<Effect> GOLDTOUCHED = EFFECTS.register("goldtouched",
            GoldTouchEffect::new);
    public static final RegistryObject<Effect> HOSTED = EFFECTS.register("hosted",
            HostedEffect::new);
    public static final RegistryObject<Effect> SUMMONDOWN = EFFECTS.register("summondown",
            SummonDownEffect::new);
}
