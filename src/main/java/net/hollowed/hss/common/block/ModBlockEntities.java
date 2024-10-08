package net.hollowed.hss.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
import net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HollowedsSwordsSorcery.MOD_ID, "pedestal"),
                    FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new,
                            ModBlocks.PEDESTAL).build());
    public static final BlockEntityType<ResonatingAltarBlockEntity> RESONATING_ALTAR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HollowedsSwordsSorcery.MOD_ID, "resonating_altar"),
                    FabricBlockEntityTypeBuilder.create(ResonatingAltarBlockEntity::new,
                            ModBlocks.RESONATING_ALTAR).build());

    public static void registerBlockEntities() {
        HollowedsSwordsSorcery.LOGGER.info("Registering Block Entities for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
