package com.hexragon.compassance.files.text;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.utils.FileUtil;

import java.io.File;

public class GearboxText
{
    private final Main instance;

    private final String fileName;

    public GearboxText(Main i, String n)
    {
        instance = i;
        fileName = n;
    }

    public void load()
    {
        File file = new File(instance.getDataFolder(), fileName);

        if (file.getParentFile().mkdirs())
        {
            instance.getLogger().info(String.format("Making directory for file '%s'.", fileName));
        }
        FileUtil.write(instance.getResource(fileName), file);
    }
}
