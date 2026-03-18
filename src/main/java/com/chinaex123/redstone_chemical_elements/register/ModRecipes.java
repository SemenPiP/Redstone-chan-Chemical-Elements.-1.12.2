package com.chinaex123.redstone_chemical_elements.register;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModRecipes {
    private static final float DEFAULT_EXPERIENCE = 0.5F;

    private ModRecipes() {
    }

    public static void registerSmelting() {
        for (String name : ElementCatalog.ELEMENTS) {
            Item ingot = ElementItem.getIngot(name);
            if (ingot == null) {
                continue;
            }

            registerSmelting(ElementItem.getRaw(name), new ItemStack(ingot), DEFAULT_EXPERIENCE);
            registerSmelting(ElementBlock.getOreBlock(name), new ItemStack(ingot), DEFAULT_EXPERIENCE);

            Block storageBlock = ElementBlock.getBlock(name);
            Block rawBlock = ElementBlock.getRawBlock(name);
            if (storageBlock != null && rawBlock != null) {
                registerSmelting(rawBlock, new ItemStack(storageBlock), DEFAULT_EXPERIENCE);
            }
        }
    }

    private static void registerSmelting(Block input, ItemStack output, float experience) {
        if (input != null) {
            GameRegistry.addSmelting(input, output, experience);
        }
    }

    private static void registerSmelting(Item input, ItemStack output, float experience) {
        if (input != null) {
            GameRegistry.addSmelting(input, output, experience);
        }
    }
}
