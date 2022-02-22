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
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_WRETCH, 0x7b3f00, 0x9f8170, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_DRUID_SPAWN_EGG = ITEMS.register("tailless_druid_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_DRUID, 0x7b3f00, 0x0b3c00, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_HORROR_SPAWN_EGG = ITEMS.register("tailless_horror_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_HORROR, 0x7b3f00, 0x673500, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BLACK_BULL_SPAWN_EGG = ITEMS.register("black_bull_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BLACK_BULL, 0x333333, 0x2c2c2c, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> MINOTAUR_SPAWN_EGG = ITEMS.register("minotaur_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.MINOTAUR, 0x2c2c2c, 0x333333, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TAILLESS_PROPHET_SPAWN_EGG = ITEMS.register("tailless_prophet_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TAILLESS_PROPHET, 0x7b3f00, 0xffffff, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ANATHEMA_SPAWN_EGG = ITEMS.register("anathema_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ANATHEMA, 0x7b3f00, 0x452400, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> TANK_SPAWN_EGG = ITEMS.register("tank_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.TANK, 0x777777, 0x3c3b3b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CALLER_SPAWN_EGG = ITEMS.register("caller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CALLER, 0xffffff, 0x3b2713, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> REDEMPTOR_SPAWN_EGG = ITEMS.register("redemptor_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.REDEMPTOR, 0x828a9b, 0xbdbdbd, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PROTECTOR_SPAWN_EGG = ITEMS.register("protector_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PROTECTOR, 0xdcbf8b, 0x5f3621, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> BREWER_SPAWN_EGG = ITEMS.register("brewer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.BREWER, 0x111111, 0x132e01, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> HEXER_SPAWN_EGG = ITEMS.register("hexer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.HEXER, 0x273a42, 0x223239, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SAVAGER_SPAWN_EGG = ITEMS.register("savager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SAVAGER, 0xdcbf8b, 0x72635a, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> NEOPHYTE_SPAWN_EGG = ITEMS.register("neophyte_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.NEOPHYTE, 0xcccccc, 0x959595, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ACOLYTE_SPAWN_EGG = ITEMS.register("acolyte_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ACOLYTE, 0x4a4a4a, 0x292929, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CHANNELLER_SPAWN_EGG = ITEMS.register("channeller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CHANNELLER, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> FANATIC_SPAWN_EGG = ITEMS.register("fanatic_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.FANATIC, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZEALOT_SPAWN_EGG = ITEMS.register("zealot_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZEALOT, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> DISCIPLE_SPAWN_EGG = ITEMS.register("disciple_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.DISCIPLE, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> APOSTLE_SPAWN_EGG = ITEMS.register("apostle_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.APOSTLE, 0x120e0e, 0x5a0b0b, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> VIZIER_SPAWN_EGG = ITEMS.register("vizier_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.VIZIER, 0x1e1c1a, 0x440a67, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SACRED_FISH_SPAWN_EGG = ITEMS.register("sacredfish_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SACRED_FISH, 0xfacb32, 0xfff34d, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PARASITE_SPAWN_EGG = ITEMS.register("parasite_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PARASITE, 0xffc975, 0xffb541, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> IRK_SPAWN_EGG = ITEMS.register("irk_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.IRK, 8032420, 8032420, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SCORCH_SPAWN_EGG = ITEMS.register("scorch_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SCORCH, 0x3b1414, 0xf48522, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> NETHERNAL_SPAWN_EGG = ITEMS.register("nethernal_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.NETHERNAL, 0x100606, 0xfaeb72, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> ZOMBIE_MINION_SPAWN_EGG = ITEMS.register("zombie_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.ZOMBIE_MINION, 0x192927, 0x737885, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SKELETON_MINION_SPAWN_EGG = ITEMS.register("skeleton_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SKELETON_MINION, 0x1f1f1f, 0x6e6473, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> SPIDERLING_MINION_SPAWN_EGG = ITEMS.register("spiderling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.SPIDERLING_MINION, 0xc18a34, 0x3c0202, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> CREEPERLING_MINION_SPAWN_EGG = ITEMS.register("creeperling_minion_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.CREEPERLING_MINION, 0x1c4c15, 0x000000, new Item.Properties().tab(FireNBlood.TAB)));

    public static final RegistryObject<ModSpawnEggItem> PENANCE_SPAWN_EGG = ITEMS.register("penance_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityType.PENANCE, 0x2c2c2c, 8032420, new Item.Properties().tab(FireNBlood.TAB)));

}
