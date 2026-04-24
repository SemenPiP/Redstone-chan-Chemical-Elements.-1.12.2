package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public final class SingularityItem {
    private static final EnumRarity DEFAULT_RARITY = EnumRarity.EPIC;

    private static final Map<String, Item> ITEMS = new LinkedHashMap<>();
    private static final List<Item> REGISTERED_ITEMS = new ArrayList<>();

    private static boolean bootstrapped;

    private SingularityItem() {
    }

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        for (String element : ElementCatalog.METAL_ELEMENTS) {
            registerSingularity(element);
        }

        DerivedContentCatalog.bootstrap();
        for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
            registerSingularity(material.getSlug());
        }

        bootstrapped = true;
    }

    public static Item getItem(String materialName) {
        bootstrap();
        return ITEMS.get(normalize(materialName));
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

    private static void registerSingularity(String materialName) {
        String normalized = normalize(materialName);
        if (normalized.isEmpty() || ITEMS.containsKey(normalized)) {
            return;
        }

        Item item = new ModItem("singularity/" + normalized + "_singularity", DEFAULT_RARITY);
        ITEMS.put(normalized, item);
        REGISTERED_ITEMS.add(item);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ENGLISH);
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
            setCreativeTab(ModCreativeTabs.SINGULARITY_TAB);
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return rarity;
        }
    }
}
