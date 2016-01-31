package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.hexsquared.compassance.Compassance.getInstance;
import static org.bukkit.Bukkit.getPluginManager;

public class SettingsMenu implements Listener
{
    public final String name = "Settings";

    public SettingsMenu()
    {
        getPluginManager().registerEvents(this, getInstance());
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 4*9, name);

        ItemBuilder itmBuild1 = new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
                .name("&a&lTarget Tracking")
                .lore("&cNot implemented yet.","","&7Track a target on your compass." );
        inv.setItem(13, itmBuild1.toItemStack());

        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();

        if(inv.getName().equalsIgnoreCase(name))
        {
            e.setCancelled(true);
        }
    }

}