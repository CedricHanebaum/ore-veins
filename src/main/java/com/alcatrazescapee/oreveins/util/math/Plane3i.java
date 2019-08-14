package com.alcatrazescapee.oreveins.util.math;

import net.minecraft.util.math.BlockPos;

public class Plane3i
{
    private Vec3i p;
    private Vec3i n;

    public Plane3i(Vec3i pPoint, Vec3i pNormal)
    {
        this.p = pPoint;
        this.n = pNormal;
    }

    public boolean isOnPlane(Vec3i pPoint)
    {
        Vec3i p0 = p.subtract(pPoint);
        return p0.dot(n) == 0;
    }

    public boolean isOnPlane(BlockPos pPos)
    {
        return isOnPlane(new Vec3i(pPos.getX(), pPos.getY(), pPos.getZ()));
    }

    public Vec3i getIntersectionWithLine(Vec3i pPoint, Vec3i pNormal)
    {
        if(pNormal.dot(n) == 0) return new Vec3i(0, Integer.MIN_VALUE, 0);
        int d = n.dot(p.subtract(pPoint)) / pNormal.dot(n);
        return pPoint.add(pNormal.multiply(d));
    }

    public Vec3i getNormal()
    {
        return n;
    }

    public Vec3i getPoint()
    {
        return p;
    }
}
