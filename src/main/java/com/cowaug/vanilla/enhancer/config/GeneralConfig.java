package com.cowaug.vanilla.enhancer.config;

import com.cowaug.vanilla.enhancer.utils.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralConfig {
    private static final String CONFIG_FILE = "/general.yaml";
    private static Map<String, String> generalConfigYamlMap; // key, value

    public enum ConfigName {
        agingPerTick,
        allowServerOverride,
        obfuscateServer,
        minEntityRotate,
        maxEntityRotate,
        spawnParticleOnItem,
        glowBorderOnItem
    }


    public static void LoadConfig() {
        generalConfigYamlMap = ConfigIo.LoadConfig(CONFIG_FILE);
        if (generalConfigYamlMap == null) {
            CreateDefaultConfig();
        }
        ConfigIo.WriteConfig(CONFIG_FILE, generalConfigYamlMap);
    }

    private static void CreateDefaultConfig() {
        generalConfigYamlMap = new LinkedHashMap<>();
    }

    public static int getAgeIncreasePerTick() {
        return getConfig(ConfigName.agingPerTick, 1);
    }

    public static boolean isAllowServerOverride() {
        return getConfig(ConfigName.allowServerOverride, true);
    }

    public static boolean isObfuscateServer() {
        return getConfig(ConfigName.obfuscateServer, true);
    }

    public static float getMinEntityRotateSpeed(){
        return getConfig(ConfigName.minEntityRotate, 0.8f);
    }

    public static float getMaxEntityRotateSpeed(){
        return getConfig(ConfigName.maxEntityRotate, 1.5f);
    }

    public static boolean isSpawnParticleOnItem(){
        return getConfig(ConfigName.spawnParticleOnItem, true);
    }

    public static boolean isGlowBorderOnItem(){
        return getConfig(ConfigName.glowBorderOnItem, true);
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
            generalConfigYamlMap.put(configName.name(), defaultValue + "");
            ConfigIo.WriteConfig(CONFIG_FILE, generalConfigYamlMap);
            return defaultValue;
        }

        return defaultValue;
    }
}



