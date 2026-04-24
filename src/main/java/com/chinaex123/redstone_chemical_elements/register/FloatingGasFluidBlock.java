package com.chinaex123.redstone_chemical_elements.register;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FloatingGasFluidBlock extends ElementFluidBlock {
    private static final int MAX_RISE_Y = 240;

    public FloatingGasFluidBlock(Fluid fluid, String elementName) {
        super(fluid, elementName);
        setTickRate(6);
    }

    private int getCeilingY(IBlockAccess world) {
        if (world instanceof World) {
            return Math.min(MAX_RISE_Y, ((World) world).getActualHeight() - 1);
        }
        return MAX_RISE_Y;
    }

    private boolean isWithinRiseLimit(IBlockAccess world, BlockPos pos) {
        return pos.getY() <= getCeilingY(world);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return isWithinRiseLimit(world, pos) && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        return isWithinRiseLimit(world, pos) && super.displaceIfPossible(world, pos);
    }

    @Override
    public int place(World world, BlockPos pos, FluidStack fluidStack, boolean doPlace) {
        if (!isWithinRiseLimit(world, pos)) {
            return 0;
        }
        return super.place(world, pos, fluidStack, doPlace);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!isWithinRiseLimit(world, pos)) {
            world.setBlockToAir(pos);
            return;
        }
        super.updateTick(world, pos, state, rand);
    }
}
