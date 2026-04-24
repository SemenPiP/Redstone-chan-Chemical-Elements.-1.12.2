package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public final class DerivedItem {
    private static final EnumRarity DEFAULT_RARITY = EnumRarity.RARE;

    private static final Map<String, Item> ITEMS = new LinkedHashMap<>();
    private static final List<Item> REGISTERED_ITEMS = new ArrayList<>();

    private static boolean bootstrapped;

    private DerivedItem() {
    }

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        DerivedContentCatalog.bootstrap();
        for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
            for (DerivedContentCatalog.DerivedEntry entry : material.getItems()) {
                String path = "derived/" + material.getSlug() + "/" + entry.getName();
                Item item = new ModItem(path, DEFAULT_RARITY);
                ITEMS.put(key(material.getSlug(), entry.getName()), item);
                REGISTERED_ITEMS.add(item);
            }
        }

        bootstrapped = true;
    }

    public static Item getItem(String materialSlug, String itemName) {
        bootstrap();
        return ITEMS.get(key(materialSlug, itemName));
    }

    public static Item getFirstItem() {
        bootstrap();
        return REGISTERED_ITEMS.isEmpty() ? null : REGISTERED_ITEMS.get(0);
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        bootstrap();
        registry.registerAll(REGISTERED_ITEMS.toArray(new Item[0]));
    }

    public static List<Item> getAllItems() {
        bootstrap();
        return Collections.unmodifiableList(REGISTERED_ITEMS);
    }

    private static String key(String materialSlug, String itemName) {
        return materialSlug + ":" + itemName;
    }

    private static String translationKey(String path) {
        return RedstonechanChemicalElements.MODID + "." + path.replace('/', '.');
    }

    private static final class ModItem extends Item {
        private final EnumRarity rarity;

        private ModItem(String path, EnumRarity rarity) {
            this.rarity = rarity;
            setRegistryName(RedstonechanChemicalElements.MODID, path);
            setTranslationKey(translationKey(path));
            setCreativeTab(ModCreativeTabs.DERIVED_ITEM_TAB);
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return rarity;
        }
    }
}
