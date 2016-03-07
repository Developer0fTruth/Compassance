package com.hexragon.compassance.commands;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.language.Tags;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class AdminCommand implements CommandExecutor
{
    public AdminCommand()
    {
        Main.plugin.getCommand("compass-admin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp())
        {
            sender.sendMessage("Must be OP to use this command.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help"))
        {
            ArrayList<String> arr = new ArrayList<>();
            arr.add("&8&m--------------------------------------------------");
            arr.add("");
            arr.add("        &9&lCompassance Admin &7v&f&n" + Main.plugin.getDescription().getVersion());
            arr.add("");
            arr.add("    &f/compass reload &8- &7Reload configurations.");
            arr.add("");
            arr.add("&8&m--------------------------------------------------");

            for (String s : arr)
            {
                sender.sendMessage(Utils.fmtClr(s));
            }
            return true;
        }
        else if (args[0].equalsIgnoreCase("reload"))
        {

            Main.mainConfig.load();

            Main.themeConfig.load();

            Main.playerConfig.save();
            Main.playerConfig.load();

            Main.themeManager.loadThemes();

            Main.taskManager.refreshAll();

            sender.sendMessage("Compassance configs reloaded.");

            return true;
        }
        else
        {
            sender.sendMessage(Utils.fmtClr(Tags.prefix + "&cInvalid arguments."));
            sender.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass help"));
            return true;
        }
    }
}