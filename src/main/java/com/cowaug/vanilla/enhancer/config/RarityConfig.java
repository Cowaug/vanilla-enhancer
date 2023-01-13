package com.cowaug.vanilla.enhancer.config;

import com.cowaug.vanilla.enhancer.mod.rarity.CustomRarity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

import static com.cowaug.vanilla.enhancer.Main.serverPath;

public class RarityConfig {
    public static final CustomRarity UNCLASSIFIED = new CustomRarity();
    public static final CustomRarity MULTIPLE_CONFIG = new CustomRarity("Hmm!?_0m_6000_true");
    private static final String CONFIG_FILE = "/rarity.yaml";
    private static Map<String, List<String>> rarityYamlMap; // rarity tier, item identifier
    private static final Map<String, CustomRarity> itemRarityMap = new LinkedHashMap<>(); // item identifier, rarity tier
    private static final Map<String, CustomRarity> itemRarityOverrideMap = new LinkedHashMap<>(); // item identifier, override rarity tier
    private static final Map<String, CustomRarity> rarityOverrideMap = new LinkedHashMap<>();// string, override rarity tier

    public static DumperOptions dumperOptions;

    static {
        dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setIndent(3);
        dumperOptions.setIndicatorIndent(2);
    }

    public static void LoadConfig() {
        resetAll();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(serverPath + CONFIG_FILE);
            Yaml yaml = new Yaml();
            rarityYamlMap = yaml.load(inputStream);
            System.out.println(rarityYamlMap);
        } catch (FileNotFoundException e) {
            System.out.println("[Vanilla Enhancer Rarity] Config not found, creating new one...");
        }

        if(rarityYamlMap == null){
            CreateDefaultConfig();
            WriteConfig();
        }

        for (Identifier id : Registries.ITEM.getIds().stream().sorted().toList()) {
            AddRemainsItemToConfig(id.getPath());
        }
        WriteConfig();

        rarityYamlMap.forEach((k, l) -> {
                    CustomRarity customRarity = new CustomRarity(k);
                    l.forEach(identifier -> {
                        if (itemRarityMap.containsKey(identifier)) {
                            System.out.println("Duplicate key: " + identifier);
                            itemRarityMap.remove(identifier);
                            itemRarityMap.put(identifier, MULTIPLE_CONFIG);
                        }
                        itemRarityMap.putIfAbsent(identifier, customRarity);
                    });
                }
        );

        // itemRarityMap.forEach((k, v) -> System.out.println("[Vanilla Enhancer Rarity] Key [" + k + "] [" + v + "]"));
    }

    public static void WriteConfig() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(serverPath + CONFIG_FILE);

            Yaml yaml = new Yaml(dumperOptions);
            yaml.dump(rarityYamlMap, writer);
            System.out.println("[Vanilla Enhancer Rarity] Saved config.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void CreateDefaultConfig() {
        rarityYamlMap = new LinkedHashMap<>();

//        List<String> colorComment = Lists.newArrayList();
//        for (Formatting f : Formatting.values()) {
//            colorComment.add(f.getName() + ": " + f.getCode());
//        }

        // default rarity
        List<CustomRarity> defaultRarity = List.of(
                new CustomRarity("Everywhere", 1200, false, Formatting.GRAY),
                new CustomRarity("Common", 6000, false, Formatting.WHITE),
                new CustomRarity("Uncommon", 12000, false, Formatting.GREEN),
                new CustomRarity("Rare", 1728000, true, Formatting.AQUA),
                new CustomRarity("Legendary", -1, true, Formatting.GOLD),
                new CustomRarity("Mythic", -1, true, Formatting.DARK_PURPLE),
                new CustomRarity("Ultimate", -1, true, Formatting.DARK_RED)
        );

        List<String> everywhereItems = new ArrayList<>(List.of("some_everywhere_identifier"));
        List<String> commonItems = new ArrayList<>(List.of("some_common_identifier"));
        List<String> uncommonItems = new ArrayList<>(List.of("some_uncommon_identifier"));
        List<String> rareItems = new ArrayList<>(List.of("some_rare_identifier"));
        List<String> legendaryItems = new ArrayList<>(List.of(
                "creeper_banner_pattern",
                "skull_banner_pattern",
                "experience_bottle",
                "dragon_breath",
                "elytra",
                "skeleton_skull",
                "wither_skeleton_skull",
                "zombie_head",
                "player_head",
                "creeper_head",
                "dragon_head",
                "piglin_head",
                "skeleton_wall_skull",
                "wither_skeleton_wall_skull",
                "zombie_wall_head",
                "player_wall_head",
                "creeper_wall_head",
                "dragon_wall_head",
                "piglin_wall_head",
                "heart_of_the_sea",
                "nether_star",
                "totem_of_undying",
                "enchanted_book",
                "ancient_debris",
                "netherite_scrap",
                "netherite_ingot",
                "netherite_block",
                "netherite_sword",
                "netherite_pickaxe",
                "netherite_axe",
                "netherite_shovel",
                "netherite_hoe",
                "netherite_helmet",
                "netherite_chestplate",
                "netherite_leggings",
                "netherite_boots"

        ));
        List<String> mythicItems = new ArrayList<>(List.of(
                "golden_apple",
                "music_disc_13",
                "music_disc_cat",
                "music_disc_blocks",
                "music_disc_chirp",
                "music_disc_far",
                "music_disc_mall",
                "music_disc_mellohi",
                "music_disc_stal",
                "music_disc_strad",
                "music_disc_ward",
                "music_disc_11",
                "music_disc_wait",
                "music_disc_otherside",
                "music_disc_pigstep",
                "music_disc_5",
                "beacon",
                "conduit",
                "end_crystal"
        ));
        List<String> ultimateItems = new ArrayList<>(List.of(
                "mojang_banner_pattern",
                "command_block",
                "chain_command_block",
                "repeating_command_block",
                "dragon_egg",
                "structure_block",
                "structure_void",
                "jigsaw",
                "light",
                "barrier",
                "command_block_minecart",
                "debug_stick",
                "knowledge_book",
                "enchanted_golden_apple"
        ));

        rarityYamlMap.put(defaultRarity.get(0).toString(), everywhereItems);
        rarityYamlMap.put(defaultRarity.get(1).toString(), commonItems);
        rarityYamlMap.put(defaultRarity.get(2).toString(), uncommonItems);
        rarityYamlMap.put(defaultRarity.get(3).toString(), rareItems);
        rarityYamlMap.put(defaultRarity.get(4).toString(), legendaryItems);
        rarityYamlMap.put(defaultRarity.get(5).toString(), mythicItems);
        rarityYamlMap.put(defaultRarity.get(6).toString(), ultimateItems);
    }

    private static void AddRemainsItemToConfig(String identifier) {
        if (rarityYamlMap.values().stream().noneMatch(l -> l.contains(identifier))) {
            List<String> list = rarityYamlMap.getOrDefault(UNCLASSIFIED.toString(), null);
            if (list == null) {
                rarityYamlMap.put(UNCLASSIFIED.toString(), new ArrayList<>(List.of(identifier)));
            } else {
                list.add(identifier);
            }
        }
    }

    public static Text getRarityText(String identifier, boolean useDefaultFormat) {
        CustomRarity rarity = getRarity(identifier);

        List<Formatting> formattingArr = Lists.newArrayList();

        if (useDefaultFormat) {
            formattingArr.add(Formatting.DARK_GRAY);
            formattingArr.add(Formatting.ITALIC);
        } else {
            formattingArr.addAll(Arrays.stream(rarity.getFormats()).toList());
        }

        if (rarity.isDefault()) {
            formattingArr.add(Formatting.OBFUSCATED);
        }

        return Text.empty().append(rarity.getName()).formatted(formattingArr.toArray(new Formatting[0]));
    }

    public static CustomRarity getRarity(String identifier) {
//        System.out.println("[Vanilla Enhancer Rarity] Get key: [" + identifier + "]");
        return itemRarityOverrideMap.getOrDefault(identifier, itemRarityMap.getOrDefault(identifier, UNCLASSIFIED));
    }

    public static void addOverride(String identifier, String customRarityStr) {
        if (!rarityOverrideMap.containsKey(customRarityStr)) {
            rarityOverrideMap.put(customRarityStr, new CustomRarity(customRarityStr));
        }
        itemRarityOverrideMap.remove(identifier);
        itemRarityOverrideMap.put(identifier, rarityOverrideMap.getOrDefault(customRarityStr, UNCLASSIFIED));
//        System.out.println("[Vanilla Enhancer Rarity] Added server override: [" + identifier + "] [" + customRarityStr + "]");
    }

    public static void resetAll() {
        itemRarityMap.clear();
        rarityOverrideMap.clear();
        itemRarityOverrideMap.clear();
    }
}



