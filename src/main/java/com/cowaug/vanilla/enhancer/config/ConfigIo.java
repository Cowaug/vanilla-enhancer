package com.cowaug.vanilla.enhancer.config;

import com.cowaug.vanilla.enhancer.utils.Log;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.cowaug.vanilla.enhancer.Main.serverPath;

public class ConfigIo {
    public static DumperOptions dumperOptions;

    static {
        dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setIndent(3);
        dumperOptions.setIndicatorIndent(2);
    }

    public static <K, V> void WriteConfig(String configFilename, Map<K, V> objectMap) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(serverPath + configFilename);

            Yaml yaml = new Yaml(dumperOptions);
            yaml.dump(objectMap, writer);
            Log.LogInfo("Saved " + configFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static <K, V> Map<K, V> LoadConfig(String configFilename) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(serverPath + configFilename);
            Yaml yaml = new Yaml();
            Map<K, V> loadedData = yaml.load(inputStream);
            Log.LogDebug(loadedData.toString());
            return loadedData;
        } catch (FileNotFoundException e) {
            Log.LogInfo(configFilename + " not found");
        }
        return null;
    }

    public static <K, V> Map<K, V> LoadConfigFromUrl(String url) {
        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();
            Yaml yaml = new Yaml();
            Map<K, V> loadedData = yaml.load(inputStream);
            Log.LogDebug(loadedData.toString());
            return loadedData;
        } catch (MalformedURLException e) {
            Log.LogWarn(url + " not found");
        } catch (IOException e) {
            Log.LogWarn("IO error when loading config from " + url);
        }
        return null;
    }

}
