package com.hexragon.compassance.gui;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.misc.Misc;
import com.hexragon.compassance.managers.settings.paths.PlayerSettings;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.misc.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ThemeMenu implements Listener
{
    public final String name = "Themes";

    public ThemeMenu()
    {
        Compassance.instance.getServer().getPluginManager().registerEvents(this, Compassance.instance);
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 6 * 9, name);

        int itemSlot = 10;
        int wrapCounter = 1;

        String selectedTheme = Compassance.instance.configManager.getPlayerSettings().getString(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()));

        for (String id : Compassance.instance.themeManager.getThemes().keySet())
        {
            Theme t = Compassance.instance.themeManager.getTheme(id);

            byte data = (byte) (selectedTheme.equalsIgnoreCase(id) ? 11 : 7);

            ItemBuilder itmBuild1 =
                    new ItemBuilder().material(Material.STAINED_GLASS_PANE).data(data).amt(1)
                            .name(Misc.formatColor("&r" + t.getName()))
                            .lore("", Misc.formatColor(t.getDesc()));
            inv.setItem(itemSlot, itmBuild1.toItemStack());

            wrapCounter++;
            itemSlot += wrapCounter > 7 ? 3 : 1;
            if (wrapCounter > 7) wrapCounter = 1;
        }

        ItemBuilder itmBuild1 =
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
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

        if (inv.getName().equalsIgnoreCase(name))
        {
            e.setCancelled(true);

            int itemSlot = 10;
            int wrapCounter = 1;

            if (e.getSlot() == 49)
            {
                p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                Compassance.instance.mainMenu.show(p);
                return;
            }

            for (int i = 1; i <= Compassance.instance.themeManager.getThemes().keySet().size(); i++)
            {

                if (slot == itemSlot && inv.getContents()[slot].getType() != Material.AIR)
                {
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);

                    String clickedId = Compassance.instance.themeManager.getThemes().keySet().toArray()[i - 1].toString();

                    Compassance.instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), clickedId);
                    Compassance.instance.compassTaskManager.refresh(p);

                    e.getWhoClicked().sendMessage(Misc.formatColor(String.format("&a&lCOMPASS &8» &7Switching your selected theme to &r%s&7.", Compassance.instance.themeManager.getTheme(clickedId).getName())));

                    show(p);
                    return;
                }

                wrapCounter++;
                itemSlot += wrapCounter > 7 ? 3 : 1;
                if (wrapCounter > 7) wrapCounter = 1;
            }
        }
    }

}