package com.hexsquared.compassance.managers.compass.tasks.tracking;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TrackedTarget
{

    private Entity target;
    private Location location;
    private TrackingType type;

    enum TrackingType
    {
        DYNAMIC,
        STATIC
    }

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

    public Location getLocation()
    {
        if(type == TrackingType.DYNAMIC)
        {
            location = target.getLocation();
        }
        return location;
    }

}
