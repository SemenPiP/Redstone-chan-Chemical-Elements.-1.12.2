package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ElementItem {
    private static final EnumRarity DEFAULT_RARITY = EnumRarity.EPIC;
    private static final Map<String, Item> INGOTS = new LinkedHashMap<>();
    private static final Map<String, Item> NUGGETS = new LinkedHashMap<>();
    private static final Map<String, Item> DUSTS = new LinkedHashMap<>();
    private static final Map<String, Item> PLATES = new LinkedHashMap<>();
    private static final Map<String, Item> RODS = new LinkedHashMap<>();
    private static final Map<String, Item> WIRES = new LinkedHashMap<>();
    private static final Map<String, Item> GEARS = new LinkedHashMap<>();
    private static final Map<String, Item> CRUSHED_RAW = new LinkedHashMap<>();
    private static final Map<String, Item> RAW = new LinkedHashMap<>();
    private static final Map<String, Item> CYLINDERS = new LinkedHashMap<>();
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Item GAS_CYLINDER = registerItem("gas_cylinder", EnumRarity.COMMON);

    static {
        for (String name : ElementCatalog.ELEMENTS) {
            INGOTS.put(name, registerItem(name + "/" + name + "_ingot", DEFAULT_RARITY));
            NUGGETS.put(name, registerItem(name + "/" + name + "_nugget", DEFAULT_RARITY));
            DUSTS.put(name, registerItem(name + "/" + name + "_dust", DEFAULT_RARITY));
            PLATES.put(name, registerItem(name + "/" + name + "_plate", DEFAULT_RARITY));
            RODS.put(name, registerItem(name + "/" + name + "_rod", DEFAULT_RARITY));
            WIRES.put(name, registerItem(name + "/" + name + "_wire", DEFAULT_RARITY));
            GEARS.put(name, registerItem(name + "/" + name + "_gear", DEFAULT_RARITY));
            CRUSHED_RAW.put(name, registerItem(name + "/crushed_raw_" + name, DEFAULT_RARITY));
            RAW.put(name, registerItem(name + "/raw_" + name, DEFAULT_RARITY));
        }

        for (String name : ElementCatalog.GAS_ELEMENTS) {
            CYLINDERS.put(name, registerItem(name + "/" + name + "_cylinder", DEFAULT_RARITY));
        }
    }

    private ElementItem() {
    }

    private static Item registerItem(String path, EnumRarity rarity) {
        Item item = new ModItem(path, rarity);
        ITEMS.add(item);
        return item;
    }

    private static boolean isStorageItemPath(String path) {
        return "gas_cylinder".equals(path) || path.endsWith("_cylinder");
    }

    private static String translationKey(String path) {
        return RedstonechanChemicalElements.MODID + "." + path.replace('/', '.');
    }

    public static Item getIngot(String elementName) {
        return INGOTS.get(elementName.toLowerCase());
    }

    public static Item getNugget(String elementName) {
        return NUGGETS.get(elementName.toLowerCase());
    }

    public static Item getDust(String elementName) {
        return DUSTS.get(elementName.toLowerCase());
    }

    public static Item getPlate(String elementName) {
        return PLATES.get(elementName.toLowerCase());
    }

    public static Item getRod(String elementName) {
        return RODS.get(elementName.toLowerCase());
    }

    public static Item getWire(String elementName) {
        return WIRES.get(elementName.toLowerCase());
    }

    public static Item getGear(String elementName) {
        return GEARS.get(elementName.toLowerCase());
    }

    public static Item getRaw(String elementName) {
        return RAW.get(elementName.toLowerCase());
    }

    public static Item getCrushedRaw(String elementName) {
        return CRUSHED_RAW.get(elementName.toLowerCase());
    }

    public static Item getCylinder(String elementName) {
        return CYLINDERS.get(elementName.toLowerCase());
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(ITEMS.toArray(new Item[0]));
    }

    public static List<Item> getAllItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    private static final class ModItem extends Item {
        private final EnumRarity rarity;

        private ModItem(String path, EnumRarity rarity) {
            this.rarity = rarity;
            setRegistryName(RedstonechanChemicalElements.MODID, path);
            setTranslationKey(translationKey(path));
            if (!isStorageItemPath(path)) {
                setCreativeTab(ModCreativeTabs.ELEMENT_ITEM_TAB);
            }
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return rarity;
        }
    }
}
