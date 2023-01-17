package com.cowaug.vanilla.enhancer.mod.particle;

import com.cowaug.vanilla.enhancer.utils.Log;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class CustomParticles {
    private static final HashMap<String, DefaultParticleType> particleHashMap = new HashMap<>();

    public static DefaultParticleType GetParticle(String name) {
        return particleHashMap.getOrDefault(name, null);
    }

    public static void AddParticle(String name, Integer colorInt) {
        int size = particleHashMap.size();
        if (size >= 4) {
            Log.LogError("Max particle added, do nothing");
            return;
        }

        if (particleHashMap.containsKey(name)) {
            Log.LogError("Particle already exist with the same name, do nothing");
            return;
        }

        if (colorInt == null) {
            Log.LogError("Null color for particle " + name);
            return;
        }

        DefaultParticleType newParticle = FabricParticleTypes.simple();

        Registry.register(Registries.PARTICLE_TYPE,
                new Identifier("modid", "particle_" + particleHashMap.size()), newParticle);
        ParticleFactoryRegistry.getInstance().register(newParticle,
                spriteProvider -> new ColorParticle.ColorFactory(spriteProvider, colorInt));

        particleHashMap.put(name, newParticle);
    }
}
