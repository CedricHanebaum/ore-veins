package com.alcatrazescapee.oreveins.util.math;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Spline3
{
    private ArrayList<Vec3d> splinePoints;
    
    /**
     * Add a point to the spline.
     * A valid spline needs at least 4 points. Calling getValueAt for a spline with less than 4 points is
     * undefined behaviour.
     * @param pPoint to point to add to the spline
     */
    public void addPoint(Vec3d pPoint)
    {
        splinePoints.add(pPoint);
    }

    /**
     * Use this as the boundary when iterating over the spline
     * @return the number of points on the spline, excluding the control points, starting at 0.
     */
    public double getSplineLength()
    {
        return splinePoints.size() - 3;
    }

    /**
     * Returns the value of the spline at the given position. Calling this function for splines with less than 4 points
     * results in undefined behaviour (read OutOfBounds exceptions).
     * @param t The relative position on the spline [0-N]
     *          where 0 is at the first non control point and N at the last non control point.
     * @return The value of the spline at position t.
     */
    public Vec3d getValueAt(double t)
    {
        int p1 = (int)t + 1;
        int p2 = p1 + 1;
        int p3 = p1 + 2;
        int p0 = p1 - 1;

        t = t - (int)t;

        double tt = t * t;
        double ttt = t * t * t;

        // relative influences for each point
        double q0 =       -ttt + 2.f * tt - t;
        double q1 =  3.f * ttt - 5.f * tt     + 2.f;
        double q2 = -3.f * ttt + 4.f * tt + t;
        double q3 =        ttt -       tt;

        double tx = .5f * (q0 * splinePoints.get(p0).x + q1 * splinePoints.get(p1).x + q2 * splinePoints.get(p2).x + q3 * splinePoints.get(p3).x);
        double ty = .5f * (q0 * splinePoints.get(p0).y + q1 * splinePoints.get(p1).y + q2 * splinePoints.get(p2).y + q3 * splinePoints.get(p3).y);
        double tz = .5f * (q0 * splinePoints.get(p0).z + q1 * splinePoints.get(p1).z + q2 * splinePoints.get(p2).z + q3 * splinePoints.get(p3).z);

        return new Vec3d(tx, ty, tz);
    }

}
