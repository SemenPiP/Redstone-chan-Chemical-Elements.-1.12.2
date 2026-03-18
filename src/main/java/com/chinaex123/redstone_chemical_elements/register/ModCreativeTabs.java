package com.chinaex123.redstone_chemical_elements.register;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ModCreativeTabs {
    public static final CreativeTabs ELEMENT_ITEM_TAB = new CreativeTabs("element_item_tab") {
        @Override
        public ItemStack createIcon() {
            Item icon = ElementItem.getIngot("neodymium");
            return new ItemStack(icon == null ? Items.IRON_INGOT : icon);
        }
    };

    public static final CreativeTabs ELEMENT_BLOCK_TAB = new CreativeTabs("element_block_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ElementBlock.getBlock("neodymium") == null ? Blocks.IRON_BLOCK : ElementBlock.getBlock("neodymium"));
        }
    };

    private ModCreativeTabs() {
    }
}
