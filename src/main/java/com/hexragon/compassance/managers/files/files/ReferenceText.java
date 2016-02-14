package com.hexragon.compassance.managers.files.files;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.misc.FileUtil;
import com.hexragon.compassance.misc.Misc;

import java.io.File;
import java.util.logging.Level;

public class ReferenceText
{
    private final String fileName;
    private File file;

    public ReferenceText()
    {
        this.fileName = "test/references.txt";
    }

    public void forceCopy()
    {
        file = new File(Compassance.instance.getDataFolder(), fileName);

        if (file.getParentFile().mkdirs())
        {
            Misc.logHandle(Level.INFO, String.format("Making directory for file '%s'.", fileName));
        }
        FileUtil.copyFile(Compassance.instance.getResource(fileName), file);
    }
}
