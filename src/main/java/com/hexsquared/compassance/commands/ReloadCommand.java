package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.Compassance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.hexsquared.compassance.Compassance.getInstance;

public class ReloadCommand implements CommandExecutor
{
    public ReloadCommand()
    {
        getInstance().getCommand("tcomp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Compassance.getCompassTaskManager().endTaskAll();

        Compassance.getConfigManager().loadThemeConfig();
        Compassance.getConfigManager().loadPlayerConfig();
        Compassance.getThemeManager().loadThemes();

        Compassance.getCompassTaskManager().newTaskAll();

        return true;
    }
}