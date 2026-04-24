package com.chinaex123.redstone_chemical_elements.register;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

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

    public static void registerOreDictionary() {
        for (String name : ElementCatalog.ELEMENTS) {
            registerOre("ingot", name, ElementItem.getIngot(name));
            registerOre("nugget", name, ElementItem.getNugget(name));
            registerOre("dust", name, ElementItem.getDust(name));
            registerOre("plate", name, ElementItem.getPlate(name));
            registerOre("stick", name, ElementItem.getRod(name));
            registerOre("wire", name, ElementItem.getWire(name));
            registerOre("gear", name, ElementItem.getGear(name));
            registerOre("raw", name, ElementItem.getRaw(name));
            registerOre("crushedRaw", name, ElementItem.getCrushedRaw(name));
            registerOre("block", name, ElementBlock.getBlock(name));
            registerOre("blockRaw", name, ElementBlock.getRawBlock(name));
            registerOre("ore", name, ElementBlock.getOreBlock(name));
        }

        for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
            String materialName = material.getSlug();
            for (DerivedContentCatalog.DerivedEntry entry : material.getItems()) {
                Item item = DerivedItem.getItem(materialName, entry.getName());
                registerDerivedOre(materialName, entry.getName(), item);
            }

            for (DerivedContentCatalog.DerivedEntry entry : material.getBlocks()) {
                ItemStack stack = DerivedBlock.getBlockStack(materialName, entry.getName());
                registerDerivedOre(materialName, entry.getName(), stack);
            }
        }
    }

    public static void registerCrafting(IForgeRegistry<IRecipe> registry) {
        registerSmelting();
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

    private static void registerOre(String prefix, String materialName, Item item) {
        if (item != null) {
            OreDictionary.registerOre(prefix + toOreSuffix(materialName), item);
        }
    }

    private static void registerOre(String prefix, String materialName, Block block) {
        if (block != null) {
            OreDictionary.registerOre(prefix + toOreSuffix(materialName), block);
        }
    }

    private static void registerDerivedOre(String materialName, String entryName, Item item) {
        if (item != null) {
            OreDictionary.registerOre(getOrePrefix(entryName) + toOreSuffix(materialName), item);
        }
    }

    private static void registerDerivedOre(String materialName, String entryName, ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            OreDictionary.registerOre(getOrePrefix(entryName) + toOreSuffix(materialName), stack);
        }
    }

    private static String getOrePrefix(String entryName) {
        String normalized = entryName.toLowerCase(java.util.Locale.ENGLISH);
        if (normalized.endsWith("_ingot")) {
            return "ingot";
        }
        if (normalized.endsWith("_nugget")) {
            return "nugget";
        }
        if (normalized.endsWith("_dust")) {
            return "dust";
        }
        if (normalized.endsWith("_plate")) {
            return "plate";
        }
        if (normalized.endsWith("_rod")) {
            return "stick";
        }
        if (normalized.endsWith("_wire")) {
            return "wire";
        }
        if (normalized.endsWith("_gear")) {
            return "gear";
        }
        if (normalized.endsWith("_ore")) {
            return "ore";
        }
        if (normalized.startsWith("raw_") && normalized.endsWith("_block")) {
            return "blockRaw";
        }
        if (normalized.endsWith("_block")) {
            return "block";
        }
        if (normalized.startsWith("crushed_raw_")) {
            return "crushedRaw";
        }
        if (normalized.startsWith("raw_")) {
            return "raw";
        }
        return "item";
    }

    private static String toOreSuffix(String materialName) {
        String normalized = materialName.toLowerCase(java.util.Locale.ENGLISH);
        StringBuilder builder = new StringBuilder();
        boolean uppercaseNext = true;
        for (int i = 0; i < normalized.length(); i++) {
            char character = normalized.charAt(i);
            if (character == '_' || character == '-' || character == ' ') {
                uppercaseNext = true;
                continue;
            }
            builder.append(uppercaseNext ? Character.toUpperCase(character) : character);
            uppercaseNext = false;
        }
        return builder.toString();
    }
}
