package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import com.chinaex123.redstone_chemical_elements.client.font.SuperheavyFontInstaller;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
        for (Item item : ElementBlock.getAllBlockItems()) {
            registerModel(item);
        }

        for (Item item : ElementItem.getAllItems()) {
            registerModel(item);
        }
    }

    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            SuperheavyFontInstaller.installIfNeeded();
        }
    }
}
