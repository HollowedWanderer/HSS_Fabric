package net.hollowed.hss.common.client;

import java.util.function.Consumer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeData;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.*;

public class ModRenderTypeRegistry extends RenderPhase {

    private static Consumer<LodestoneCompositeStateBuilder> MODIFIER;
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_NO_CULL;

    public ModRenderTypeRegistry(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    /** @deprecated */
    @Deprecated
    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, RenderPhase.ShaderProgram shader, RenderPhase.Transparency transparency, Identifier texture) {
        return createGenericRenderType(name, format, DrawMode.QUADS, shader, transparency, new RenderPhase.Texture(texture, false, false), CULL);
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.DrawMode mode, RenderPhase.ShaderProgram shader, RenderPhase.Transparency transparency, RenderPhase.TextureBase texture, RenderPhase.Cull cull) {
        return createGenericRenderType(name, format, mode, builder().setShaderState(shader).setTransparencyState(transparency).setTextureState(texture).setLightmapState(LIGHTMAP).setCullState(cull));
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.DrawMode mode, LodestoneCompositeStateBuilder builder) {
        return createGenericRenderType(name, format, mode, builder, null);
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.DrawMode mode, LodestoneCompositeStateBuilder builder, ShaderUniformHandler handler) {
        if (MODIFIER != null) {
            MODIFIER.accept(builder);
        }

        LodestoneRenderType type = LodestoneRenderType.createRenderType(name, format, builder.mode != null ? builder.mode : mode, FabricLoader.getInstance().isModLoaded("sodium") ? 2097152 : 256, false, true, builder.build(true));
        RenderHandler.addRenderType(type);
        if (handler != null) {
            applyUniformChanges(type, handler);
        }

        MODIFIER = null;
        return type;
    }

    public static LodestoneRenderType copy(LodestoneRenderType type) {
        return GENERIC.apply(new RenderTypeData(type));
    }

    public static LodestoneRenderType copy(String newName, LodestoneRenderType type) {
        return GENERIC.apply(new RenderTypeData(newName, type));
    }


    public static LodestoneCompositeStateBuilder builder() {
        return (new LodestoneCompositeStateBuilder()).setLightmapState(LIGHTMAP);
    }

    static {
        TRANSPARENT_TEXTURE_NO_CULL = new RenderTypeProvider((token) -> {
            return createGenericRenderType("transparent_texture_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, DrawMode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setLightmapState(RenderPhase.ENABLE_LIGHTMAP).setCullState(RenderPhase.DISABLE_CULLING).setTextureState(token.get()));
        });
    }

    public static class LodestoneCompositeStateBuilder extends RenderLayer.MultiPhaseParameters.Builder {
        protected VertexFormat.DrawMode mode;

        LodestoneCompositeStateBuilder() {
        }

        public LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return this.setShaderState(shaderHolder.getShard());
        }

        public LodestoneCompositeStateBuilder setTextureState(RenderPhase.TextureBase pTextureState) {
            return (LodestoneCompositeStateBuilder)super.texture(pTextureState);
        }

        public LodestoneCompositeStateBuilder setShaderState(RenderPhase.ShaderProgram pShaderState) {
            return (LodestoneCompositeStateBuilder)super.program(pShaderState);
        }

        public LodestoneCompositeStateBuilder setTransparencyState(RenderPhase.Transparency pTransparencyState) {
            return (LodestoneCompositeStateBuilder)super.transparency(pTransparencyState);
        }

        public LodestoneCompositeStateBuilder setCullState(RenderPhase.Cull pCullState) {
            return (LodestoneCompositeStateBuilder)super.cull(pCullState);
        }

        public LodestoneCompositeStateBuilder setLightmapState(RenderPhase.Lightmap pLightmapState) {
            return (LodestoneCompositeStateBuilder)super.lightmap(pLightmapState);
        }
    }
}
