package com.hexragon.compassance.commands;

import com.hexragon.compassance.Compassance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{
    public ReloadCommand()
    {
        Compassance.instance.getCommand("tcomp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Compassance.instance.themeConfig.load();
        Compassance.instance.playerConfig.load();
        Compassance.instance.themeManager.loadThemes();

        Compassance.instance.compassTaskManager.refreshAll();

        return true;
    }
}