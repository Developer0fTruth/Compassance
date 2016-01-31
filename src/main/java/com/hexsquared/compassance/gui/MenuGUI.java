package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class MenuGUI implements Listener
{
    private Inventory inv;

    public MenuGUI()
    {
        inv = Bukkit.createInventory(null, 6*9, "Compassance");
    }

    public void show(Player p)
    {
        ItemBuilder itmBuild = new ItemBuilder()
                .material(Material.PAINTING)
                .data((byte) 0)
                .amt(1)
                .name("&a&lTheming")
                .lore("", "&7Select and customize your compass to","&7your favorite style.");
        inv.setItem(15, itmBuild.toItemStack());

        p.openInventory(inv);
    }

}
