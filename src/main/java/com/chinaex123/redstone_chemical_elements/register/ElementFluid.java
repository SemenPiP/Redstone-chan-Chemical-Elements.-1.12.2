package com.chinaex123.redstone_chemical_elements.register;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ElementFluid extends Fluid {
    private final String elementName;

    public ElementFluid(String name, String elementName, ResourceLocation still, ResourceLocation flowing) {
        super(name, still, flowing);
        this.elementName = ElementCatalog.normalize(elementName);
        setUnlocalizedName(name);
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        String baseName = resolveLocalizedElementName();
        if (ElementCatalog.isGas(elementName)) {
            return looksChinese(baseName) ? baseName + "\u6c14" : baseName + " Gas";
        }
        if (ElementCatalog.isLiquid(elementName)) {
            return looksChinese(baseName) ? "\u6db2\u6001" + baseName : "Liquid " + baseName;
        }
        return looksChinese(baseName) ? "\u7194\u878d" + baseName : "Molten " + baseName;
    }

    private String resolveLocalizedElementName() {
        String translationKey = ElementCatalog.getIngotTranslationKey(elementName);
        String localizedIngotName = I18n.canTranslate(translationKey)
            ? I18n.translateToLocal(translationKey)
            : ElementCatalog.getDisplayName(elementName) + " Ingot";

        if (localizedIngotName.endsWith(" Ingot")) {
            return localizedIngotName.substring(0, localizedIngotName.length() - " Ingot".length());
        }
        if (localizedIngotName.endsWith("\u952d")) {
            return localizedIngotName.substring(0, localizedIngotName.length() - 1);
        }
        return localizedIngotName;
    }

    private boolean looksChinese(String text) {
        for (int i = 0; i < text.length(); i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(text.charAt(i));
            if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }
}
