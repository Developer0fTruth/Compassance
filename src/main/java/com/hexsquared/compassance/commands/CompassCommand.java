package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.Compassance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.instance;

public class CompassCommand implements CommandExecutor
{
    public CompassCommand()
    {
        instance.getCommand("compass").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Compassance.instance.mainMenu.show((Player) sender);
        return true;
    }
}