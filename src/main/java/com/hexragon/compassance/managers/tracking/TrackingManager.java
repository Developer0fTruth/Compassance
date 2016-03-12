package com.hexragon.compassance.managers.tracking;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TrackingManager
{
    private final HashMap<Player, HashMap<Integer, TrackedTarget>> targetsMap;

    public TrackingManager()
    {
        targetsMap = new HashMap<>();
    }

    public void newTracking(Player p, final Entity e)
    {
        if (targetsMap.containsKey(p) && targetsMap.get(p) != null)
        {
            targetsMap.get(p).put(1, new TrackedTarget(e));
        }
        else
        {
            targetsMap.put(p, new HashMap<Integer, TrackedTarget>()
                    {{
                        put(1, new TrackedTarget(e));
                    }}
            );
        }
    }

    public void newTracking(Player p, final Location l)
    {
        if (targetsMap.containsKey(p))
        {
            targetsMap.get(p).put(1, new TrackedTarget(l));
        }
        else
        {
            targetsMap.put(p, new HashMap<Integer, TrackedTarget>()
                    {{
                        put(1, new TrackedTarget(l));
                    }}
            );
        }
    }


    public TrackedTarget getTargetOf(Player p)
    {
        if (targetsMap.get(p) == null) return null;
        return targetsMap.get(p).get(1);
    }

    public HashMap<Integer, TrackedTarget> getTargetsOf(Player p)
    {
        return targetsMap.get(p);
    }

    public void removeTrackingFrom(Player p)
    {
        if (targetsMap.containsKey(p))
        {
            targetsMap.get(p).remove(1);
        }
    }

    public void removeTrackingOf(Entity e)
    {
        for (Player p : targetsMap.keySet())
        {
            if (targetsMap.get(p).get(1) != null && targetsMap.get(p).get(1) == e)
            {
                targetsMap.get(p).remove(1);
            }
        }
    }

}