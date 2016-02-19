package com.hexragon.compassance.files.configs;

public enum MainConfig
{
    USE_PERMISSIONS("use-permissions"),
    USE_TRACKING("use-tracking"),
    METRICS("metrics"),
    DEBUG_MODE("bleeding-edge");

    public final String path;

    MainConfig(String s)
    {
        path = s;
    }
}
