package net.hollowed.hss.common.block;

import net.hollowed.hss.common.entity.ModEntities;
import net.hollowed.hss.common.entity.custom.CryoShardEntity;
import net.hollowed.hss.common.item.ModItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public interface ModDispenserBehavior {
    static void registerDefaults() {
        DispenserBlock.registerBehavior(ModItems.CRYO_SHARD, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                // Use the constructor that accepts the position
                CryoShardEntity cryoShardEntity = new CryoShardEntity(ModEntities.CRYO_SHARD, position.getX(), position.getY(), position.getZ(), world, new ItemStack(ModItems.CRYO_SHARD));

                // Set pickup type if needed
                cryoShardEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;

                return cryoShardEntity;
            }
        });
    }
}


