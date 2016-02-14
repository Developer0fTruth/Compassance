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
        if (!sender.isOp())
        {
            sender.sendMessage("Must be OP to use this command.");
            return true;
        }

        Compassance.mainConfig.load();

        Compassance.themeConfig.load();

        Compassance.playerConfig.save();
        Compassance.playerConfig.load();

        Compassance.themeManager.loadThemes();

        Compassance.compassTaskManager.refreshAll();

        sender.sendMessage("Compassance configs reloaded.");

        return true;
    }
}