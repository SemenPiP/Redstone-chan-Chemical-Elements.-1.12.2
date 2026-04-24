package com.chinaex123.redstone_chemical_elements.register;

import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class ModCreativeTabs {
    private static final Method ITEMSTACK_IS_EMPTY_METHOD = resolveItemStackEmptyMethod();

    public static final CreativeTabs ELEMENT_ITEM_TAB = new CreativeTabs("element_item_tab") {
        @Override
        public ItemStack createIcon() {
            Item icon = ElementItem.getIngot("neodymium");
            return new ItemStack(icon == null ? Items.IRON_INGOT : icon);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            for (String element : ElementCatalog.ELEMENTS) {
                addIfPresent(items, ElementItem.getIngot(element));
                addIfPresent(items, ElementItem.getNugget(element));
                addIfPresent(items, ElementItem.getDust(element));
                addIfPresent(items, ElementItem.getPlate(element));
                addIfPresent(items, ElementItem.getRod(element));
                addIfPresent(items, ElementItem.getWire(element));
                addIfPresent(items, ElementItem.getGear(element));
                addIfPresent(items, ElementItem.getCrushedRaw(element));
                addIfPresent(items, ElementItem.getRaw(element));
            }
        }

    };

    public static final CreativeTabs ELEMENT_BLOCK_TAB = new CreativeTabs("element_block_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ElementBlock.getBlock("neodymium") == null ? Blocks.IRON_BLOCK : ElementBlock.getBlock("neodymium"));
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            for (String element : ElementCatalog.ELEMENTS) {
                addIfPresent(items, ElementBlock.getBlock(element));
                addIfPresent(items, ElementBlock.getRawBlock(element));
                addIfPresent(items, ElementBlock.getOreBlock(element));
            }
        }

    };

    public static final CreativeTabs ELEMENT_STORAGE_TAB = new CreativeTabs("element_storage_tab") {
        @Override
        public ItemStack createIcon() {
            ItemStack icon = ElementFluidRegistry.getFilledBucket("hydrargyrum");
            return isStackEmpty(icon) ? new ItemStack(ElementItem.GAS_CYLINDER) : icon;
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            items.add(new ItemStack(ElementItem.GAS_CYLINDER));

            for (String element : ElementCatalog.ELEMENTS) {
                ItemStack bucket = ElementFluidRegistry.getFilledBucket(element);
                if (!isStackEmpty(bucket)) {
                    items.add(bucket);
                }

                Item cylinder = ElementItem.getCylinder(element);
                if (cylinder != null) {
                    items.add(new ItemStack(cylinder));
                }
            }
        }

    };

    public static final CreativeTabs DERIVED_ITEM_TAB = new CreativeTabs("derived_item_tab") {
        @Override
        public ItemStack createIcon() {
            Item icon = DerivedItem.getFirstItem();
            return new ItemStack(icon == null ? Items.DIAMOND : icon);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
                for (DerivedContentCatalog.DerivedEntry entry : material.getItems()) {
                    addIfPresent(items, DerivedItem.getItem(material.getSlug(), entry.getName()));
                }
            }
        }

    };

    public static final CreativeTabs DERIVED_BLOCK_TAB = new CreativeTabs("derived_block_tab") {
        @Override
        public ItemStack createIcon() {
            Block icon = DerivedBlock.getFirstBlock();
            return new ItemStack(icon == null ? Blocks.DIAMOND_BLOCK : icon);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
                for (DerivedContentCatalog.DerivedEntry entry : material.getBlocks()) {
                    addIfPresent(items, DerivedBlock.getBlockStack(material.getSlug(), entry.getName()));
                }
            }
        }

    };

    public static final CreativeTabs SINGULARITY_TAB = new CreativeTabs("singularity_tab") {
        @Override
        public ItemStack createIcon() {
            Item icon = SingularityItem.getFirstItem();
            return new ItemStack(icon == null ? Items.NETHER_STAR : icon);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            for (Item item : SingularityItem.getAllItems()) {
                addIfPresent(items, item);
            }
        }

    };

    private ModCreativeTabs() {
    }

    private static void addIfPresent(NonNullList<ItemStack> items, Item item) {
        if (item != null) {
            items.add(new ItemStack(item));
        }
    }

    private static void addIfPresent(NonNullList<ItemStack> items, Block block) {
        if (block != null) {
            items.add(new ItemStack(block));
        }
    }

    private static void addIfPresent(NonNullList<ItemStack> items, ItemStack stack) {
        if (!isStackEmpty(stack)) {
            items.add(stack);
        }
    }

    private static boolean isStackEmpty(ItemStack stack) {
        if (stack == null) {
            return true;
        }
        if (ITEMSTACK_IS_EMPTY_METHOD != null) {
            try {
                return ((Boolean) ITEMSTACK_IS_EMPTY_METHOD.invoke(stack)).booleanValue();
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return false;
    }

    private static Method resolveItemStackEmptyMethod() {
        try {
            return ItemStack.class.getMethod("isEmpty");
        } catch (ReflectiveOperationException ignored) {
        }

        try {
            return ItemStack.class.getMethod("func_190926_b");
        } catch (ReflectiveOperationException ignored) {
        }

        return null;
    }
}
