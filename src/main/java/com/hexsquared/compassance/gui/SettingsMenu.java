package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.hexsquared.compassance.Compassance.getCompassTaskManager;
import static com.hexsquared.compassance.Compassance.getConfigManager;
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

        ItemBuilder itmBuild1 = new ItemBuilder().material(Material.COMPASS).data((byte) 0).amt(1)
                .name("&a&lCompass")
                .lore("","","&7Toggle the compass feature." );
        inv.setItem(10, itmBuild1.toItemStack());

        ItemBuilder itmBuild2 = new ItemBuilder().material(Material.EYE_OF_ENDER).data((byte) 0).amt(1)
                .name("&b&lAlways Show")
                .lore("&7If this setting is enabled, then the","&7compass will always show. Disable it","&7and it will only show when you turn.");
        inv.setItem(12, itmBuild2.toItemStack());

        ItemBuilder itmBuild3 = new ItemBuilder().material(Material.ARROW).data((byte) 0).amt(1)
                .name("&cCursor")
                .lore("&cNot implemented yet.","","&7???" );
        inv.setItem(14, itmBuild3.toItemStack());

        if(getConfigManager().getPlayerSettings().getBoolean(String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString())))
        {
            ItemBuilder itmBuild = new ItemBuilder().material(Material.INK_SACK).data((byte) 10).amt(1)
                    .name("&aToggle")
                    .lore("","&2✔ &aEnabled","&7Click to disable.");
            inv.setItem(19, itmBuild.toItemStack());
        }
        else
        {
            ItemBuilder itmBuild = new ItemBuilder().material(Material.INK_SACK).data((byte) 8).amt(1)
                    .name("&cToggle")
                    .lore("","&4✘ &cDisabled","&7Click to disable.");
            inv.setItem(19, itmBuild.toItemStack());
        }



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

            switch (e.getSlot())
            {
                case 19:

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
                    Compassance.getSettingsMenu().show(p);
                    p.playSound(p.getLocation(), Sound.CLICK,0.5f,1);
                    break;
            }
        }
    }

}