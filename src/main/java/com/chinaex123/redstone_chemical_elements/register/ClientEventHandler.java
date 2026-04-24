package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import com.chinaex123.redstone_chemical_elements.client.font.SuperheavyFontInstaller;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = RedstonechanChemicalElements.MODID, value = Side.CLIENT)
public final class ClientEventHandler {
    private ClientEventHandler() {
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (DerivedBlock.ModelRegistration registration : DerivedBlock.getModelRegistrations()) {
            registerModel(registration.getItem(), registration.getMeta(), registration.getModelLocation());
        }

        for (Item item : DerivedItem.getAllItems()) {
            registerModel(item);
        }

        for (Item item : SingularityItem.getAllItems()) {
            registerModel(item);
        }

        for (Item item : ElementBlock.getAllBlockItems()) {
            registerModel(item);
        }

        for (Item item : ElementItem.getAllItems()) {
            registerModel(item);
        }

        for (BlockFluidClassic block : DerivedFluidRegistry.getFluidBlocks()) {
            registerFluidModel(block);
        }

        for (BlockFluidClassic block : ElementFluidRegistry.getFluidBlocks()) {
            registerFluidModel(block);
        }
    }

    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    private static void registerModel(Item item, int meta, String modelLocation) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelLocation, "inventory"));
    }

    private static void registerFluidModel(BlockFluidClassic block) {
        final ModelResourceLocation modelLocation =
            new ModelResourceLocation(RedstonechanChemicalElements.MODID + ":element_fluid", "fluid=" + block.getFluid().getName());
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return modelLocation;
            }
        });
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            SuperheavyFontInstaller.installIfNeeded();
        }
    }
}
