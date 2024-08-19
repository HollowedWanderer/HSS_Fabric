package net.hollowed.hss.common.item;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void register() {
        ModelPredicateProviderRegistry.register(new Identifier("hss", "shattered"),
            (stack, world, entity, seed) -> {
                if (stack.hasNbt()) {
                    assert stack.getNbt() != null;
                    if (stack.getNbt().getBoolean("Shattered")) {
                        return 1.0F;
                    }
                }
                return 0.0F;
            }
        );
    }
}
