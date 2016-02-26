package com.hexragon.compassance.commands;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.ItemBuilder;
import com.hexragon.compassance.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TestCommand implements CommandExecutor
{
    public TestCommand()
    {
        Main.instance.getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp())
        {
            sender.sendMessage("Must be OP to use this command.");
            return true;
        }

        if (args.length == 0)
        {
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("data"))
        {
            Theme theme = Main.themeManager.getTheme(args[1]);

            if (theme == null)
            {
                return true;
            }

            sender.sendMessage("Displaying parsed data for theme " + theme.id);

            sender.sendMessage(Utils.fmtClr("&9&lMeta :"));
            sender.sendMessage(Utils.fmtClr("  &8Name : &r" + theme.meta.name));
            sender.sendMessage(Utils.fmtClr("  &8Desc : &r" + theme.meta.desc));
            sender.sendMessage(Utils.fmtClr("  &8Main Pattern Map : &r") + theme.meta.patternMap);

            sender.sendMessage(Utils.fmtClr("&9&lDirect Replacers :"));
            for (String s : theme.data.replacers.keySet())
            {
                sender.sendMessage(Utils.fmtClr("  &8" + s + " : &r" + theme.data.replacers.get(s)));
            }
            sender.sendMessage(Utils.fmtClr("&9&lSub-Patterns :"));
            for (String s : theme.data.sub.pattern.keySet())
            {
                sender.sendMessage(Utils.fmtClr("  &8" + s + " : &r") + theme.data.sub.pattern.get(s));
                for (String s2 : theme.data.sub.replacers.get(s).keySet())
                {
                    sender.sendMessage(Utils.fmtClr("    &8" + s2 + " : &r" + theme.data.sub.replacers.get(s).get(s2)));
                }
            }

            sender.sendMessage(Utils.fmtClr("&9&lPost Processing : "));
            sender.sendMessage("  " + theme.post.pattern);
            for (String s : theme.post.replacers.keySet())
            {
                sender.sendMessage(Utils.fmtClr("  &8" + s + " : &r" + theme.post.replacers.get(s)));
            }

            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("perm"))
        {
            Player p = (Player) sender;

            Theme theme = Main.themeManager.getTheme(args[1]);
            if (theme == null)
            {
                return true;
            }

            p.sendMessage(String.valueOf(Utils.permHandle(p, theme.meta.permission, true)));


            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("mydir"))
        {
            Player p = (Player) sender;
            p.sendMessage(String.valueOf(p.getLocation().getYaw()));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("dirto"))
        {
            Player p = (Player) sender;
            Location ploc = p.getLocation();
            Location loc = new Location(p.getWorld(), 0, 65, 0);

            float angle = (float) (Math.atan2(loc.getX() - ploc.getX(), loc.getZ() - ploc.getZ()));
            angle = (float) (-(angle / Math.PI) * 360) / 2 + 180;

            p.sendMessage(String.valueOf(angle));
            return true;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("loc"))
        {
            Player p = (Player) sender;
            try
            {
                Double x = Double.parseDouble(args[1]);
                Double y = Double.parseDouble(args[2]);
                Double z = Double.parseDouble(args[3]);
                Main.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                Main.taskManager.refresh(p);
            }
            catch (Exception e)
            {
                p.sendMessage("error");
            }
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("player"))
        {
            Player p = (Player) sender;

            String targetName = args[1];
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                if (pl.getName().equalsIgnoreCase(targetName))
                {
                    Main.trackingManager.newTracking(p, pl);
                    break;
                }
            }
            Main.taskManager.refresh(p);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle"))
        {
            Player p = (Player) sender;
            //String uuid = p.getUniqueId().toString();
            boolean b = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()));
            if (b)
            {
                Main.playerConfig.config.set(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()), false);
            }
            else
            {
                Main.playerConfig.config.set(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()), true);
            }
            Main.taskManager.refresh(p);
            ActionBar.send(p, "");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("menu"))
        {
            Main.mainMenu.show((Player) sender);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("version"))
        {
            sender.sendMessage("Theme: " + Main.themeConfig.config.getDefaults().getString("version") + " default vs have " + Main.themeConfig.config.getString("version"));
            sender.sendMessage("Player: " + Main.playerConfig.config.getDefaults().getString("version") + " default vs have " + Main.playerConfig.config.getString("version"));

            sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, "%compassance_p_string%"));
            sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, "%compassance_p_string_theme_default%"));
            sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, "%compassance_p_string_theme_midnight-light%"));
            sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, "%compassance_p_selectedtheme% %compassance_p_target% %compassance_p_target_location%"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("time"))
        {
            Player p = (Player) sender;
            Date d = new Date();

            p.sendMessage(new SimpleDateFormat("EEEE").format(d)); //<dow>
            p.sendMessage(new SimpleDateFormat("E").format(d)); //<a/dow>

            p.sendMessage(new SimpleDateFormat("MMMM").format(d)); //<month>
            p.sendMessage(new SimpleDateFormat("MMM").format(d)); //<a/month>
            p.sendMessage(new SimpleDateFormat("M").format(d)); //<m/month>

            p.sendMessage(new SimpleDateFormat("d").format(d)); //<day>

            p.sendMessage(new SimpleDateFormat("a").format(d)); //<marker>
            p.sendMessage(new SimpleDateFormat("s").format(d)); //<second>
            p.sendMessage(new SimpleDateFormat("m").format(d)); //<minute>
            p.sendMessage(new SimpleDateFormat("h").format(d)); //<hour>

        }

        if (args.length == 1 && args[0].equalsIgnoreCase("tracklist"))
        {
            Player p = (Player) sender;

            double rad = Math.pow(10, 2);

            Map<Double, Entity> list = new HashMap<>();

            for (Entity e : p.getWorld().getEntities())
            {
                if (list.size() >= 28) break;
                if (e == p) continue;
                if (e instanceof LivingEntity)
                {
                    double d = p.getLocation().distanceSquared(e.getLocation());
                    if (d <= rad)
                    {
                        list.put(d, e);
                    }
                }
            }
            list = new TreeMap<>(list);

            Inventory inv = Bukkit.createInventory(null, 6 * 9, "Debug");

            int i = 1;
            int itemSlot = 10;
            int wrapCounter = 1;

            for (double d : list.keySet())
            {
                if (i > 28) break;

                double d1 = Math.sqrt(d);
                d1 = Math.round(d1 * 100);
                d1 /= 100;

                Entity e = list.get(d);

                ItemStack spawnEgg = new SpawnEgg(list.get(d).getType()).toItemStack();

                inv.setItem(itemSlot,
                        new ItemBuilder()
                                .amt(1)
                                .material(spawnEgg.getType())
                                .data(spawnEgg.getDurability())
                                .name("&7&l" + e.getName())
                                .lore("&fD: &b" + d1, "&fUUID: &b" + e.getUniqueId().toString())
                                .toItemStack());

                i++;
                wrapCounter++;
                itemSlot += wrapCounter > 7 ? 3 : 1;
                if (wrapCounter > 7) wrapCounter = 1;
            }

            p.openInventory(inv);

            return true;
        }

        return false;
    }
}