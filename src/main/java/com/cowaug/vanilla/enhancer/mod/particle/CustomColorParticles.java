package com.cowaug.vanilla.enhancer.mod.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class CustomColorParticles {
    private static final HashMap<String, DefaultParticleType> particleHashMap = new HashMap<>();

    public static DefaultParticleType GetParticle(String colorName) {
        return particleHashMap.getOrDefault(colorName, null);
    }

    public static void AddParticle(String colorName, Integer colorInt) {
        if (colorInt == null) {
            return;
        }

        DefaultParticleType newParticle = FabricParticleTypes.simple();

        Registry.register(Registries.PARTICLE_TYPE,
                new Identifier("modid", "particle_" + particleHashMap.size()), newParticle);
        ParticleFactoryRegistry.getInstance().register(newParticle,
                spriteProvider -> new ColorParticle.ColorFactory(spriteProvider, colorInt));

        particleHashMap.put(colorName, newParticle);
    }

    public static void init() {
        for (Formatting format : Formatting.values()) {
            AddParticle(format.getName(), format.getColorValue());
        }
    }
}
