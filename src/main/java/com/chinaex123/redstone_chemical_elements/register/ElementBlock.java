package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ElementBlock {
    private static final float DEFAULT_HARDNESS = 2.4F;
    private static final float DEFAULT_RESISTANCE = 6.0F;
    private static final float ORE_HARDNESS = 3.0F;
    private static final float ORE_RESISTANCE = 5.0F;
    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Block> RAW_BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Block> ORES = new LinkedHashMap<>();
    private static final List<Block> REGISTERED_BLOCKS = new ArrayList<>();
    private static final List<Item> BLOCK_ITEMS = new ArrayList<>();

    static {
        for (String name : ElementCatalog.ELEMENTS) {
            registerElementBlocks(name, DEFAULT_HARDNESS, DEFAULT_RESISTANCE);
        }
    }

    private ElementBlock() {
    }

    private static void registerElementBlocks(String element, float hardness, float resistance) {
        BLOCKS.put(element, registerStorageBlock(element + "/" + element + "_block", hardness, resistance, SoundType.METAL));
        RAW_BLOCKS.put(element, registerStorageBlock(element + "/raw_" + element + "_block", hardness, resistance, SoundType.STONE));
        ORES.put(element, registerOreBlock(element + "/" + element + "_ore", ORE_HARDNESS, ORE_RESISTANCE));
    }

    private static Block registerStorageBlock(String path, float hardness, float resistance, SoundType soundType) {
        Block block = new ModBlock(path, Material.IRON, hardness, resistance, soundType);
        REGISTERED_BLOCKS.add(block);
        BLOCK_ITEMS.add(new ModItemBlock(block, EnumRarity.EPIC));
        return block;
    }

    private static Block registerOreBlock(String path, float hardness, float resistance) {
        Block block = new ModBlock(path, Material.ROCK, hardness, resistance, SoundType.STONE);
        REGISTERED_BLOCKS.add(block);
        BLOCK_ITEMS.add(new ModItemBlock(block, EnumRarity.EPIC));
        return block;
    }

    private static String translationKey(String path) {
        return RedstonechanChemicalElements.MODID + "." + path.replace('/', '.');
    }

    public static Block getBlock(String elementName) {
        return BLOCKS.get(elementName.toLowerCase());
    }

    public static Block getRawBlock(String elementName) {
        return RAW_BLOCKS.get(elementName.toLowerCase());
    }

    public static Block getOreBlock(String elementName) {
        return ORES.get(elementName.toLowerCase());
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(REGISTERED_BLOCKS.toArray(new Block[0]));
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(BLOCK_ITEMS.toArray(new Item[0]));
    }

    public static List<Item> getAllBlockItems() {
        return Collections.unmodifiableList(BLOCK_ITEMS);
    }

    private static final class ModBlock extends Block {
        private ModBlock(String path, Material material, float hardness, float resistance, SoundType soundType) {
            super(material);
            setRegistryName(RedstonechanChemicalElements.MODID, path);
            setTranslationKey(translationKey(path));
            setCreativeTab(ModCreativeTabs.ELEMENT_BLOCK_TAB);
            setHardness(hardness);
            setResistance(resistance);
            setSoundType(soundType);
            setHarvestLevel("pickaxe", 1);
        }

        @Override
        public EnumBlockRenderType getRenderType(IBlockState state) {
            return EnumBlockRenderType.MODEL;
        }

        @Override
        public boolean isOpaqueCube(IBlockState state) {
            return true;
        }

        @Override
        public boolean isFullCube(IBlockState state) {
            return true;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public BlockRenderLayer getRenderLayer() {
            return BlockRenderLayer.SOLID;
        }
    }

    private static final class ModItemBlock extends ItemBlock {
        private final EnumRarity rarity;

        private ModItemBlock(Block block, EnumRarity rarity) {
            super(block);
            this.rarity = rarity;
            setRegistryName(block.getRegistryName());
            setTranslationKey(block.getTranslationKey());
            setCreativeTab(ModCreativeTabs.ELEMENT_BLOCK_TAB);
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return rarity;
        }
    }
}
