package com.hexsquared.compassance.managers.compass.tasks.tracking;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TrackedTarget
{

    private Entity target;
    private Location location;
    private TrackingType type;

    public TrackedTarget(Entity e)
    {
        target = e;
        location = e.getLocation();
        type = TrackingType.DYNAMIC;
    }

    public TrackedTarget(Location l)
    {
        location = l;
        type = TrackingType.STATIC;
    }

    public Entity getTarget()
    {
        return target;
    }

    public Location getLocation()
    {
        if (type == TrackingType.DYNAMIC)
        {
            return target.getLocation();
        }
        return location;
    }

    enum TrackingType
    {
        DYNAMIC,
        STATIC
    }

}
