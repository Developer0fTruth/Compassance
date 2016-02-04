package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.hexsquared.compassance.Compassance.instance;

public class MainMenu implements Listener
{
    public final String name = "Compassance";

    public MainMenu()
    {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 3 * 9, name);

        inv.setItem(11,
                new ItemBuilder().material(Material.NAME_TAG).data((byte) 0).amt(1)
                        .name("&a&lSettings")
                        .lore("", "&7Click to enter the preferences", "&7menu to adjust personal settings.").toItemStack());

        inv.setItem(13,
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                        .name("&c&lExit")
                        .lore("", "&7Close the menu.").toItemStack());

        inv.setItem(15,
                new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
                        .name("&a&lTheming")
                        .lore("", "&7Select and customize your compass to", "&7your favorite style.").toItemStack());

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

            switch (e.getSlot())
            {
                case 11:
                    Compassance.instance.settingsMenu.show(p);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    break;
                case 13:
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    p.closeInventory();
                    break;
                case 15:
                    Compassance.instance.themeMenu.show(p);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    break;
            }
        }
    }

}
