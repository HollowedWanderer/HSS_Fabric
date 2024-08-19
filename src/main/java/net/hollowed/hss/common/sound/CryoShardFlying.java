package net.hollowed.hss.common.sound;

import net.hollowed.hss.common.entity.custom.CryoShardEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class CryoShardFlying extends MovingSoundInstance {
    private final CryoShardEntity projectile;
    private int tickCount;

    public CryoShardFlying(CryoShardEntity projectile) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.projectile = projectile;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1F;
        this.attenuationType = AttenuationType.LINEAR; // Ensure linear attenuation for sound falloff
    }

    @Override
    public void tick() {
        ++this.tickCount;

        // Stop the sound if the projectile is removed or on the ground
        if (projectile.isRemoved() || projectile.isOnGround()) {
            this.setDone();
            return;
        }

        // Update the sound position to follow the projectile
        this.x = projectile.getX();
        this.y = projectile.getY();
        this.z = projectile.getZ();

        // Simplified volume logic
        float distanceToPlayer = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.volume = MathHelper.clamp(1.0F - (distanceToPlayer / 100.0F), 0.0F, 1.0F); // Adjust 100.0F as needed

        // Fade in the sound over the first 20 ticks
        if (this.tickCount < 10) {
            this.volume = MathHelper.clamp(this.tickCount / 20.0F, 0.0F, 1.0F);
        } else if (this.tickCount < 40) {
            this.volume *= (float)(this.tickCount - 20) / 20.0F;
        }

        // Adjust pitch based on speed
        float speed = (float) projectile.getVelocity().lengthSquared();
        if (this.volume > 0.8F) {
            this.pitch = 1.0F + (this.volume - 0.8F);
        } else {
            this.pitch = 1.0F;
        }
    }

    public void done() {
        this.setDone();
    }
}



