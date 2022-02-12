package com.Polarice3.FireNBlood;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class FNBConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> CrippleCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> TeleportCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SoulSkullCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FangDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoarDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> CrippleDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SpiderlingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FeastDuration;

    public static final ForgeConfigSpec.ConfigValue<Integer> BrainEaterXPCost;

    public static final ForgeConfigSpec.ConfigValue<Integer> VexCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> ZombieCooldown;
    public static final ForgeConfigSpec.ConfigValue<Integer> SkeletonCooldown;

    public static final ForgeConfigSpec.ConfigValue<Integer> UndeadSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> AnthropodSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> IllagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> VillagerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> TaillessSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PiglinSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderDragonSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> PlayerSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> DefaultSouls;

    public static final ForgeConfigSpec.ConfigValue<Integer> MaxEnchant;

    public static final ForgeConfigSpec.ConfigValue<Integer> HiredTimer;
    public static final ForgeConfigSpec.ConfigValue<Integer> DyingTimer;

    public static final ForgeConfigSpec.ConfigValue<Integer> CraftingSouls;
    public static final ForgeConfigSpec.ConfigValue<Integer> CultistSpawnFreq;
    public static final ForgeConfigSpec.ConfigValue<Integer> MRabbitMax;

    public static final ForgeConfigSpec.ConfigValue<Float> HealAmount;

    public static final ForgeConfigSpec.ConfigValue<Boolean> HexerSpawn;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CultistsSpawn;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SoulRepair;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemUndying;
    public static final ForgeConfigSpec.ConfigValue<Boolean> StarterTotem;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ProfanedTowerGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ProfanedShrineGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TavernGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DarkManorGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PortalOutpostGen;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TotemGen;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VizierMinion;

    static {
        BUILDER.push("General");
        MaxSouls = BUILDER.comment("Totem Maximum Soul Count, Default: 10000")
                .defineInRange("maxSouls", 10000, 100, Integer.MAX_VALUE);
        SoulRepair = BUILDER.comment("Dark and Necro Robes repair themselves using Soul Energy, Default: true")
                .define("soulrepair", true);
        TotemUndying = BUILDER.comment("Totem of Souls will save the Player if full of Soul Energy, Default: true")
                .define("totemundying", true);
        MaxEnchant = BUILDER.comment("Soul Eater Maximum Enchantment Level, Default: 3")
                .defineInRange("maxEnchant", 3, 1, 10);
        StarterTotem = BUILDER.comment("Gives Players a Totem of Souls when first entering World, Default: false")
                .define("startertotem", false);
        CraftingSouls = BUILDER.comment("How much Souls is consumed when crafting with Totem, Default: 1")
                .defineInRange("craftsouls", 1, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Protectors");
        HiredTimer = BUILDER.comment("Hired Timer, Default: 24000")
                .defineInRange("hiredtimer", 24000, 0, 72000);
        DyingTimer = BUILDER.comment("Dying Timer, Default: 12000")
                .defineInRange("dyingtimer", 12000, 0, 72000);
        HealAmount = BUILDER.comment("Heal Amount after being fed (Always put an F at the end of the value!), Default: 10.0F")
                .define("healamount", 10.0F);
        BUILDER.pop();
        BUILDER.push("Soul Taken");
        UndeadSouls = BUILDER.comment("Undead Killed, Default: 5")
                .defineInRange("undeadsouls", 5, 0, Integer.MAX_VALUE);
        AnthropodSouls = BUILDER.comment("Anthropods Killed, Default: 5")
                .defineInRange("anthropodsouls", 5, 0, Integer.MAX_VALUE);
        IllagerSouls = BUILDER.comment("Illagers, Witches, Cultists, Protectors Killed, Default: 25")
                .defineInRange("illagersouls", 25, 0, Integer.MAX_VALUE);
        VillagerSouls = BUILDER.comment("Villagers Killed, Default: 50")
                .defineInRange("villagersouls", 50, 0, Integer.MAX_VALUE);
        TaillessSouls = BUILDER.comment("Tailless Killed, Default: 45")
                .defineInRange("taillesssouls", 45, 0, Integer.MAX_VALUE);
        PiglinSouls = BUILDER.comment("Non-Undead Piglin Killed, Default: 10")
                .defineInRange("piglinsouls", 10, 0, Integer.MAX_VALUE);
        EnderDragonSouls = BUILDER.comment("Ender Dragon Killed, Default: 1000")
                .defineInRange("enderdragonsouls", 1000, 0, Integer.MAX_VALUE);
        PlayerSouls = BUILDER.comment("Players Killed, Default: 100")
                .defineInRange("playersouls", 100, 0, Integer.MAX_VALUE);
        DefaultSouls = BUILDER.comment("Others Killed, Default: 5")
                .defineInRange("othersouls", 5, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Spell Costs");
        VexCost = BUILDER.comment("Vex Spell Cost, Default: 18")
                .defineInRange("vexcost", 18, 0, Integer.MAX_VALUE);
        FangCost = BUILDER.comment("Fang Spell Cost, Default: 8")
                .defineInRange("fangcost", 8, 0, Integer.MAX_VALUE);
        RoarCost = BUILDER.comment("Roaring Spell Cost, Default: 10")
                .defineInRange("bitecost", 10, 0, Integer.MAX_VALUE);
        ZombieCost = BUILDER.comment("Necroturgy Spell Cost, Default: 5")
                .defineInRange("zombiecost", 5, 0, Integer.MAX_VALUE);
        SkeletonCost = BUILDER.comment("Osseous Spell Cost, Default: 8")
                .defineInRange("skeletoncost", 8, 0, Integer.MAX_VALUE);
        CrippleCost = BUILDER.comment("Crippling Spell Cost, Default: 15")
                .defineInRange("cripplecost", 15, 0, Integer.MAX_VALUE);
        SpiderlingCost = BUILDER.comment("Spiderling Spell Cost per second, Default: 2")
                .defineInRange("spiderlingcost", 2, 0, Integer.MAX_VALUE);
        BrainEaterCost = BUILDER.comment("Brain Eater Spell Cost per second, Default: 5")
                .defineInRange("braincost", 5, 0, Integer.MAX_VALUE);
        BrainEaterXPCost = BUILDER.comment("How much Experience the above spell Cost per heal, Default: 10")
                .defineInRange("brainxpcost", 10, 0, 1000);
        TeleportCost = BUILDER.comment("Teleport Spell Cost, Default: 5")
                .defineInRange("teleportcost", 5, 0, Integer.MAX_VALUE);
        SoulSkullCost = BUILDER.comment("Soul Skull Spell Cost, Default: 16")
                .defineInRange("soulskullcost", 16, 0, Integer.MAX_VALUE);
        FeastCost = BUILDER.comment("Feasting Spell Cost, Default: 8")
                .defineInRange("feastcost", 8, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Casting Time");
        VexDuration = BUILDER.comment("Time to cast Vex Spell, Default: 100")
                .defineInRange("vextime", 100, 0, 72000);
        FangDuration = BUILDER.comment("Time to cast Fang Spell, Default: 80")
                .defineInRange("fangtime", 80, 0, 72000);
        RoarDuration = BUILDER.comment("Time to cast Roaring Spell, Default: 40")
                .defineInRange("roartime", 40, 0, 72000);
        ZombieDuration = BUILDER.comment("Time to cast Necroturgy Spell, Default: 60")
                .defineInRange("zombietime", 60, 0, 72000);
        SkeletonDuration = BUILDER.comment("Time to cast Osseous Spell, Default: 60")
                .defineInRange("skeletontime", 60, 0, 72000);
        CrippleDuration = BUILDER.comment("Time to cast Crippling Spell, Default: 20")
                .defineInRange("crippletime", 20, 0, 72000);
        SpiderlingDuration = BUILDER.comment("Time to cast Spiderling Spell per second, Default: 20")
                .defineInRange("spiderlingtime", 20, 0, 72000);
        BrainEaterDuration = BUILDER.comment("Time to cast Brain Eater Spell per second, Default: 20")
                .defineInRange("braineatertime", 20, 0, 72000);
        FeastDuration = BUILDER.comment("Time to cast Feasting Spell per second, Default: 20")
                .defineInRange("feasttime", 20, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Summon Down Duration");
        VexCooldown = BUILDER.comment("Vex Spell Cooldown, Default: 340")
                .defineInRange("vexcooldown", 340, 0, 72000);
        ZombieCooldown = BUILDER.comment("Necroturgy Spell Cooldown, Default: 250")
                .defineInRange("zombiecooldown", 250, 0, 72000);
        SkeletonCooldown = BUILDER.comment("Osseous Spell Cooldown, Default: 280")
                .defineInRange("skeletoncooldown", 280, 0, 72000);
        BUILDER.pop();
        BUILDER.push("Mob Spawning");
        HexerSpawn = BUILDER.comment("Hexer Spawning, Default: true")
                .define("hexerspawn", true);
        CultistsSpawn = BUILDER.comment("Cultists Spawning, Default: true")
                .define("cultistsspawn", true);
        CultistSpawnFreq = BUILDER.comment("Spawn Frequency for Cultists, Default: 12000")
                .defineInRange("cultistsspawnfreq", 12000, 0, 72000);
        MRabbitMax = BUILDER.comment("Maximum amount of Mutant Rabbits can spawn, Default: 16")
                .defineInRange("mrabbitmax", 16, 0, 100);
        BUILDER.pop();
        BUILDER.push("Structure Generation");
        TavernGen = BUILDER.comment("Tavern Generates in the World, Default: true")
                .define("taverngen", true);
        ProfanedTowerGen = BUILDER.comment("Profaned Tower Generates in the World, Default: true")
                .define("profanedtowergen", true);
        ProfanedShrineGen = BUILDER.comment("Profaned Shrine Generates in the World, Default: true")
                .define("profanedshrinegen", true);
        DarkManorGen = BUILDER.comment("Dark Manor Generates in the World, Default: true")
                .define("darkmanorgen", true);
        PortalOutpostGen = BUILDER.comment("Portal Outpost Generates in the World, Default: true")
                .define("portaloutpostgen", true);
        TotemGen = BUILDER.comment("Totems Generates in the World, Default: true")
                .define("totemgen", true);
        BUILDER.pop();
        BUILDER.push("Misc");
        VizierMinion = BUILDER.comment("Viziers spawn Vexes instead of Irks, Default: false")
                .define("vizierminion", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

}
