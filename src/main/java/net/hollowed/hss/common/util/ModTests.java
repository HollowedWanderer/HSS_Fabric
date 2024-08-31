package net.hollowed.hss.common.util;

import net.minecraft.block.Blocks;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

public class ModTests {

    @GameTest(templateName = "minecraft:powder_snow")
    public void powderSnowTest(TestContext context) {
        context.pushButton(1, 2, 0);
        context.expectBlockAtEnd(Blocks.POWDER_SNOW, 1, 3, 1);
    }

    @GameTest(templateName = "minecraft:piston_push")
    public void pistonPushTest(TestContext context) {
        context.toggleLever(0, 2, 2);
        context.expectBlockAtEnd(Blocks.DIAMOND_BLOCK, 4, 2, 2);
    }
}
