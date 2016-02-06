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

        inv.setItem(10,
                new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
                        .name("&a&lTheming")
                        .lore("", "&7Select and customize your compass to", "&7your favorite style.").toItemStack());

        boolean b = Compassance.instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()));
        inv.setItem(12,
                new ItemBuilder()
                        .material(b ? Material.EYE_OF_ENDER : Material.ENDER_PEARL)
                        .data((byte) 0)
                        .amt(1)
                        .name("&b&lTracking")
                        .lore(
                                "",
                                b ? "&7Tracking a player:" : null,
                                b ? "    &f/compass trk pl &b-player" : "&fIf you enable tracking of locations",
                                b ? "" : "&fand other players, &cother players",
                                b ? "&7Tracking a location:" : "&ccan attempt to track you.",
                                b ? "    &f/compass trk loc &b-x -y -z" : null,
                                b ? "%nl%&fYou are susceptible to being%nl%&ftracked for other people.%nl% " : "",
                                b ? "&7Click to disable." : "&7Click to enable."
                        ).toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.NAME_TAG).data((byte) 0).amt(1)
                        .name("&9&lSettings")
                        .lore("", "&7Click to enter the preferences", "&7menu to adjust personal settings.").toItemStack());

        inv.setItem(16,
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                        .name("&c&lExit")
                        .lore("", "&7Close the menu.").toItemStack());


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
                case 10:
                    Compassance.instance.themeMenu.show(p);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    break;
                case 12:
                    String str = String.format(PlayerSettings.SETTING_TRACKING, p.getPlayer().getUniqueId().toString());
                    boolean b = Compassance.instance.configManager.getPlayerSettings().getBoolean(str);
                    Compassance.instance.configManager.getPlayerSettings().set(str, !b);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    show(p);

                    if (b)
                    {
                        instance.trackingManager.removeTrackingFrom(p);
                        instance.trackingManager.removeTrackingOf(p);
                    }

                    break;
                case 14:
                    Compassance.instance.settingsMenu.show(p);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    break;
                case 16:
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    p.closeInventory();
                    break;

            }
        }
    }

}
