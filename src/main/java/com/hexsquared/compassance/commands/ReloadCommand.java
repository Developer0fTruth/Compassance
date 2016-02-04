package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.Compassance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.hexsquared.compassance.Compassance.instance;

public class ReloadCommand implements CommandExecutor
{
    public ReloadCommand()
    {
        instance.getCommand("tcomp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Compassance.instance.configManager.loadThemeConfig();
        Compassance.instance.configManager.loadPlayerConfig();
        Compassance.instance.themeManager.loadThemes();

        Compassance.instance.compassTaskManager.refreshAll();

        return true;
    }
}