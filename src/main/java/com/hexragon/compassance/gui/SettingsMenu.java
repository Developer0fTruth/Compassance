package com.hexragon.compassance.gui;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.utils.ItemBuilder;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class SettingsMenu implements Listener
{
    private final String name;
    private ArrayList<Player> users = new ArrayList<>();

    public SettingsMenu()
    {
        String s = Utils.fmtClr("&lSettings");
        if (s.length() > 32)
        {
            name = s.substring(0, 31);
        }
        else
        {
            name = s;
        }

        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
    }

    private String[] settingsPathsOf(Player p)
    {
        String[] bl = new String[3];
        bl[0] = ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString());
        bl[1] = ConfigurationPaths.PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString());
        bl[2] = ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString());
        return bl;
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 4 * 9, name);

        inv.setItem(10,
                new ItemBuilder().material(Material.COMPASS).data((byte) 0).amt(1)
                        .name("&9&lCompass")
                        .lore("", "", "&7Toggle the compass feature.").toItemStack());

        inv.setItem(12,
                new ItemBuilder().material(Material.BEACON).data((byte) 0).amt(1)
                        .name("&b&lAlways Update")
                        .lore("&7If this setting is enabled, then the", "&7compass will always show. Disable it", "&7and it will only show when you turn.").toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.ARROW).data((byte) 0).amt(1)
                        .name("&cCursor")
                        .lore("&7Show a cursor right in the center of", "&7the action bar.").toItemStack());


        String[] bl = settingsPathsOf(p);

        int i = 0;
        for (String str : bl)
        {

            boolean b = Main.playerConfig.config.getBoolean(str);

            inv.setItem(19 + i,
                    new ItemBuilder()
                            .material(Material.INK_SACK)
                            .data(b ? (byte) 10 : (byte) 8)
                            .amt(1)
                            .name(b ? "&aToggle Option" : "&cToggle Option")
                            .lore(
                                    "",
                                    b ? "&2✔ &aEnabled" : "&4✘ &cDisabled",
                                    b ? "&7Click to disable." : "&7Click to enable.").toItemStack());
            i += 2;
        }

        inv.setItem(25,
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                        .name("&c&lExit")
                        .lore("", "&7Return to meta menu.").toItemStack());

        users.add(p);
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();

        if (inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
        {
            e.setCancelled(true);

            if (e.getSlot() < 0) return;

            if (inv.getContents()[e.getSlot()] != null && inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
            {
                String[] bl = settingsPathsOf(p);

                switch (e.getSlot())
                {
                    case 25:
                        Main.mainMenu.show(p);
                    default:

                        int i = 0;
                        for (String str : bl)
                        {
                            boolean b = Main.playerConfig.config.getBoolean(str);

                            if (e.getSlot() == 19 + i)
                            {
                                Main.playerConfig.config.set(str, !b);
                                Main.taskManager.refresh(p);
                                show(p);
                                return;
                            }
                            i += 2;
                        }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();

        if (inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
        {
            users.remove(p);
        }
    }

}