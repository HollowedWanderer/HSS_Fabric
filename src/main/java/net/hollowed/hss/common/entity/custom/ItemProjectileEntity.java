package net.hollowed.hss.common.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemProjectileEntity extends PersistentProjectileEntity {
    protected ItemStack stack;

    protected ItemProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, ItemStack stack) {
        super(entityType, world);
        this.stack = stack;
    }

    protected ItemProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, double d, double e, double f, World world, ItemStack stack) {
        super(entityType, d, e, f, world);
        this.stack = stack;
    }

    protected ItemProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, LivingEntity livingEntity, World world, ItemStack stack) {
        super(entityType, livingEntity, world);
        this.stack = stack;
    }

    @Override
    public ItemStack asItemStack() {
        return this.stack.copy();
    }
}
