package com.alcatrazescapee.oreveins.api;

import java.util.Random;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A default vein implementation. Used for veins that don't require any additional instance parameters
 *
 * @author AlcatrazEscapee
 */
@SuppressWarnings("WeakerAccess")
@ParametersAreNonnullByDefault
public class DefaultVein extends AbstractVein<DefaultVeinType>
{
    public DefaultVein(DefaultVeinType type, BlockPos pos, World w, Random rand)
    {
        super(type, pos, w, rand);
    }

    @Override
    public boolean inRange(int x, int z)
    {
        return getType().inRange(this, pos.getX() - x, pos.getZ() - z);
    }

    @Override
    public double getChanceToGenerate(BlockPos pos)
    {
        return getType().getChanceToGenerate(this, pos);
    }
}
