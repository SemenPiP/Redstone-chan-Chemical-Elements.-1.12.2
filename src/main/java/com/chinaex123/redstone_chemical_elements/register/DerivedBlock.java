package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public final class DerivedBlock {
    private static final int VARIANTS_PER_GROUP = 16;
    private static final PropertyInteger VARIANT = createVariantProperty();

    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Block> RAW_BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Block> ORES = new LinkedHashMap<>();
    private static final Map<String, Item> BLOCK_ITEMS = new LinkedHashMap<>();
    private static final Map<String, Integer> BLOCK_ITEM_METAS = new LinkedHashMap<>();
    private static final List<Block> REGISTERED_BLOCKS = new ArrayList<>();
    private static final List<Item> REGISTERED_BLOCK_ITEMS = new ArrayList<>();
    private static final List<ModelRegistration> MODEL_REGISTRATIONS = new ArrayList<>();

    private static boolean bootstrapped;

    private DerivedBlock() {
    }

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        DerivedContentCatalog.bootstrap();
        List<DerivedContentCatalog.DerivedMaterial> materials = DerivedContentCatalog.getMaterials();
        registerCategory(materials, Category.STORAGE);
        registerCategory(materials, Category.RAW);
        registerCategory(materials, Category.ORE);
        bootstrapped = true;
    }

    public static Block getBlock(String materialSlug, String blockName) {
        bootstrap();
        String normalizedSlug = normalize(materialSlug);
        String normalizedBlockName = normalize(blockName);
        if (normalizedBlockName.endsWith("_ore")) {
            return ORES.get(normalizedSlug);
        }
        if (normalizedBlockName.startsWith("raw_")) {
            return RAW_BLOCKS.get(normalizedSlug);
        }
        return BLOCKS.get(normalizedSlug);
    }

    public static Item getBlockItem(String materialSlug, String blockName) {
        bootstrap();
        return BLOCK_ITEMS.get(key(materialSlug, blockName));
    }

    public static ItemStack getBlockStack(String materialSlug, String blockName) {
        Item item = getBlockItem(materialSlug, blockName);
        if (item == null) {
            return emptyItemStack();
        }
        Integer meta = BLOCK_ITEM_METAS.get(key(materialSlug, blockName));
        return new ItemStack(item, 1, meta == null ? 0 : meta);
    }

    public static Block getFirstBlock() {
        bootstrap();
        return REGISTERED_BLOCKS.isEmpty() ? null : REGISTERED_BLOCKS.get(0);
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        bootstrap();
        if (!REGISTERED_BLOCKS.isEmpty()) {
            registry.registerAll(REGISTERED_BLOCKS.toArray(new Block[0]));
        }
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        bootstrap();
        if (!REGISTERED_BLOCK_ITEMS.isEmpty()) {
            registry.registerAll(REGISTERED_BLOCK_ITEMS.toArray(new Item[0]));
        }
    }

    public static List<Item> getAllBlockItems() {
        bootstrap();
        return Collections.unmodifiableList(REGISTERED_BLOCK_ITEMS);
    }

    public static List<ModelRegistration> getModelRegistrations() {
        bootstrap();
        return Collections.unmodifiableList(MODEL_REGISTRATIONS);
    }

    private static void registerCategory(List<DerivedContentCatalog.DerivedMaterial> materials, Category category) {
        List<VariantEntry> pending = new ArrayList<>(VARIANTS_PER_GROUP);
        int groupIndex = 0;

        for (DerivedContentCatalog.DerivedMaterial material : materials) {
            String blockName = findBlockName(material, category);
            if (blockName == null) {
                continue;
            }

            String slug = normalize(material.getSlug());
            pending.add(
                new VariantEntry(
                    slug,
                    blockName,
                    translationKey("derived/" + slug + "/" + blockName),
                    RedstonechanChemicalElements.MODID + ":derived/" + slug + "/" + blockName
                )
            );

            if (pending.size() == VARIANTS_PER_GROUP) {
                registerGroup(category, groupIndex++, pending);
                pending = new ArrayList<>(VARIANTS_PER_GROUP);
            }
        }

        if (!pending.isEmpty()) {
            registerGroup(category, groupIndex, pending);
        }
    }

    private static void registerGroup(Category category, int groupIndex, List<VariantEntry> variants) {
        String registryPath = category.registryPath(groupIndex);
        VariantBlock block = new VariantBlock(
            registryPath,
            category.material,
            category.hardness,
            category.resistance,
            category.soundType,
            variants.size()
        );
        VariantItemBlock item = new VariantItemBlock(block, variants, EnumRarity.RARE);
        REGISTERED_BLOCKS.add(block);
        REGISTERED_BLOCK_ITEMS.add(item);

        for (int meta = 0; meta < variants.size(); meta++) {
            VariantEntry entry = variants.get(meta);
            category.targetMap.put(entry.slug, block);
            BLOCK_ITEMS.put(key(entry.slug, entry.blockName), item);
            BLOCK_ITEM_METAS.put(key(entry.slug, entry.blockName), meta);
            MODEL_REGISTRATIONS.add(new ModelRegistration(item, meta, entry.modelLocation));
        }
    }

    private static String findBlockName(DerivedContentCatalog.DerivedMaterial material, Category category) {
        for (DerivedContentCatalog.DerivedEntry entry : material.getBlocks()) {
            String name = normalize(entry.getName());
            if (category.matches(name)) {
                return name;
            }
        }
        return null;
    }

    private static String key(String materialSlug, String blockName) {
        return normalize(materialSlug) + ":" + normalize(blockName);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(java.util.Locale.ENGLISH);
    }

    private static String translationKey(String path) {
        return RedstonechanChemicalElements.MODID + "." + path.replace('/', '.');
    }

    private static PropertyInteger createVariantProperty() {
        return (PropertyInteger) invokeStaticMethod(
            PropertyInteger.class,
            new String[] { "func_177719_a", "create" },
            new Class<?>[] { String.class, Integer.TYPE, Integer.TYPE },
            "variant",
            Integer.valueOf(0),
            Integer.valueOf(VARIANTS_PER_GROUP - 1)
        );
    }

    private static Material resolveMaterial(String srgName, String mcpName) {
        return (Material) getStaticField(Material.class, srgName, mcpName);
    }

    private static SoundType resolveSoundType(String srgName, String mcpName) {
        return (SoundType) getStaticField(SoundType.class, srgName, mcpName);
    }

    private static CreativeTabs getSearchTab() {
        return (CreativeTabs) getStaticField(CreativeTabs.class, "field_78027_g", "SEARCH");
    }

    private static boolean isSearchTab(CreativeTabs tab) {
        return tab != null && tab == getSearchTab();
    }

    private static ItemStack emptyItemStack() {
        return (ItemStack) getStaticField(ItemStack.class, "field_190927_a", "EMPTY");
    }

    private static Block applyBlockTranslationKey(Block block, String translationKey) {
        invokeMethod(
            block,
            new String[] { "func_149663_c", "setTranslationKey" },
            new Class<?>[] { String.class },
            translationKey
        );
        return block;
    }

    private static Block applyBlockCreativeTab(Block block, CreativeTabs creativeTabs) {
        invokeMethod(
            block,
            new String[] { "func_149647_a", "setCreativeTab" },
            new Class<?>[] { CreativeTabs.class },
            creativeTabs
        );
        return block;
    }

    private static Block applyBlockHardness(Block block, float hardness) {
        invokeMethod(
            block,
            new String[] { "func_149711_c", "setHardness" },
            new Class<?>[] { Float.TYPE },
            Float.valueOf(hardness)
        );
        return block;
    }

    private static Block applyBlockResistance(Block block, float resistance) {
        invokeMethod(
            block,
            new String[] { "func_149752_b", "setResistance" },
            new Class<?>[] { Float.TYPE },
            Float.valueOf(resistance)
        );
        return block;
    }

    private static Block applyBlockSoundType(Block block, SoundType soundType) {
        invokeMethod(
            block,
            new String[] { "func_149672_a", "setSoundType" },
            new Class<?>[] { SoundType.class },
            soundType
        );
        return block;
    }

    private static void applyBlockDefaultState(Block block, IBlockState state) {
        invokeMethod(
            block,
            new String[] { "func_180632_j", "setDefaultState" },
            new Class<?>[] { IBlockState.class },
            state
        );
    }

    private static IBlockState getBlockDefaultState(Block block) {
        return (IBlockState) invokeMethod(
            block,
            new String[] { "func_176223_P", "getDefaultState" },
            new Class<?>[0]
        );
    }

    private static BlockStateContainer getBlockStateContainer(Block block) {
        return (BlockStateContainer) getInstanceField(block, "field_176227_L", "blockState");
    }

    private static IBlockState getBaseState(BlockStateContainer blockStateContainer) {
        return (IBlockState) invokeMethod(
            blockStateContainer,
            new String[] { "func_177621_b", "getBaseState" },
            new Class<?>[0]
        );
    }

    private static IBlockState withProperty(IBlockState state, PropertyInteger property, Integer value) {
        return (IBlockState) invokeMethod(
            state,
            new String[] { "func_177226_a", "withProperty" },
            new Class<?>[] { IProperty.class, Comparable.class },
            property,
            value
        );
    }

    private static Integer getPropertyValue(IBlockState state, PropertyInteger property) {
        return (Integer) invokeMethod(
            state,
            new String[] { "func_177229_b", "getValue" },
            new Class<?>[] { IProperty.class },
            property
        );
    }

    private static String getBlockTranslationKey(Block block) {
        return (String) invokeMethod(
            block,
            new String[] { "func_149739_a", "getTranslationKey" },
            new Class<?>[0]
        );
    }

    private static Item applyItemTranslationKey(Item item, String translationKey) {
        invokeMethod(
            item,
            new String[] { "func_77655_b", "setTranslationKey" },
            new Class<?>[] { String.class },
            translationKey
        );
        return item;
    }

    private static Item applyItemCreativeTab(Item item, CreativeTabs creativeTabs) {
        invokeMethod(
            item,
            new String[] { "func_77637_a", "setCreativeTab" },
            new Class<?>[] { CreativeTabs.class },
            creativeTabs
        );
        return item;
    }

    private static Item applyItemHasSubtypes(Item item, boolean hasSubtypes) {
        invokeMethod(
            item,
            new String[] { "func_77627_a", "setHasSubtypes" },
            new Class<?>[] { Boolean.TYPE },
            Boolean.valueOf(hasSubtypes)
        );
        return item;
    }

    private static Item applyItemMaxDamage(Item item, int maxDamage) {
        invokeMethod(
            item,
            new String[] { "func_77656_e", "setMaxDamage" },
            new Class<?>[] { Integer.TYPE },
            Integer.valueOf(maxDamage)
        );
        return item;
    }

    private static int getItemStackMetadata(ItemStack stack) {
        return ((Integer) invokeMethod(
            stack,
            new String[] { "func_77960_j", "getMetadata" },
            new Class<?>[0]
        )).intValue();
    }

    private static Object getStaticField(Class<?> owner, String... candidateNames) {
        for (String candidateName : candidateNames) {
            try {
                Field field = findField(owner, candidateName);
                if (field == null) {
                    continue;
                }
                return field.get(null);
            } catch (IllegalAccessException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve static field for " + owner.getName());
    }

    private static Object getInstanceField(Object target, String... candidateNames) {
        for (String candidateName : candidateNames) {
            try {
                Field field = findField(target.getClass(), candidateName);
                if (field == null) {
                    continue;
                }
                return field.get(target);
            } catch (IllegalAccessException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve instance field for " + target.getClass().getName());
    }

    private static Object invokeStaticMethod(Class<?> owner, String[] candidateNames, Class<?>[] parameterTypes, Object... arguments) {
        for (String candidateName : candidateNames) {
            try {
                Method method = findMethod(owner, candidateName, parameterTypes);
                if (method == null) {
                    continue;
                }
                return method.invoke(null, arguments);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve static method for " + owner.getName());
    }

    private static Object invokeMethod(Object target, String[] candidateNames, Class<?>[] parameterTypes, Object... arguments) {
        for (String candidateName : candidateNames) {
            try {
                Method method = findMethod(target.getClass(), candidateName, parameterTypes);
                if (method == null) {
                    continue;
                }
                return method.invoke(target, arguments);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve method for " + target.getClass().getName());
    }

    private static Field findField(Class<?> owner, String name) {
        Class<?> current = owner;
        while (current != null) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private static Method findMethod(Class<?> owner, String name, Class<?>[] parameterTypes) {
        try {
            return owner.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException ignored) {
        }

        Class<?> current = owner;
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private enum Category {
        STORAGE("storage", BLOCKS, resolveMaterial("field_151573_f", "IRON"), 2.8F, 6.0F, resolveSoundType("field_185852_e", "METAL")),
        RAW("raw", RAW_BLOCKS, resolveMaterial("field_151573_f", "IRON"), 2.8F, 6.0F, resolveSoundType("field_185851_d", "STONE")),
        ORE("ore", ORES, resolveMaterial("field_151576_e", "ROCK"), 3.0F, 5.0F, resolveSoundType("field_185851_d", "STONE"));

        private final String registryPrefix;
        private final Map<String, Block> targetMap;
        private final Material material;
        private final float hardness;
        private final float resistance;
        private final SoundType soundType;

        Category(String registryPrefix, Map<String, Block> targetMap, Material material, float hardness, float resistance, SoundType soundType) {
            this.registryPrefix = registryPrefix;
            this.targetMap = targetMap;
            this.material = material;
            this.hardness = hardness;
            this.resistance = resistance;
            this.soundType = soundType;
        }

        private boolean matches(String blockName) {
            if (this == ORE) {
                return blockName.endsWith("_ore");
            }
            if (this == RAW) {
                return blockName.startsWith("raw_") && blockName.endsWith("_block");
            }
            return blockName.endsWith("_block") && !blockName.startsWith("raw_");
        }

        private String registryPath(int groupIndex) {
            return "derived_" + registryPrefix + "_" + groupIndex;
        }
    }

    private static final class VariantEntry {
        private final String slug;
        private final String blockName;
        private final String translationKey;
        private final String modelLocation;

        private VariantEntry(String slug, String blockName, String translationKey, String modelLocation) {
            this.slug = slug;
            this.blockName = blockName;
            this.translationKey = translationKey;
            this.modelLocation = modelLocation;
        }
    }

    public static final class ModelRegistration {
        private final Item item;
        private final int meta;
        private final String modelLocation;

        private ModelRegistration(Item item, int meta, String modelLocation) {
            this.item = item;
            this.meta = meta;
            this.modelLocation = modelLocation;
        }

        public Item getItem() {
            return item;
        }

        public int getMeta() {
            return meta;
        }

        public String getModelLocation() {
            return modelLocation;
        }
    }

    private static final class VariantBlock extends Block {
        private final int variantCount;

        private VariantBlock(String path, Material material, float hardness, float resistance, SoundType soundType, int variantCount) {
            super(material);
            this.variantCount = variantCount;
            setRegistryName(RedstonechanChemicalElements.MODID, path);
            applyBlockTranslationKey(this, translationKey(path));
            applyBlockCreativeTab(this, ModCreativeTabs.DERIVED_BLOCK_TAB);
            applyBlockHardness(this, hardness);
            applyBlockResistance(this, resistance);
            applyBlockSoundType(this, soundType);
            setHarvestLevel("pickaxe", 1);
            applyBlockDefaultState(this, withProperty(getBaseState(getBlockStateContainer(this)), VARIANT, Integer.valueOf(0)));
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

        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, VARIANT);
        }

        @Override
        public IBlockState getStateFromMeta(int meta) {
            return withProperty(getBlockDefaultState(this), VARIANT, Integer.valueOf(clampMeta(meta, variantCount)));
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            return getPropertyValue(state, VARIANT).intValue();
        }

        @Override
        public int damageDropped(IBlockState state) {
            return getMetaFromState(state);
        }

        @Override
        public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
            if (tab != ModCreativeTabs.DERIVED_BLOCK_TAB && !isSearchTab(tab)) {
                return;
            }

            for (int meta = 0; meta < variantCount; meta++) {
                items.add(new ItemStack(this, 1, meta));
            }
        }

    }

    private static final class VariantItemBlock extends ItemBlock {
        private final List<VariantEntry> variants;
        private final EnumRarity rarity;

        private VariantItemBlock(Block block, List<VariantEntry> variants, EnumRarity rarity) {
            super(block);
            this.variants = new ArrayList<>(variants);
            this.rarity = rarity;
            setRegistryName(block.getRegistryName());
            applyItemTranslationKey(this, getBlockTranslationKey(block));
            applyItemCreativeTab(this, ModCreativeTabs.DERIVED_BLOCK_TAB);
            applyItemHasSubtypes(this, true);
            applyItemMaxDamage(this, 0);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public String getTranslationKey(ItemStack stack) {
            return variants.get(clampMeta(getItemStackMetadata(stack), variants.size())).translationKey;
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return rarity;
        }

    }

    private static int clampMeta(int meta, int size) {
        if (size <= 0) {
            return 0;
        }
        if (meta < 0) {
            return 0;
        }
        if (meta >= size) {
            return size - 1;
        }
        return meta;
    }
}
