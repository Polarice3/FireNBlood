package com.Polarice3.FireNBlood;

import com.Polarice3.FireNBlood.client.ClientConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class FNBConfig {

    public static int MaxSouls = 10000;
    public static boolean HexerSpawn = true;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static final ClientConfig CLIENT;

    public static void bakeClient(final ModConfig config) {
        try {
            MaxSouls = CLIENT.MaxSouls.get();
            HexerSpawn = CLIENT.HexerSpawn.get();
        }catch (Exception e){
            FireNBlood.LOGGER.warn("An exception was caused trying to load the config for Fire N Blood.");
            e.printStackTrace();
        }
    }

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }

    }
}
