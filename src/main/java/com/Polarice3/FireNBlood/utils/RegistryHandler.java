package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.armors.ModArmorMaterial;
import com.Polarice3.FireNBlood.blocks.BlockItemBase;
import com.Polarice3.FireNBlood.blocks.PorcusShrineBlock;
import com.Polarice3.FireNBlood.blocks.TankCoreBlock;
import com.Polarice3.FireNBlood.items.*;
import com.Polarice3.FireNBlood.potions.CursedEffect;
import com.Polarice3.FireNBlood.potions.EvilEyeEffect;
import com.Polarice3.FireNBlood.potions.MinorHarmEffect;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
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
    public static final RegistryObject<Item> MOCKING_EFFIGY = ITEMS.register("mocking_effigy", MockingEffigyItem::new);
    public static final RegistryObject<SacredFishItem> SACRED_FISH = ITEMS.register("sacred_fish", SacredFishItem::new);
    public static final RegistryObject<Item> RALLYING_HORN = ITEMS.register("rallying_horn", RallyingHornItem::new);
    public static final RegistryObject<Item> DARK_SCROLL = ITEMS.register("dark_scroll", DarkScrollItem::new);
    public static final RegistryObject<Item> SOULRUBY = ITEMS.register("soulruby", ItemBase::new);
    public static final RegistryObject<WitchBombItem> WITCHBOMB = ITEMS.register("witchbomb", WitchBombItem::new);
    public static final RegistryObject<GoldTotemItem> GOLDTOTEM = ITEMS.register("goldtotem", GoldTotemItem::new);

    //Tools
    public static final RegistryObject<Item> GOLDEN_MACE = ITEMS.register("golden_mace", GoldenMaceItem::new);
    public static final RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace", DiamondMaceItem::new);
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> STAFFOFVEXATIONS = ITEMS.register("staffofvexations", StaffofVexationsItem::new);
    public static final RegistryObject<Item> WANDOFVEXING = ITEMS.register("wandofvexing", WandofVexingItem::new);
    //Armors
    public static final RegistryObject<Item> FURRED_HELMET = ITEMS.register("furred_helmet", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.HEAD, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_CHESTPLATE = ITEMS.register("furred_chestplate", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.CHEST, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_LEGGINGS = ITEMS.register("furred_leggings", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.LEGS, new Item.Properties().group(FireNBlood.TAB)));
    public static final RegistryObject<Item> FURRED_BOOTS = ITEMS.register("furred_boots", () ->
            new ArmorItem(ModArmorMaterial.FURRED, EquipmentSlotType.FEET, new Item.Properties().group(FireNBlood.TAB)));
    //Blocks
    public static final RegistryObject<Block> BLAZE_CORE_BLOCK = BLOCKS.register("blazecoreblock", TankCoreBlock::new);
    public static final RegistryObject<Block> PORCUS_SHRINE = BLOCKS.register("porcusshrine", PorcusShrineBlock::new);
    //Block Items
    public static final RegistryObject<Item> BLAZE_CORE_BLOCK_ITEM = ITEMS.register("blazecoreblock",
            () -> new BlockItemBase(BLAZE_CORE_BLOCK.get()));
    public static final RegistryObject<Item> PORCUS_SHRINE_ITEM = ITEMS.register("porcusshrine",
            () -> new BlockItemBase(PORCUS_SHRINE.get()));
    //Effects
    public static final RegistryObject<Effect> EVIL_EYE = EFFECTS.register("evileye",
            EvilEyeEffect::new);
    public static final RegistryObject<Effect> CURSED = EFFECTS.register("cursed",
            CursedEffect::new);
    public static final RegistryObject<Effect> MINOR_HARM = EFFECTS.register("minorharm",
            MinorHarmEffect::new);
}
