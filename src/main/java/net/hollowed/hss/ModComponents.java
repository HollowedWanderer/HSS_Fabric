package net.hollowed.hss;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.hollowed.hss.common.networking.BooleanComponent;
import net.hollowed.hss.common.networking.StringComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<BooleanComponent> BOUND_INVENTORY =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "bound_inventory")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> UTILITY_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "utility_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> BACKSLOT_SWITCH =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "backslot_switch")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> GROUND_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "ground_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> HEAVY_GROUND_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "heavy_ground_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> MOVEMENT_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "movement_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> NORMAL_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "normal_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> HEAVY_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "heavy_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> GRAB_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "grab_checker")), BooleanComponent.class);

    public static final ComponentKey<BooleanComponent> MELEE_CHECKER =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "melee_checker")), BooleanComponent.class);

    public static final ComponentKey<StringComponent> BOUND =
            ComponentRegistry.getOrCreate(Objects.requireNonNull(Identifier.of("hss", "fire_bound")), StringComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(BOUND_INVENTORY, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("bound_inventory");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("bound_inventory", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(UTILITY_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("utility_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("utility_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(BACKSLOT_SWITCH, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("backslot_switch");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("backslot_switch", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(GROUND_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("ground_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("ground_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(HEAVY_GROUND_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("heavy_ground_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("heavy_ground_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(MOVEMENT_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("movement_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("movement_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(NORMAL_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("normal_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("normal_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(HEAVY_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("heavy_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("heavy_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(GRAB_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("grab_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("grab_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(MELEE_CHECKER, player -> new BooleanComponent() {
            private boolean value = false;

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getBoolean("melee_checker");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putBoolean("melee_checker", value);
            }

            @Override
            public boolean getValue() {
                return value;
            }

            @Override
            public void setValue(boolean value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(BOUND, player -> new StringComponent() {
            private String value = "";

            @Override
            public void readFromNbt(@NotNull NbtCompound tag) {
                value = tag.getString("bound");
            }

            @Override
            public void writeToNbt(@NotNull NbtCompound tag) {
                tag.putString("bound", value);
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public void setValue(String value) {
                this.value = value;
            }
        }, RespawnCopyStrategy.CHARACTER);
    }
}