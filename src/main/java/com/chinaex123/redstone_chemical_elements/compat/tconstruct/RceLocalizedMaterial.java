package com.chinaex123.redstone_chemical_elements.compat.tconstruct;

import slimeknights.tconstruct.library.materials.Material;

final class RceLocalizedMaterial extends Material {
    private final String localizedName;

    RceLocalizedMaterial(String identifier, int color, String localizedName) {
        super(identifier, color);
        this.localizedName = localizedName;
    }

    @Override
    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String getLocalizedItemName(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return localizedName;
        }
        return localizedName + itemName;
    }
}
