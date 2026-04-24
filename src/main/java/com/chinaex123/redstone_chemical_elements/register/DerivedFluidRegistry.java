package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class DerivedFluidRegistry {
    private static final ResourceLocation LAVA_STILL_TEXTURE = new ResourceLocation("blocks/lava_still");
    private static final ResourceLocation LAVA_FLOWING_TEXTURE = new ResourceLocation("blocks/lava_flow");

    private static final Map<String, Fluid> FLUIDS = new LinkedHashMap<>();
    private static final Map<String, BlockFluidClassic> FLUID_BLOCKS = new LinkedHashMap<>();

    private static boolean bootstrapped;

    private DerivedFluidRegistry() {
    }

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        DerivedContentCatalog.bootstrap();
        for (DerivedContentCatalog.DerivedMaterial material : DerivedContentCatalog.getMaterials()) {
            String slug = normalize(material.getSlug());
            if (slug.isEmpty()) {
                continue;
            }

            Fluid definedFluid = new DerivedFluid(getFluidName(slug), material, LAVA_STILL_TEXTURE, LAVA_FLOWING_TEXTURE);
            definedFluid
                .setDensity(3000)
                .setViscosity(1450)
                .setTemperature(1325)
                .setLuminosity(7);

            FluidRegistry.registerFluid(definedFluid);
            Fluid activeFluid = FluidRegistry.getFluid(definedFluid.getName());
            if (activeFluid == null) {
                activeFluid = definedFluid;
            }

            if (!FluidRegistry.hasBucket(activeFluid)) {
                FluidRegistry.addBucketForFluid(activeFluid);
            }

            BlockFluidClassic block = activeFluid.getBlock() instanceof BlockFluidClassic
                ? (BlockFluidClassic) activeFluid.getBlock()
                : new DerivedFluidBlock(activeFluid, slug);

            FLUIDS.put(slug, activeFluid);
            FLUID_BLOCKS.put(slug, block);
        }

        bootstrapped = true;
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        bootstrap();
        for (BlockFluidClassic block : FLUID_BLOCKS.values()) {
            if (!registry.containsKey(block.getRegistryName())) {
                registry.register(block);
            }
        }
    }

    public static Collection<BlockFluidClassic> getFluidBlocks() {
        bootstrap();
        return Collections.unmodifiableCollection(FLUID_BLOCKS.values());
    }

    public static Fluid getFluid(String materialName) {
        bootstrap();
        return FLUIDS.get(normalize(materialName));
    }

    private static String getFluidName(String materialName) {
        return RedstonechanChemicalElements.MODID + "_" + normalize(materialName) + "_fluid";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ENGLISH);
    }

    private static final class DerivedFluid extends Fluid {
        private final DerivedContentCatalog.DerivedMaterial material;

        private DerivedFluid(String name, DerivedContentCatalog.DerivedMaterial material, ResourceLocation still, ResourceLocation flowing) {
            super(name, still, flowing);
            this.material = material;
            setUnlocalizedName(name);
        }

        @Override
        public String getLocalizedName(net.minecraftforge.fluids.FluidStack stack) {
            String translationKey = "fluid." + getName();
            if (I18n.canTranslate(translationKey)) {
                return I18n.translateToLocal(translationKey);
            }
            return "Molten " + material.getEnglishName();
        }
    }

    private static final class DerivedFluidBlock extends ElementFluidBlock {
        private DerivedFluidBlock(Fluid fluid, String materialName) {
            super(fluid, materialName, "derived/" + materialName + "/" + materialName + "_fluid");
        }
    }
}
