package com.cowaug.vanilla.enhancer.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralConfig {
    private static final String CONFIG_FILE = "/general.yaml";
    private static Map<String, Object> generalConfigYamlMap; // key, value

    public enum ConfigName {
        scaleEntityRotationWithDespawnTime
    }


    public static void LoadConfig() {
        generalConfigYamlMap = ConfigHelper.LoadConfig(CONFIG_FILE);
        if (generalConfigYamlMap == null) {
            CreateDefaultConfig();
        }
        ConfigHelper.WriteConfig(CONFIG_FILE, generalConfigYamlMap);
    }

    private static void CreateDefaultConfig() {
        generalConfigYamlMap = new LinkedHashMap<>();
    }

}



