package com.chinaex123.redstone_chemical_elements.register;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.IForgeRegistry;

public final class ElementFluidRegistry {
    private static final ResourceLocation WATER_STILL_TEXTURE = new ResourceLocation("blocks/water_still");
    private static final ResourceLocation WATER_FLOWING_TEXTURE = new ResourceLocation("blocks/water_flow");
    private static final ResourceLocation LAVA_STILL_TEXTURE = new ResourceLocation("blocks/lava_still");
    private static final ResourceLocation LAVA_FLOWING_TEXTURE = new ResourceLocation("blocks/lava_flow");

    private static final Map<String, Fluid> FLUIDS = new LinkedHashMap<>();
    private static final Map<String, BlockFluidClassic> FLUID_BLOCKS = new LinkedHashMap<>();

    private static boolean bootstrapped;

    private ElementFluidRegistry() {
    }

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        for (String element : ElementCatalog.ELEMENTS) {
            ElementFluid definedFluid = new ElementFluid(
                getFluidName(element),
                element,
                getStillTexture(element),
                getFlowingTexture(element)
            );
            definedFluid
                .setDensity(ElementCatalog.getFluidDensity(element))
                .setViscosity(ElementCatalog.getFluidViscosity(element))
                .setTemperature(ElementCatalog.getFluidTemperature(element))
                .setColor(ElementCatalog.getFluidColor(element))
                .setGaseous(ElementCatalog.isGas(element));

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
                : createFluidBlock(activeFluid, element);

            FLUIDS.put(element, activeFluid);
            FLUID_BLOCKS.put(element, block);
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

    public static Fluid getFluid(String elementName) {
        bootstrap();
        return FLUIDS.get(ElementCatalog.normalize(elementName));
    }

    public static ItemStack getFilledBucket(String elementName) {
        Fluid fluid = getFluid(elementName);
        if (fluid == null) {
            return ItemStack.EMPTY;
        }
        return FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
    }

    private static BlockFluidClassic createFluidBlock(Fluid fluid, String element) {
        if (ElementCatalog.isGas(element)) {
            return new FloatingGasFluidBlock(fluid, element);
        }
        return new ElementFluidBlock(fluid, element);
    }

    private static String getFluidName(String element) {
        return "redstone_chemical_elements_" + element.toLowerCase(Locale.ENGLISH) + "_fluid";
    }

    private static ResourceLocation getStillTexture(String element) {
        return ElementCatalog.isMolten(element) ? LAVA_STILL_TEXTURE : WATER_STILL_TEXTURE;
    }

    private static ResourceLocation getFlowingTexture(String element) {
        return ElementCatalog.isMolten(element) ? LAVA_FLOWING_TEXTURE : WATER_FLOWING_TEXTURE;
    }
}
