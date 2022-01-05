package com.Polarice3.FireNBlood.init;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.items.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FireNBlood.MOD_ID);

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_WRETCH_SPAWN_EGG = ITEMS.register("tailless_wretch_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_WRETCH, 0x7b3f00, 0x9f8170, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_DRUID_SPAWN_EGG = ITEMS.register("tailless_druid_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_DRUID, 0x7b3f00, 0x0b3c00, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_HORROR_SPAWN_EGG = ITEMS.register("tailless_horror_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_HORROR, 0x7b3f00, 0x673500, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BLACK_BULL_SPAWN_EGG = ITEMS.register("black_bull_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BLACK_BULL, 0x333333, 0x2c2c2c, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> MINOTAUR_SPAWN_EGG = ITEMS.register("minotaur_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.MINOTAUR, 0x2c2c2c, 0x333333, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_PROPHET_SPAWN_EGG = ITEMS.register("tailless_prophet_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_PROPHET, 0x7b3f00, 0xffffff, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ANATHEMA_SPAWN_EGG = ITEMS.register("anathema_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ANATHEMA, 0x7b3f00, 0x452400, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TANK_SPAWN_EGG = ITEMS.register("tank_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TANK, 0x777777, 0x3c3b3b, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CALLER_SPAWN_EGG = ITEMS.register("caller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CALLER, 0xffffff, 0x3b2713, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> REDEMPTOR_SPAWN_EGG = ITEMS.register("redemptor_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.REDEMPTOR, 0x828a9b, 0xbdbdbd, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PROTECTOR_SPAWN_EGG = ITEMS.register("protector_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PROTECTOR, 0xdcbf8b, 0x5f3621, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BREWER_SPAWN_EGG = ITEMS.register("brewer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BREWER, 0x111111, 0x132e01, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HEXER_SPAWN_EGG = ITEMS.register("hexer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HEXER, 0x273a42, 0x223239, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SAVAGER_SPAWN_EGG = ITEMS.register("savager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SAVAGER, 0xdcbf8b, 0x72635a, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> NEOPHYTE_SPAWN_EGG = ITEMS.register("neophyte_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.NEOPHYTE, 0xcccccc, 0x959595, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ACOLYTE_SPAWN_EGG = ITEMS.register("acolyte_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ACOLYTE, 0x4a4a4a, 0x292929, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> VIZIER_SPAWN_EGG = ITEMS.register("vizier_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.VIZIER, 0x1e1c1a, 0x440a67, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> IRK_SPAWN_EGG = ITEMS.register("irk_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.IRK, 8032420, 8032420, new Item.Properties().group(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PENANCE_SPAWN_EGG = ITEMS.register("penance_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PENANCE, 0x2c2c2c, 8032420, new Item.Properties().group(FireNBlood.TAB)));

}
