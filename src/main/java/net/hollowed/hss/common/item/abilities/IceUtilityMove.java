package net.hollowed.hss.common.item.abilities;

import net.hollowed.hss.common.item.custom.CharmItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class IceUtilityMove implements CharmItem.Ability {
    @Override
    public void useAbility(World world, PlayerEntity user) {
        user.sendMessage(Text.literal("Ice Utility Move"), false);
    }
}
