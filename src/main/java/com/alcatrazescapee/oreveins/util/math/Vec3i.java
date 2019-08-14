package com.alcatrazescapee.oreveins.util.math;

import net.minecraft.util.math.BlockPos;

public class Vec3i
{
    private int x;
    private int y;
    private int z;

    public Vec3i(int pX, int pY, int pZ)
    {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    public Vec3i(BlockPos pos)
    {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vec3i add(Vec3i pV)
    {
        return new Vec3i(x + pV.x, y + pV.y, z + pV.z);
    }

    public Vec3i multiply(int pScalar)
    {
        return new Vec3i(pScalar * x, pScalar * y, pScalar * z);
    }

    public Vec3i subtract(Vec3i pV)
    {
        return this.add(pV.multiply(-1));
    }

    public int dot(Vec3i pV)
    {
        return x * pV.x + y * pV.y + z * pV.z;
    }

    public Vec3i cross(Vec3i pV)
    {
        return new Vec3i(y * pV.z - z * pV.y, z * pV.x - x * pV.z, x * pV.y - y * pV.x);
    }

    public int getLength()
    {
        return (int)Math.sqrt(x * x + y * y + z * z);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + "," + z + "," + ")";
    }
}
