package com.hexragon.compassance.commands;

import com.hexragon.compassance.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{
    public ReloadCommand()
    {
        Main.instance.getCommand("tcomp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp())
        {
            sender.sendMessage("Must be OP to use this command.");
            return true;
        }

        Main.mainConfig.load();

        Main.themeConfig.load();

        Main.playerConfig.save();
        Main.playerConfig.load();

        Main.themeManager.loadThemes();

        Main.taskManager.refreshAll();

        sender.sendMessage("Compassance configs reloaded.");

        return true;
    }
}