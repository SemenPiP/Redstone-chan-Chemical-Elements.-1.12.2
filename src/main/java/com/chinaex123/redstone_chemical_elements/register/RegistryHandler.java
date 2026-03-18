package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RedstonechanChemicalElements.MODID)
public final class RegistryHandler {
    private RegistryHandler() {
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        ElementBlock.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        ElementBlock.registerItems(event.getRegistry());
        ElementItem.registerItems(event.getRegistry());
    }
}
