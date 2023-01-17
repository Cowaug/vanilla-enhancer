package com.cowaug.vanilla.enhancer.mod.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class ColorParticle extends SpriteBillboardParticle {
    static final Random RANDOM = Random.create();
    private final SpriteProvider spriteProvider;

    ColorParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96F;
        this.field_28787 = true;
        this.spriteProvider = spriteProvider;
        this.scale *= 0.75F;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getBrightness(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class ColorFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private final Color color;

        public ColorFactory(SpriteProvider spriteProvider, int rgbColor) {
            this.color = new Color(rgbColor).brighter();
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColorParticle colorParticle = new ColorParticle(clientWorld, d, e, f, 0.5D - ColorParticle.RANDOM.nextDouble(), h, 0.5D - ColorParticle.RANDOM.nextDouble(), this.spriteProvider);
            colorParticle.setColor(
                    this.color.getRed() / 255f,
                    this.color.getGreen() / 255f,
                    this.color.getBlue() / 255f
            );

            colorParticle.velocityY *= 0.20000000298023224D;
            if (g == 0.0D && i == 0.0D) {
                colorParticle.velocityX *= 0.10000000149011612D;
                colorParticle.velocityZ *= 0.10000000149011612D;
            }

            colorParticle.setMaxAge((int) (8.0D / (clientWorld.random.nextDouble() * 0.8D + 0.2D)));
            return colorParticle;
        }
    }
}
