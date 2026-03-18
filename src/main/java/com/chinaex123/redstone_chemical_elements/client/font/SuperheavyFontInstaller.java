package com.chinaex123.redstone_chemical_elements.client.font;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SuperheavyFontInstaller {
    private static final ResourceLocation ASCII_FONT = new ResourceLocation("textures/font/ascii.png");

    private SuperheavyFontInstaller() {
    }

    public static void installIfNeeded() {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft == null || minecraft.fontRenderer instanceof SuperheavyFontRenderer) {
            return;
        }

        FontRenderer currentRenderer = minecraft.fontRenderer;
        if (currentRenderer == null || minecraft.gameSettings == null || minecraft.getTextureManager() == null) {
            return;
        }

        SuperheavyFontRenderer replacement = new SuperheavyFontRenderer(
                minecraft.gameSettings,
                ASCII_FONT,
                minecraft.getTextureManager(),
                currentRenderer.getUnicodeFlag()
        );
        replacement.setUnicodeFlag(currentRenderer.getUnicodeFlag());

        LanguageManager languageManager = minecraft.getLanguageManager();
        if (languageManager != null) {
            replacement.setBidiFlag(languageManager.isCurrentLanguageBidirectional());
        }

        if (minecraft.getResourceManager() instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) minecraft.getResourceManager()).registerReloadListener(replacement);
        }

        minecraft.fontRenderer = replacement;
        RedstonechanChemicalElements.LOGGER.info("Installed client-side superheavy font renderer patch.");
    }
}
