package com.chinaex123.redstone_chemical_elements.register;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DerivedContentCatalog {
    private static final String MANIFEST_PATH = "assets/redstone_chemical_elements/derived/manifest.json";
    private static final Gson GSON = new Gson();

    private static Manifest manifest;

    private DerivedContentCatalog() {
    }

    public static synchronized void bootstrap() {
        if (manifest != null) {
            return;
        }

        try (InputStream inputStream = DerivedContentCatalog.class.getClassLoader().getResourceAsStream(MANIFEST_PATH)) {
            if (inputStream == null) {
                manifest = new Manifest();
                return;
            }

            Manifest loaded = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), Manifest.class);
            manifest = loaded == null ? new Manifest() : loaded.sanitize();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load derived content manifest: " + MANIFEST_PATH, exception);
        }
    }

    public static List<DerivedMaterial> getMaterials() {
        bootstrap();
        return manifest.materials;
    }

    private static <T> List<T> immutableCopy(List<T> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static final class Manifest {
        private List<DerivedMaterial> materials = Collections.emptyList();

        private Manifest sanitize() {
            List<DerivedMaterial> sanitizedMaterials = new ArrayList<>();
            for (DerivedMaterial material : immutableCopy(materials)) {
                if (material == null) {
                    continue;
                }
                material.slug = safe(material.slug);
                material.englishName = safe(material.englishName);
                material.chineseName = safe(material.chineseName);
                material.items = sanitizeEntries(material.items);
                material.blocks = sanitizeEntries(material.blocks);
                sanitizedMaterials.add(material);
            }
            materials = Collections.unmodifiableList(sanitizedMaterials);
            return this;
        }

        private List<DerivedEntry> sanitizeEntries(List<DerivedEntry> entries) {
            List<DerivedEntry> sanitizedEntries = new ArrayList<>();
            for (DerivedEntry entry : immutableCopy(entries)) {
                if (entry == null) {
                    continue;
                }
                entry.name = safe(entry.name);
                entry.englishName = safe(entry.englishName);
                entry.chineseName = safe(entry.chineseName);
                sanitizedEntries.add(entry);
            }
            return Collections.unmodifiableList(sanitizedEntries);
        }
    }

    public static final class DerivedMaterial {
        private String slug = "";
        private String englishName = "";
        private String chineseName = "";
        private List<DerivedEntry> items = Collections.emptyList();
        private List<DerivedEntry> blocks = Collections.emptyList();

        public String getSlug() {
            return slug;
        }

        public String getEnglishName() {
            return englishName;
        }

        public String getChineseName() {
            return chineseName;
        }

        public List<DerivedEntry> getItems() {
            return items;
        }

        public List<DerivedEntry> getBlocks() {
            return blocks;
        }
    }

    public static final class DerivedEntry {
        private String name = "";
        private String englishName = "";
        private String chineseName = "";

        public String getName() {
            return name;
        }

        public String getEnglishName() {
            return englishName;
        }

        public String getChineseName() {
            return chineseName;
        }
    }
}
