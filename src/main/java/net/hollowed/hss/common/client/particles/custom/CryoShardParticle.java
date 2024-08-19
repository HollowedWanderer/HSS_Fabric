package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

public class CryoShardParticle extends SpriteBillboardParticle {
    protected CryoShardParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.98F; // Slow down movement over time
        this.gravityStrength = 0.1F; // Higher gravity for more realistic fall
        this.scale *= 2.5F;
        this.maxAge = 20;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;

        this.setSprite(spriteSet.getSprite(this.random.nextInt(3), 3));
    }

    @Override
    public void tick() {
        this.velocityY -= gravityStrength; // Apply gravity

        if (this.onGround()) { // Check if the particle is on the ground
            this.velocityY = 0; // Stop downward movement
            this.velocityX *= 0.7F; // Apply friction when on the ground
            this.velocityZ *= 0.7F;
        }

        super.tick(); // Call the parent tick method
        fadeOut(); // Handle fade-out logic
    }

    private boolean onGround() {
        // Check if the particle is colliding with a solid block beneath it
        return this.onGround || this.world.getBlockState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).isSolidBlock(this.world, new BlockPos((int) this.x, (int) this.y, (int) this.z));
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CryoShardParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}