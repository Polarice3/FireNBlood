package com.Polarice3.FireNBlood.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.IntValue MaxSouls;
    public final ForgeConfigSpec.BooleanValue HexerSpawn;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        this.MaxSouls = buildInt(builder, "Totem Maximum Soul Count", "Totem of Souls", 10000, 100, 999999, "How many Souls a Totem of Souls can hold");
        this.HexerSpawn = buildBoolean(builder, "Hexer Natural Spawn", "Spawns", true, "True - Hexers can spawn near player ala Wandering Trader.");

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
