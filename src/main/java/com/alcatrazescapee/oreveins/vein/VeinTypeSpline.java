package com.alcatrazescapee.oreveins.vein;

import com.alcatrazescapee.oreveins.api.AbstractVein;
import com.alcatrazescapee.oreveins.api.AbstractVeinType;
import com.alcatrazescapee.oreveins.util.math.Spline3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;

public class VeinTypeSpline extends AbstractVeinType<VeinTypeSpline.VeinSpline>
{

    @Override
    public float getChanceToGenerate(VeinSpline vein, BlockPos pos)
    {
        double radius = 3.f;
        double distance = getDistanceToSpline(vein.getSplinePositions(), pos);

        if(distance < radius)
            return 1.f;
        else
            return 0;
    }

    @Nonnull
    @Override
    public VeinSpline createVein(World w, int chunkX, int chunkZ, Random rand)
    {
        int maxOffY = getMaxY() - getMinY() - verticalSize;
        int posY = getMinY() + verticalSize / 2 + ((maxOffY > 0) ? rand.nextInt(maxOffY) : 0);
        BlockPos pos = new BlockPos(
                chunkX * 16 + rand.nextInt(16),
                posY,
                chunkZ * 16 + rand.nextInt(16)
        );

        return new VeinSpline(this, pos, w, rand);
    }

    private double getDistanceToSpline(ArrayList<BlockPos> splinePos, BlockPos p)
    {
        double smallestDistance = Integer.MAX_VALUE;

        for(BlockPos sp: splinePos)
        {
            if(sp.distanceSq(p) < smallestDistance)
                smallestDistance = sp.distanceSq(p);
        }

        return smallestDistance;
    }

    static class VeinSpline extends AbstractVein<VeinTypeSpline>
    {
        private final Random rand;
        private boolean isInitialized = false;

        ArrayList<BlockPos> splinePositions;

        public VeinSpline(VeinTypeSpline type, BlockPos pos, World w, Random random)
        {
            super(type, pos, w, .5f * (1.f + random.nextFloat()));
            this.rand = random;
        }

        @Override
        public boolean inRange(int x, int z)
        {
            return getType().inRange(this, getPos().getX() - x, getPos().getZ() - z);
        }

        @Override
        public double getChanceToGenerate(BlockPos pos)
        {
            if(!isInitialized)
                initialize();
            return getType().getChanceToGenerate(this, pos);
        }

        private void initialize()
        {
            Spline3 spline = new Spline3();

            Vec3d p = new Vec3d(pos);
            for(int i = 0; i < 8; i++)
            {
                spline.addPoint(p);

                double randomAngle = rand.nextDouble() * 2 * Math.PI;
                double randomLength = rand.nextDouble() * 10 + 5;
                Vec3d randomDirection = new Vec3d(Math.sin(randomAngle), 0, Math.cos(randomAngle));
                randomDirection = randomDirection.scale(randomLength);

                p = p.add(randomDirection);
            }
            splinePositions = spline.toBlockList();

            isInitialized = true;
        }

        public ArrayList<BlockPos> getSplinePositions()
        {
            return splinePositions;
        }
    }
}
