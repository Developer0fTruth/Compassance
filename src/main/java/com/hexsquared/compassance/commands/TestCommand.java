package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.gui.MenuGUI;
import com.hexsquared.compassance.managers.compass.CompassStringGenerator;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.*;

public class TestCommand implements CommandExecutor {
    public TestCommand() {
        getInstance().getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
        {
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("data"))
        {
            Theme theme = getThemeManager().getTheme(args[1]);

            if (theme == null) {
                return true;
            }

            sender.sendMessage("Displaying data for theme:\n" + theme.getName());

            sender.sendMessage("Name: " + theme.getName());
            sender.sendMessage(Misc.formatColorString("Desc: " + theme.getDesc()));
            sender.sendMessage("PatternMap: " + theme.getMain_PatternMap());

            sender.sendMessage("DirectReplacers:");
            for (String s : theme.getPattern_DirectReplacers().keySet()) {
                sender.sendMessage(Misc.formatColorString("   " + s + " : " + theme.getPattern_DirectReplacers().get(s)));
            }
            sender.sendMessage("SubPatternMap:");
            for (String s : theme.getPattern_subPatternMap().keySet()) {
                sender.sendMessage("   " + s + " : " + theme.getPattern_subPatternMap().get(s));
                for (String s2 : theme.getPattern_subPatternReplacers().get(s).keySet()) {
                    sender.sendMessage("         " + s2 + " : " + theme.getPattern_subPatternReplacers().get(s).get(s2));
                }
            }

            sender.sendMessage(theme.getStringMapFull());
            sender.sendMessage(String.valueOf(theme.getStringMapArray().length));

            if (sender instanceof Player) {
                CompassStringGenerator gen = new CompassStringGenerator(theme, ((Player) sender).getLocation().getYaw(), false);
                ActionBarUtil.sendActionBar((Player) sender, gen.getString());
            }

            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle"))
        {
            Player p = (Player) sender;
            //String uuid = p.getUniqueId().toString();
            boolean b = getConfigManager().getPlayerSettings().getBoolean(String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString()));
            if(b)
            {
                getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString()),false);
            }
            else
            {
                getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString()),true);
            }
            getCompassTaskManager().refresh(p);
            ActionBarUtil.sendActionBar(p,"");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("version"))
        {
            //sender.sendMessage(getConfigManager().getThemeConfig().getDefaults().getString("version") + " default vs have " + getConfigManager().getThemeConfig().getString("version"));
            //sender.sendMessage(Bukkit.getVersion()+" getVersion and getBukkitVersion "+Bukkit.getBukkitVersion());
            new MenuGUI().show((Player) sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("theme"))
        {
            Player p = (Player) sender;
            getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()), args[1]);
            getCompassTaskManager().refresh(p);
            return true;
        }

        return false;
    }
}