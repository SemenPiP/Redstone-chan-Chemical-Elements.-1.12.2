package com.chinaex123.redstone_chemical_elements.register;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class ElementFluidBlock extends BlockFluidClassic {
    public ElementFluidBlock(Fluid fluid, String elementName) {
        this(fluid, elementName, elementName + "/" + elementName + "_fluid");
    }

    protected ElementFluidBlock(Fluid fluid, String elementName, String path) {
        super(fluid, resolveFluidMaterial(elementName));
        setRegistryName(RedstonechanChemicalElements.MODID, path);
        applyTranslationKey(RedstonechanChemicalElements.MODID + "." + path.replace('/', '.'));
        setQuantaPerBlock(8);

        int luminosity = fluid.getLuminosity();
        if (luminosity > 0) {
            applyLightLevel(Math.min(1.0F, luminosity / 15.0F));
        }
    }

    private static Material resolveFluidMaterial(String elementName) {
        String[] candidateNames = ElementCatalog.isMolten(elementName)
            ? new String[] { "field_151587_i", "LAVA" }
            : new String[] { "field_151586_h", "WATER" };

        for (String candidateName : candidateNames) {
            try {
                Field field = Material.class.getField(candidateName);
                Object resolved = field.get(null);
                if (resolved instanceof Material) {
                    Material candidate = (Material) resolved;
                    return candidate;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve a compatible fluid Material for " + elementName + '.');
    }

    private void applyTranslationKey(String translationKey) {
        invokeBlockMethod(
            new String[] { "func_149663_c", "setTranslationKey" },
            new Class<?>[] { String.class },
            translationKey
        );
    }

    private void applyLightLevel(float lightLevel) {
        invokeBlockMethod(
            new String[] { "func_149715_a", "setLightLevel" },
            new Class<?>[] { Float.TYPE },
            Float.valueOf(lightLevel)
        );
    }

    private Object invokeBlockMethod(String[] candidateNames, Class<?>[] parameterTypes, Object... arguments) {
        for (String candidateName : candidateNames) {
            try {
                Method method = findBlockMethod(candidateName, parameterTypes);
                if (method == null) {
                    continue;
                }
                return method.invoke(this, arguments);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        throw new IllegalStateException("Unable to resolve a compatible Block method for " + candidateNames[0] + '.');
    }

    private Method findBlockMethod(String name, Class<?>[] parameterTypes) {
        Class<?> current = getClass();
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
