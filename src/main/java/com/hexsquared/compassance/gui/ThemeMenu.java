package com.hexsquared.compassance.gui;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ItemBuilder;
import com.hexsquared.compassance.misc.Misc;
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

public class ThemeMenu implements Listener
{
    public final String name = "Themes";

    public ThemeMenu()
    {
        getPluginManager().registerEvents(this, getInstance());
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 6*9, name);

        int i = 10;
        int ii = 1;

        String selectedTheme =  getConfigManager().getPlayerSettings().getString(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()));

        for (String id : Compassance.getThemeManager().getThemes().keySet())
        {
            Theme t = Compassance.getThemeManager().getTheme(id);

            byte data = 7;

            if (selectedTheme.equalsIgnoreCase(id))
            {
                data = 11;
            }

            ItemBuilder itmBuild1 = new ItemBuilder().material(Material.STAINED_GLASS_PANE).data(data).amt(1)
                    .name(Misc.formatColor("&r"+t.getName()))
                    .lore("", Misc.formatColor(t.getDesc()));
            inv.setItem(i, itmBuild1.toItemStack());

            ii++;

            int add = ii > 7 ? 3 : 1;

            if(ii > 7)
            {
                ii = 1;
            }

            i += add;
        }

        ItemBuilder itmBuild1 = new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                .name("&c&lExit")
                .lore("", "&7Return to main menu.");
        inv.setItem(49, itmBuild1.toItemStack());

        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if(inv.getName().equalsIgnoreCase(name))
        {
            e.setCancelled(true);

            int ii = 0;
            int iii = 0;

            for(int i = 1;i <= Compassance.getThemeManager().getThemes().keySet().size();i++)
            {

                ii++;

                if(ii > 7)
                {
                    ii = 1;
                    iii++;
                }

                if (slot == 10 + (i - 1) + (iii) && inv.getContents()[slot].getType() != Material.AIR)
                {
                    p.playSound(p.getLocation(), Sound.CLICK,0.5f,1);

                    String clickedId = Compassance.getThemeManager().getThemes().keySet().toArray()[i-1].toString();

                    getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()), clickedId);
                    getCompassTaskManager().refresh(p);

                    e.getWhoClicked().sendMessage(Misc.formatColor(String.format("&a&lCOMPASS &8» &7Switching your selected theme to &r%s&7.",Compassance.getThemeManager().getTheme(clickedId).getName())));

                    show(p);
                }
            }

            if (e.getSlot() == 49)
            {
                p.playSound(p.getLocation(),Sound.CLICK,0.5f,1);
                Compassance.getMainMenu().show(p);
            }
        }
    }

}