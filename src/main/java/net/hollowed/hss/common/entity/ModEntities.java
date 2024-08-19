package net.hollowed.hss.common.entity;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.entity.custom.CryoShardEntity;
import net.hollowed.hss.common.entity.custom.FragileCryoShardEntity;
import net.hollowed.hss.common.entity.custom.SpiralFragileCryoShardEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {

    public static final EntityType<CryoShardEntity> CRYO_SHARD = registerEntity(
            "cryo_shard",
            SpawnGroup.MISC,
            CryoShardEntity::new,
            EntityDimensions.fixed(0.40F, 0.40F),
            false
    );

    public static final EntityType<FragileCryoShardEntity> FRAGILE_CRYO_SHARD = registerEntity(
            "fragile_cryo_shard",
            SpawnGroup.MISC,
            FragileCryoShardEntity::new,
            EntityDimensions.fixed(0.40F, 0.40F),
            false
    );

    public static final EntityType<SpiralFragileCryoShardEntity> SPIRAL_FRAGILE_CRYO_SHARD = registerEntity(
            "spiral_fragile_cryo_shard",
            SpawnGroup.MISC,
            SpiralFragileCryoShardEntity::new,
            EntityDimensions.fixed(0.40F, 0.40F),
            false
    );

    public static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory, EntityDimensions dimensions, boolean fireImmune) {
        EntityType.Builder<T> builder = EntityType.Builder.create(factory, group)
                .setDimensions(dimensions.width, dimensions.height);
        if (fireImmune) {
            builder = builder.makeFireImmune();
        }
        return Registry.register(Registries.ENTITY_TYPE, HollowedsSwordsSorcery.MOD_ID + ":" + name, builder.build(name));
    }
}
