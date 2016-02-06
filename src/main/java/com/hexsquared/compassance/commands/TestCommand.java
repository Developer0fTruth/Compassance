package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.generator.CompassStringGenerator;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.ItemBuilder;
import com.hexsquared.compassance.misc.Misc;
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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.hexsquared.compassance.Compassance.instance;

public class TestCommand implements CommandExecutor
{
    public TestCommand()
    {
        instance.getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("data"))
        {
            Theme theme = instance.themeManager.getTheme(args[1]);

            if (theme == null)
            {
                return true;
            }

            sender.sendMessage("Displaying parsed data for theme " + theme.getId());

            sender.sendMessage(Misc.formatColor("&9&lMeta :"));
            sender.sendMessage(Misc.formatColor("  &8Name : &r" + theme.getName()));
            sender.sendMessage(Misc.formatColor("  &8Desc : &r" + theme.getDesc()));
            sender.sendMessage(Misc.formatColor("  &8Main Pattern Map : &r") + theme.getData_main_PatternMap());

            sender.sendMessage(Misc.formatColor("&9&lDirect Replacers :"));
            for (String s : theme.getData_DirectReplacers().keySet())
            {
                sender.sendMessage(Misc.formatColor("  &8" + s + " : &r" + theme.getData_DirectReplacers().get(s)));
            }
            sender.sendMessage(Misc.formatColor("&9&lSub-Patterns :"));
            for (String s : theme.getData_subPatternMap().keySet())
            {
                sender.sendMessage(Misc.formatColor("  &8" + s + " : &r") + theme.getData_subPatternMap().get(s));
                for (String s2 : theme.getData_subPatternReplacers().get(s).keySet())
                {
                    sender.sendMessage(Misc.formatColor("    &8" + s2 + " : &r" + theme.getData_subPatternReplacers().get(s).get(s2)));
                }
            }

            sender.sendMessage(Misc.formatColor("&9&lPost Processing : "));
            sender.sendMessage("  " + theme.getFinal_PatternMap());
            for (String s : theme.getFinal_DirectReplacers().keySet())
            {
                sender.sendMessage(Misc.formatColor("  &8" + s + " : &r" + theme.getFinal_DirectReplacers().get(s)));
            }

//            sender.sendMessage("");
//            sender.sendMessage(theme.getStringMapFull());
//            sender.sendMessage(String.valueOf(theme.getStringMapArray().length));

            if (sender instanceof Player)
            {
                CompassStringGenerator gen = new CompassStringGenerator(theme, ((Player) sender).getLocation().getYaw(), false);
                ActionBarUtil.sendActionBar((Player) sender, gen.getString());
            }

            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("perm"))
        {
            Player p = (Player) sender;

            Theme theme = instance.themeManager.getTheme(args[1]);
            if (theme == null)
            {
                return true;
            }

            p.sendMessage(String.valueOf(Misc.permHandle(p, theme.getPerm(), true)));


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
                Compassance.instance.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                instance.compassTaskManager.refresh(p);
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
                    Compassance.instance.trackingManager.newTracking(p, pl);
                    break;
                }
            }
            instance.compassTaskManager.refresh(p);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle"))
        {
            Player p = (Player) sender;
            //String uuid = p.getUniqueId().toString();
            boolean b = instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()));
            if (b)
            {
                instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), false);
            }
            else
            {
                instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), true);
            }
            instance.compassTaskManager.refresh(p);
            ActionBarUtil.sendActionBar(p, "");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("menu"))
        {
            //sender.sendMessage(instance.configManager.getThemeConfig().getDefaults().getString("version") + " default vs have " + instance.configManager.getThemeConfig().getString("version"));
            //sender.sendMessage(Bukkit.getVersion()+" getVersion and getBukkitVersion "+Bukkit.getBukkitVersion());
            Compassance.instance.mainMenu.show((Player) sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("tracklist"))
        {
            Player p = (Player) sender;

            double rad = Math.pow(10, 2);

            Map<Double, Entity> list = new HashMap<>();

            for (Entity e : p.getWorld().getEntities())
            {
                if(list.size() >= 28) break;
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
                                .name("&7&l"+e.getName())
                                .lore("&fD: &b"+d1,"&fUUID: &b"+e.getUniqueId().toString())
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