package net.hollowed.hss.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HollowedsSwordsSorcery.MOD_ID, "gem_polishing_be"),
                    FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new,
                            ModBlocks.PEDESTAL).build());

    public static void registerBlockEntities() {
        HollowedsSwordsSorcery.LOGGER.info("Registering Block Entities for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
