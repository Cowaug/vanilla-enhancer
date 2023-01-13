package com.cowaug.vanilla.enhancer.mod.rarity;

import net.minecraft.util.Formatting;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomRarity {
    private String name = "Unclassified";
    private String formatCode = String.valueOf(Formatting.WHITE.getCode());
    private int depsawnTicks = 6000;
    private boolean glowing = false;
    private boolean isDefault;

    public CustomRarity() {
        isDefault = true;
    }

    public CustomRarity(String initString) {
        isDefault = initString.equals(toString());

        String[] values = initString.split("_");
        switch (values.length) {
            case 4:
                glowing = Boolean.parseBoolean(values[3]);
            case 3:
                depsawnTicks = Integer.parseInt(values[2]);
            case 2:
                formatCode = values[1];
            case 1:
                name = values[0];
        }
    }

    public CustomRarity(String name, int despawnTicks, boolean glowing, Formatting... format) {
        this.name = name;
        this.depsawnTicks = despawnTicks;
        this.glowing = glowing;

        StringBuilder formats = new StringBuilder();
        for (Formatting formatting : format) {
            formats.append(formatting.getCode());
        }
        this.formatCode = formats.toString();
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Integer getColor() {
        char color = formatCode.replaceAll("[^0-9a-f]", "").charAt(0);
        Formatting formatting = Formatting.byCode(color);
        if (formatting != null) {
            return formatting.getColorValue();
        } else {
            return null;
        }
    }

    public Formatting[] getFormats() {
        List<Formatting> formattingArr = Lists.newArrayList();
        for (char c : formatCode.toCharArray()) {
            Formatting formatting = Formatting.byCode(c);
            if (formatting != null) {
                formattingArr.add(formatting);
            }
        }

        if (formattingArr.size() == 0) {
            formattingArr.add(Formatting.WHITE);
        }

        return formattingArr.toArray(new Formatting[0]);
    }

    public int getDepsawnTicks() {
        return depsawnTicks;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return name + "_" + formatCode + "_" + depsawnTicks + "_" + glowing;
    }
}
