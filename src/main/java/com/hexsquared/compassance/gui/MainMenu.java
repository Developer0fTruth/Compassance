package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.hexsquared.compassance.Compassance.getConfigManager;
import static com.hexsquared.compassance.Compassance.getInstance;
import static org.bukkit.Bukkit.getPluginManager;

public class MainMenu implements Listener
{
    public final String name = "Compassance";

    public MainMenu()
    {
        getPluginManager().registerEvents(this, getInstance());
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 3*9, name);


        ItemBuilder itmBuild = new ItemBuilder().material(Material.NAME_TAG).data((byte) 0).amt(1)
                .name("&a&lSettings")
                .lore("", "&7Click to enter the preferences","&7menu to adjust personal settings.");
        inv.setItem(11, itmBuild.toItemStack());

//        ItemBuilder itmBuild1 = new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
//                .name("&a&lTarget Tracking")
//                .lore("&cNot implemented yet.","","&7Track a target on your compass." );
//        inv.setItem(13, itmBuild1.toItemStack());

        ItemBuilder itmBuild2 = new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
                .name("&a&lTheming")
                .lore("", "&7Select and customize your compass to","&7your favorite style.");
        inv.setItem(15, itmBuild2.toItemStack());

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

            p.sendMessage(""+e.getSlot());

            switch (e.getSlot())
            {
                case 11:
                    Compassance.getSettingsMenu().show(p);
                    break;
                case 15:
                    Compassance.getThemeMenu().show(p);
                    break;
            }
        }
    }

}
