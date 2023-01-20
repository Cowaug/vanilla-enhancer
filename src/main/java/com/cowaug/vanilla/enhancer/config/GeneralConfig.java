package com.cowaug.vanilla.enhancer.config;

import com.cowaug.vanilla.enhancer.utils.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralConfig {
    private static final String CONFIG_FILE = "/general.yaml";
    private static Map<String, String> generalConfigYamlMap; // key, value

    public static void LoadConfig(String externalUrl) {
        if (externalUrl != null) {
            generalConfigYamlMap = ConfigIo.LoadConfigFromUrl(externalUrl);
        }

        if (externalUrl == null || generalConfigYamlMap == null) {
            generalConfigYamlMap = ConfigIo.LoadConfig(CONFIG_FILE);
        }

        if (generalConfigYamlMap == null) {
            CreateDefaultConfig();
        }
        ConfigIo.WriteConfig(CONFIG_FILE, generalConfigYamlMap);
    }

    private static void CreateDefaultConfig() {
        generalConfigYamlMap = new LinkedHashMap<>();
    }

    // misc
    public static int getAgeIncreasePerTick() {
        return getConfig(ConfigName.AGING_PER_TICK, 1);
    }

    public static boolean isAllowServerOverride() {
        return getConfig(ConfigName.ALLOW_SERVER_OVERRIDE, true);
    }

    public static boolean isObfuscateServer() {
        return getConfig(ConfigName.OBFUSCATE_SERVER, true);
    }

    public static float getMinEntityRotateSpeed() {
        return getConfig(ConfigName.MIN_ENTITY_ROTATE, 0.8f);
    }

    public static float getMaxEntityRotateSpeed() {
        return getConfig(ConfigName.MAX_ENTITY_ROTATE, 1.5f);
    }

    public static boolean isSpawnParticleOnItem() {
        return getConfig(ConfigName.SPAWN_PARTICLE_ON_ITEM, true);
    }

    public static boolean isGlowBorderOnItem() {
        return getConfig(ConfigName.GLOW_BORDER_ON_ITEM, false);
    }

    // keep inventory trader
    public static String getKeepInventoryTraderName() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_NAME, "Keep Inventory Trader");
    }

    public static String getKeepInventoryTraderTag() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_TAG, "keep_inventory_trader");
    }

    public static String getKeepInventoryTraderBuyItem1Key() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_BUY_ITEM_1_KEY, "dirt");
    }

    public static int getKeepInventoryTraderBuyItem1Count() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_BUY_ITEM_1_COUNT, 64);
    }

    public static String getKeepInventoryTraderBuyItem2Key() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_BUY_ITEM_2_KEY, "diamond_block");
    }

    public static int getKeepInventoryTraderBuyItem2Count() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_BUY_ITEM_2_COUNT, 3);
    }

    public static String getKeepInventoryTraderBookName() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_BOOK_NAME, "Keep Inventory Book");
    }

    public static String getKeepInventoryTraderDefaultSpawnLocation() {
        return getConfig(ConfigName.KEEP_INVENTORY_TRADER_DEFAULT_SPAWN_LOC, "0 150 0");
    }

    public static void setKeepInventoryTraderDefaultSpawnLocation(String posString) {
        saveConfig(ConfigName.KEEP_INVENTORY_TRADER_DEFAULT_SPAWN_LOC, posString);
    }


    @SuppressWarnings("unchecked")
    private static <T> T getConfig(ConfigName configName, T defaultValue) {
        String result = generalConfigYamlMap.getOrDefault(configName.toString(), null);
        try {
            if (result == null) {
                throw new NullPointerException();
            }

            if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(result);
            }
            if (defaultValue instanceof Double) {
                return (T) Double.valueOf(result);
            }
            if (defaultValue instanceof Float) {
                return (T) Float.valueOf(result);
            }
            if (defaultValue instanceof String) {
                return (T) result;
            }
            if (defaultValue instanceof Boolean) {
                return (T) Boolean.valueOf(result);
            }
        } catch (Exception e) {
            Log.LogWarn("Invalid config found for '" + configName + "'. Setting value to '" + defaultValue + "'");
            saveConfig(configName, defaultValue);
            return defaultValue;
        }

        return defaultValue;
    }

    private static <T> void saveConfig(ConfigName configName, T newValue) {
        generalConfigYamlMap.put(configName.name(), newValue + "");
        ConfigIo.WriteConfig(CONFIG_FILE, generalConfigYamlMap);
    }

    public enum ConfigName {
        AGING_PER_TICK,
        ALLOW_SERVER_OVERRIDE,
        OBFUSCATE_SERVER,
        MIN_ENTITY_ROTATE,
        MAX_ENTITY_ROTATE,
        SPAWN_PARTICLE_ON_ITEM,
        GLOW_BORDER_ON_ITEM,

        KEEP_INVENTORY_TRADER_NAME,
        KEEP_INVENTORY_TRADER_TAG,
        KEEP_INVENTORY_TRADER_BUY_ITEM_1_KEY,
        KEEP_INVENTORY_TRADER_BUY_ITEM_1_COUNT,
        KEEP_INVENTORY_TRADER_BUY_ITEM_2_KEY,
        KEEP_INVENTORY_TRADER_BUY_ITEM_2_COUNT,
        KEEP_INVENTORY_TRADER_BOOK_NAME,
        KEEP_INVENTORY_TRADER_DEFAULT_SPAWN_LOC,
    }
}



