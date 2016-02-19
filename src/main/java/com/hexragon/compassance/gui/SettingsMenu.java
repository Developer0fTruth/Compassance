package com.hexragon.compassance.gui;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.misc.ItemBuilder;
import com.hexragon.compassance.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SettingsMenu implements Listener
{
    private final String name = Utils.fmtClr("&lSettings");
    private String[] bl;

    public SettingsMenu()
    {
        Compassance.instance.getServer().getPluginManager().registerEvents(this, Compassance.instance);

        bl = new String[3];
    }

    private String[] getBL(Player p)
    {
        bl[0] = PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString());
        bl[1] = PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString());
        bl[2] = PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString());
        return bl;
    }


    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 4 * 9, name);

        inv.setItem(10,
                new ItemBuilder().material(Material.COMPASS).data((byte) 0).amt(1)
                        .name("&a&lCompass")
                        .lore("", "", "&7Toggle the compass feature.").toItemStack());


        inv.setItem(12,
                new ItemBuilder().material(Material.BEACON).data((byte) 0).amt(1)
                        .name("&b&lAlways Show")
                        .lore("&7If this setting is enabled, then the", "&7compass will always show. Disable it", "&7and it will only show when you turn.").toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.ARROW).data((byte) 0).amt(1)
                        .name("&cCursor")
                        .lore("&cNot implemented yet.", "", "&7???").toItemStack());


        bl = getBL(p);

        int i = 0;
        for (String str : bl)
        {

            boolean b = Compassance.playerConfig.config.getBoolean(str);

            inv.setItem(19 + i,
                    new ItemBuilder()
                            .material(Material.INK_SACK)
                            .data(b ? (byte) 10 : (byte) 8)
                            .amt(1)
                            .name(b ? "&aToggle" : "&cToggle")
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

        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();

        if (inv.getName().equalsIgnoreCase(name))
        {
            e.setCancelled(true);

            bl = getBL(p);

            switch (e.getSlot())
            {
                case 25:
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    Compassance.mainMenu.show(p);
                default:

                    int i = 0;
                    for (String str : bl)
                    {
                        boolean b = Compassance.playerConfig.config.getBoolean(str);

                        if (e.getSlot() == 19 + i)
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                            Compassance.playerConfig.config.set(str, !b);
                            Compassance.compassTaskManager.refresh(p);
                            show(p);
                            return;
                        }
                        i += 2;
                    }
            }
        }
    }

}