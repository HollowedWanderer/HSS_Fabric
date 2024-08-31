package net.hollowed.hss.common.event;

import io.github.fabricators_of_create.porting_lib.entity.events.ShieldBlockEvent;
import net.hollowed.hss.common.item.custom.VolcanicValor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class BlockShieldEvent implements ShieldBlockEvent.Callback {
    public static void register() {
        ShieldBlockEvent.EVENT.register(new BlockShieldEvent());
    }

    @Override
    public void onShieldBlock(ShieldBlockEvent event) {
        PlayerEntity entity = (PlayerEntity) event.getEntity();
        Item stack = entity.getMainHandStack().getItem();
        if (stack instanceof VolcanicValor) {
            if (!entity.getItemCooldownManager().isCoolingDown(stack)) {
                entity.getItemCooldownManager().set(stack, 5);
                if (((VolcanicValor) stack).charge < 5) {
                    ((VolcanicValor) stack).charge++;
                }
            }
        }
    }
}
