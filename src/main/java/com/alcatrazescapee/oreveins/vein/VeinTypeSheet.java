package com.alcatrazescapee.oreveins.vein;

import com.alcatrazescapee.oreveins.api.AbstractVein;
import com.alcatrazescapee.oreveins.api.AbstractVeinType;
import com.alcatrazescapee.oreveins.util.Plane3i;
import com.alcatrazescapee.oreveins.util.Vec3i;
import com.alcatrazescapee.oreveins.world.WorldGenVeins;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class VeinTypeSheet extends AbstractVeinType<VeinTypeSheet.VeinSheet>
{
    int jaggedness = 4;

    @Override
    public float getChanceToGenerate(VeinSheet vein, BlockPos pos)
    {
        double dx = vein.getPos().getX() - pos.getX();
        double dy = vein.getPos().getY() - pos.getY();
        double dz = vein.getPos().getZ() - pos.getZ();
        double distance2d = Math.sqrt(dx * dx + dz * dz);

        // Angle between direction to block and x-axis
        double dirX = pos.getX() - vein.getPos().getX();
        double dirY = pos.getZ() - vein.getPos().getZ();
        double angle = dirX < 0 ? Math.atan(dirY / dirX) : Math.atan(dirY / dirX) + Math.PI;

        int maxRadius = horizontalSize;
        int minRadius = maxRadius / jaggedness;

        int scale = (maxRadius - minRadius) / 2;
        int offset = minRadius + scale;
        double radius = offset + scale * vein.getSineValue(angle);

        if(distance2d < radius &&
                vein.plane.getIntersectionWithLine(new Vec3i(pos), new Vec3i(0, 1, 0)).subtract(new Vec3i(pos)).getLength() < verticalSize)
            return 1;
        else
            return 0;
    }

    @Nonnull
    @Override
    public VeinSheet createVein(World w, int chunkX, int chunkZ, Random rand)
    {
        int maxOffY = getMaxY() - getMinY() - verticalSize;
        int posY = getMinY() + verticalSize / 2 + ((maxOffY > 0) ? rand.nextInt(maxOffY) : 0);
        BlockPos pos = new BlockPos(
                chunkX * 16 + rand.nextInt(16),
                posY,
                chunkZ * 16 + rand.nextInt(16)
        );

        return new VeinSheet(this, pos, w, rand);
    }

    static class VeinSheet extends AbstractVein<VeinTypeSheet>
    {
        private final Random rand;
        private boolean isInitialized = false;

        private Plane3i plane;
        // Values controlling our sine function
        private int s1;
        private int s2;
        private int s3;

        public VeinSheet(VeinTypeSheet type, BlockPos pos, World w, Random random) {
            super(type, pos, w, .5f * (1.f + random.nextFloat()));
            this.rand = new Random(random.nextLong());
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
            {
                initialize();
            }
            return getType().getChanceToGenerate(this, pos);
        }

        private void initialize()
        {
            // Generate normal based on approximate terrain normal at veins origin
            Vec3i p0 = new Vec3i(WorldGenVeins.getTopBlockIgnoreVegetation(this.world, pos.north(10)));
            Vec3i p1 = new Vec3i(WorldGenVeins.getTopBlockIgnoreVegetation(this.world, pos.north(-10)));
            Vec3i p2 = new Vec3i(WorldGenVeins.getTopBlockIgnoreVegetation(this.world, pos.east(10)));
            Vec3i p3 = new Vec3i(WorldGenVeins.getTopBlockIgnoreVegetation(this.world, pos.east(-10)));

            Vec3i pX = p0.subtract(p1);
            Vec3i pY = p2.subtract(p3);

            Vec3i normal = pX.cross(pY);
            Vec3i point = new Vec3i(this.pos.getX(), this.pos.getY(), this.pos.getZ());
            plane = new Plane3i(point, normal);

            s1 = rand.nextInt(4);
            s2 = rand.nextInt(4);
            s3 = rand.nextInt(4);

            isInitialized = true;
        }

        public double getSineValue(double angle)
        {
            return (1.f/3.f) * Math.sin(s1 * angle) + (1.f/3.f) * Math.sin(s2 * angle) + (1.f/3.f) * Math.sin(s3 * angle);
        }
    }
}
