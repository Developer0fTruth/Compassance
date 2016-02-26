package com.hexragon.compassance.managers.tasks.tracking;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TrackingManager
{
    private final HashMap<Player, TrackedTarget> targetsMap;

    public TrackingManager()
    {
        targetsMap = new HashMap<>();
    }

    public void newTracking(Player p, Entity e)
    {
        if (targetsMap.containsKey(p))
        {
            targetsMap.remove(p);
        }
        targetsMap.put(p, new TrackedTarget(e));
    }

    public void newTracking(Player p, Location l)
    {
        if (targetsMap.containsKey(p))
        {
            targetsMap.remove(p);
        }
        targetsMap.put(p, new TrackedTarget(l));
    }

    public TrackedTarget getTargetOf(Player p)
    {
        return targetsMap.get(p);
    }

    public void removeTrackingFrom(Player p)
    {
        if (targetsMap.containsKey(p))
        {
            targetsMap.remove(p);
        }
    }

    public void removeTrackingOf(Entity e)
    {
        for (Player p : targetsMap.keySet())
        {
            if (targetsMap.get(p).getTarget() == e)
            {
                targetsMap.remove(p);
            }
        }
    }

}