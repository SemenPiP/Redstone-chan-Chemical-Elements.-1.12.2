package com.chinaex123.redstone_chemical_elements.register;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

public final class ElementCatalog {
    private static final String[] INGOT_TEXTURE_PATTERNS = {
        "assets/redstone_chemical_elements/textures/item/%s/%s_ingot.png",
        "assets/redstone_chemical_elements/textures/items/%s/%s_ingot.png"
    };

    private static final Map<String, Integer> FLUID_COLOR_CACHE = new LinkedHashMap<>();

    private ElementCatalog() {
    }

    public static final String[] ELEMENTS = {
        "actinium",
        "aluminum",
        "americium",
        "antimony",
        "argon",
        "arsenic",
        "astatine",
        "barium",
        "berkelium",
        "beryllium",
        "bismuth",
        "bohrium",
        "boron",
        "bromine",
        "cadmium",
        "calcium",
        "californium",
        "carbon",
        "cerium",
        "cesium",
        "chlorine",
        "chromium",
        "cobalt",
        "copernicium",
        "copper",
        "curium",
        "darmstadtium",
        "dubnium",
        "dysprosium",
        "einsteinium",
        "erbium",
        "europium",
        "fermium",
        "flerovium",
        "fluorine",
        "francium",
        "gadolinium",
        "gallium",
        "germanium",
        "gold",
        "hafnium",
        "hassium",
        "helium",
        "holmium",
        "hydrargyrum",
        "hydrogen",
        "indium",
        "iodine",
        "iridium",
        "iron",
        "krypton",
        "lanthanum",
        "lawrencium",
        "lead",
        "lithium",
        "livermorium",
        "longium",
        "lutetium",
        "magnesium",
        "manganese",
        "meitnerium",
        "mendelevium",
        "molybdenum",
        "moscovium",
        "mysterium",
        "neodymium",
        "neon",
        "neptunium",
        "nickel",
        "nihonium",
        "niobium",
        "nitrogen",
        "nobelium",
        "oganesson",
        "osmium",
        "oxygen",
        "palladium",
        "phosphorus",
        "platinum",
        "plutonium",
        "polonium",
        "potassium",
        "praseodymium",
        "promethium",
        "protactinium",
        "radium",
        "radon",
        "rhenium",
        "rhodium",
        "roentgenium",
        "rubidium",
        "ruthenium",
        "rutherfordium",
        "samarium",
        "scandium",
        "seaborgium",
        "selenium",
        "silicon",
        "silver",
        "sodium",
        "strontium",
        "sulfur",
        "tantalum",
        "technetium",
        "tellurium",
        "tennessine",
        "terbium",
        "thallium",
        "thorium",
        "thulium",
        "tin",
        "titanium",
        "tungsten",
        "uranium",
        "vanadium",
        "xenon",
        "ytterbium",
        "yttrium",
        "zinc",
        "zirconium"
    };

    public static final String[] GAS_ELEMENTS = {
        "hydrogen",
        "helium",
        "nitrogen",
        "oxygen",
        "fluorine",
        "neon",
        "chlorine",
        "argon",
        "krypton",
        "xenon",
        "radon",
        "oganesson"
    };

    public static final String[] LIQUID_ELEMENTS = {
        "bromine",
        "hydrargyrum"
    };

    private static final String[] METAL_EXCLUDED_ELEMENTS = {
        "astatine",
        "boron",
        "bromine",
        "carbon",
        "iodine",
        "phosphorus",
        "selenium",
        "silicon",
        "sulfur",
        "tennessine"
    };

    private static final Set<String> GAS_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GAS_ELEMENTS)));
    private static final Set<String> LIQUID_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(LIQUID_ELEMENTS)));
    private static final Set<String> METAL_EXCLUDED_SET =
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(METAL_EXCLUDED_ELEMENTS)));
    public static final String[] METAL_ELEMENTS = createMetalElements();
    private static final Set<String> METAL_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(METAL_ELEMENTS)));

    public static boolean isGas(String elementName) {
        return GAS_SET.contains(normalize(elementName));
    }

    public static boolean isLiquid(String elementName) {
        return LIQUID_SET.contains(normalize(elementName));
    }

    public static boolean isMetal(String elementName) {
        return METAL_SET.contains(normalize(elementName));
    }

    public static boolean isMolten(String elementName) {
        String normalized = normalize(elementName);
        return !GAS_SET.contains(normalized) && !LIQUID_SET.contains(normalized);
    }

    public static boolean usesWaterFluidTextures(String elementName) {
        return isLiquid(elementName);
    }

    public static String getDisplayName(String elementName) {
        String[] parts = normalize(elementName).split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }

    public static String getFluidDisplayName(String elementName) {
        String displayName = getDisplayName(elementName);
        if (isGas(elementName)) {
            return displayName + " Gas";
        }
        if (isLiquid(elementName)) {
            return "Liquid " + displayName;
        }
        return "Molten " + displayName;
    }

    public static int getFluidDensity(String elementName) {
        if (isGas(elementName)) {
            return -600;
        }
        if (isLiquid(elementName)) {
            return 1200;
        }
        return 2600;
    }

    public static int getFluidViscosity(String elementName) {
        if (isGas(elementName)) {
            return 600;
        }
        if (isLiquid(elementName)) {
            return 1400;
        }
        return 3200;
    }

    public static int getFluidTemperature(String elementName) {
        if (isGas(elementName)) {
            return 295;
        }
        if (isLiquid(elementName)) {
            return 310;
        }
        return 1200;
    }

    public static int getFluidLuminosity(String elementName) {
        String normalized = normalize(elementName);
        if (isGas(normalized) || isLiquid(normalized)) {
            return 0;
        }
        if ("actinium".equals(normalized)
            || "americium".equals(normalized)
            || "curium".equals(normalized)
            || "neptunium".equals(normalized)
            || "plutonium".equals(normalized)
            || "polonium".equals(normalized)
            || "promethium".equals(normalized)
            || "radium".equals(normalized)
            || "thorium".equals(normalized)
            || "uranium".equals(normalized)) {
            return 8;
        }
        return 12;
    }

    public static int getFluidColor(String elementName) {
        String normalized = normalize(elementName);
        Integer cachedColor = FLUID_COLOR_CACHE.get(normalized);
        if (cachedColor != null) {
            return cachedColor;
        }

        int sampledColor = sampleFluidColorFromIngot(normalized);
        if (sampledColor != -1) {
            int shadedColor = shadeFluidColor(normalized, sampledColor);
            FLUID_COLOR_CACHE.put(normalized, shadedColor);
            return shadedColor;
        }

        int fallbackColor = shadeFluidColor(normalized, getFallbackFluidColor(normalized));
        FLUID_COLOR_CACHE.put(normalized, fallbackColor);
        return fallbackColor;
    }

    public static String getIngotTranslationKey(String elementName) {
        String normalized = normalize(elementName);
        return "item.redstone_chemical_elements." + normalized + "." + normalized + "_ingot.name";
    }

    public static String normalize(String elementName) {
        return elementName.toLowerCase(Locale.ENGLISH);
    }

    private static String[] createMetalElements() {
        String[] metals = new String[ELEMENTS.length];
        int count = 0;
        for (String element : ELEMENTS) {
            String normalized = normalize(element);
            if (GAS_SET.contains(normalized) || METAL_EXCLUDED_SET.contains(normalized)) {
                continue;
            }
            metals[count++] = normalized;
        }
        return Arrays.copyOf(metals, count);
    }

    private static int getFallbackFluidColor(String normalized) {
        if ("bromine".equals(normalized)) {
            return 0xFF8A3B12;
        }
        if ("hydrargyrum".equals(normalized)) {
            return 0xFFD2D6DE;
        }

        int hash = Math.abs(normalized.hashCode());
        if (isGas(normalized)) {
            float hue = (180.0F + hash % 80) / 360.0F;
            return Color.HSBtoRGB(hue, 0.30F, 0.97F);
        }
        if (isLiquid(normalized)) {
            float hue = (20.0F + hash % 50) / 360.0F;
            return Color.HSBtoRGB(hue, 0.70F, 0.88F);
        }
        float hue = (12.0F + hash % 36) / 360.0F;
        return Color.HSBtoRGB(hue, 0.78F, 0.95F);
    }

    private static int sampleFluidColorFromIngot(String normalized) {
        for (String pattern : INGOT_TEXTURE_PATTERNS) {
            BufferedImage image = readImage(String.format(Locale.ENGLISH, pattern, normalized, normalized));
            if (image == null) {
                continue;
            }

            int sampledColor = sampleOpaquePixels(image, true);
            if (sampledColor != -1) {
                return sampledColor;
            }

            sampledColor = sampleOpaquePixels(image, false);
            if (sampledColor != -1) {
                return sampledColor;
            }
        }
        return -1;
    }

    private static int shadeFluidColor(String normalized, int argb) {
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);

        float saturation = hsb[1];
        float brightness = hsb[2];

        if (isGas(normalized)) {
            saturation = clamp01(saturation * 0.82F);
            brightness = clamp01(0.58F + brightness * 0.20F);
        } else if (isLiquid(normalized)) {
            saturation = clamp01(saturation * 1.08F + 0.02F);
            brightness = clamp01(brightness * 0.60F + 0.06F);
        } else {
            saturation = clamp01(saturation * 1.18F + 0.03F);
            brightness = clamp01(brightness * 0.46F + 0.04F);
        }

        if (!isGas(normalized) && brightness < 0.22F) {
            brightness = 0.22F;
        }

        return 0xFF000000 | (Color.HSBtoRGB(hsb[0], saturation, brightness) & 0x00FFFFFF);
    }

    private static float clamp01(float value) {
        if (value < 0.0F) {
            return 0.0F;
        }
        if (value > 1.0F) {
            return 1.0F;
        }
        return value;
    }

    private static BufferedImage readImage(String resourcePath) {
        try (InputStream inputStream = ElementCatalog.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            return ImageIO.read(inputStream);
        } catch (IOException ignored) {
            return null;
        }
    }

    private static int sampleOpaquePixels(BufferedImage image, boolean skipOuterBorder) {
        long red = 0L;
        long green = 0L;
        long blue = 0L;
        long count = 0L;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (skipOuterBorder && (x == 0 || y == 0 || x == image.getWidth() - 1 || y == image.getHeight() - 1)) {
                    continue;
                }

                int argb = image.getRGB(x, y);
                int alpha = argb >>> 24;
                if (alpha < 32) {
                    continue;
                }

                red += (argb >> 16) & 0xFF;
                green += (argb >> 8) & 0xFF;
                blue += argb & 0xFF;
                count++;
            }
        }

        if (count == 0L) {
            return -1;
        }

        int averageRed = (int) (red / count);
        int averageGreen = (int) (green / count);
        int averageBlue = (int) (blue / count);
        return 0xFF000000 | (averageRed << 16) | (averageGreen << 8) | averageBlue;
    }
}
