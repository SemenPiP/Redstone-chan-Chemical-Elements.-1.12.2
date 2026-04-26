package com.chinaex123.redstone_chemical_elements.compat.tconstruct;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

final class TConstructDataLoader {
    private static final Gson GSON = new Gson();
    private static final String TRAITS_RESOURCE = "/assets/redstone_chemical_elements/tconstruct/traits.json";
    private static final String MATERIALS_RESOURCE = "/assets/redstone_chemical_elements/tconstruct/materials.json";

    private TConstructDataLoader() {
    }

    static List<TraitDefinition> loadTraits() {
        return loadList(TRAITS_RESOURCE, new TypeToken<List<TraitDefinition>>() { }.getType());
    }

    static List<MaterialDefinition> loadMaterials() {
        return loadList(MATERIALS_RESOURCE, new TypeToken<List<MaterialDefinition>>() { }.getType());
    }

    private static <T> List<T> loadList(String resourcePath, Type type) {
        InputStream stream = RedstonechanChemicalElements.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Missing TConstruct data resource: " + resourcePath);
        }

        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            List<T> loaded = GSON.fromJson(reader, type);
            if (loaded == null) {
                throw new IllegalStateException("Empty TConstruct data resource: " + resourcePath);
            }
            return loaded;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load TConstruct data resource: " + resourcePath, exception);
        }
    }

    static final class TraitDefinition {
        String identifier;
        String name;
        String description;
        int color;
        float miningMultiplier;
        float damageMultiplier;
        float flatDamage;
        int critChance;
        float critDamageBonus;
        int saveChance;
        int saveAmount;
        int penaltyChance;
        int penaltyAmount;
        float healOnHit;
        float healOnKill;
        int burnTime;
        int slowChance;
        float knockbackBonus;
        int repairBonus;
    }

    static final class MaterialDefinition {
        String identifier;
        String fluidName;
        String localizedName;
        String oreBase;
        int color;
        int headDurability;
        float miningSpeed;
        float attack;
        int harvestLevel;
        float handleModifier;
        int handleDurability;
        int extraDurability;
        List<String> headTraits;
        List<String> handleTraits;
        List<String> extraTraits;
    }
}
