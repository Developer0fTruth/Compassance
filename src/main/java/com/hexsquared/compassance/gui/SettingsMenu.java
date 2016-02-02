package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.hexsquared.compassance.Compassance.getConfigManager;
import static com.hexsquared.compassance.Compassance.getInstance;
import static org.bukkit.Bukkit.getPluginManager;

public class SettingsMenu implements Listener
{
    public final String name = "Settings";
    String[] bl;

    public SettingsMenu()
    {
        getPluginManager().registerEvents(this, getInstance());

        bl = new String[3];
    }

    public String[] getBL(Player p)
    {
        bl[0] = String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString());
        bl[1] = String.format(PlayerSettings.COMPASS_ALWAYSON, p.getPlayer().getUniqueId().toString());
        bl[2] = String.format(PlayerSettings.COMPASS_CURSOR, p.getPlayer().getUniqueId().toString());
        return bl;
    }


    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 4*9, name);

        inv.setItem(10,
                new ItemBuilder().material(Material.COMPASS).data((byte) 0).amt(1)
                        .name("&a&lCompass")
                        .lore("","","&7Toggle the compass feature." ).toItemStack());


        inv.setItem(12,
                new ItemBuilder().material(Material.EYE_OF_ENDER).data((byte) 0).amt(1)
                .name("&b&lAlways Show")
                .lore("&7If this setting is enabled, then the","&7compass will always show. Disable it","&7and it will only show when you turn.").toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.ARROW).data((byte) 0).amt(1)
                .name("&cCursor")
                .lore("&cNot implemented yet.","","&7???" ).toItemStack());


        bl = getBL(p);

        int i = 0;
        for(String str : bl) {

            boolean b = Compassance.getConfigManager().getPlayerSettings().getBoolean(str);

            if (b)
            {
                inv.setItem(19+i,
                        new ItemBuilder().material(Material.INK_SACK).data((byte) 10).amt(1)
                        .name("&aToggle")
                        .lore("", "&2✔ &aEnabled", "&7Click to disable.").toItemStack());
            }
            else
            {
                inv.setItem(19+i,
                        new ItemBuilder().material(Material.INK_SACK).data((byte) 8).amt(1)
                        .name("&cToggle")
                        .lore("", "&4✘ &cDisabled", "&7Click to disable.").toItemStack());
            }
            i += 2;
        }

        inv.setItem(25,
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                .name("&c&lExit")
                .lore("", "&7Return to main menu.").toItemStack());

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

            bl = getBL(p);

            switch (e.getSlot())
            {
                case 25:
                    p.playSound(p.getLocation(),Sound.CLICK,0.5f,1);
                    Compassance.getMainMenu().show(p);
                default:

                    int i = 0;
                    int ii = 1;
                    for(String str : bl) {

                        boolean b = Compassance.getConfigManager().getPlayerSettings().getBoolean(str);

                        if(e.getSlot() == 19+i)
                        {
                            p.playSound(p.getLocation(), Sound.CLICK,0.5f,1);
                            if(b)
                            {
                                getConfigManager().getPlayerSettings().set(str,false);
                            }
                            else
                            {
                                getConfigManager().getPlayerSettings().set(str,true);
                            }
                            show(p);
                        }
                        i += 2;
                    }
            }
        }
    }

}