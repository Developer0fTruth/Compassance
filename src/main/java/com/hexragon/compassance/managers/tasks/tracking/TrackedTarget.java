package com.hexragon.compassance.managers.tasks.tracking;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TrackedTarget
{
    private final Location location;
    private final TrackType trackType;
    private Entity target;

    public TrackedTarget(Entity e)
    {
        target = e;
        location = e.getLocation();
        trackType = TrackType.DYNAMIC;
    }

    public TrackedTarget(Location l)
    {
        location = l;
        trackType = TrackType.STATIC;
    }

    public Entity getTarget()
    {
        return target;
    }

    public Location getLocation()
    {
        if (trackType == TrackType.DYNAMIC)
        {
            return target.getLocation();
        }
        return location;
    }

    public TrackType getType()
    {
        return this.trackType;
    }

    public enum TrackType
    {
        DYNAMIC,
        STATIC
    }


}
