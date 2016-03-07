package com.hexragon.compassance.files.configs;

public enum MainConfig
{
    USE_PERMISSIONS("use-permissions"),
    USE_TRACKING("use-tracking"),
    METRICS("metrics"),
    DEBUG_MODE("bleeding-edge"),
    UPDATE_CHECK("update-checker"),
    CHECKER_INTERVAL("checker-interval");

    public final String path;

    MainConfig(String s)
    {
        path = s;
    }

    public String format(Object... obj)
    {
        return String.format(path, obj);
    }
}
