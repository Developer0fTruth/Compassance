package com.hexragon.compassance.gui;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.managers.files.configs.MainConfig;
import com.hexragon.compassance.managers.files.configs.PlayerConfig;
import com.hexragon.compassance.misc.ItemBuilder;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MainMenu implements Listener
{
    public final String name = Misc.fmtClr("&lCompassance");

    public MainMenu()
    {
        Compassance.instance.getServer().getPluginManager().registerEvents(this, Compassance.instance);
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 3 * 9, name);

        inv.setItem(10,
                new ItemBuilder().material(Material.PAINTING).data((byte) 0).amt(1)
                        .name("&a&lTheming")
                        .lore(
                                "",
                                "&7Select and customize your compass to", "&7your favorite style.",
                                "",
                                "&7Alternatively, you can change your",
                                "&7theme using &f/compass theme -id"
                        ).toItemStack());

        boolean b = Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()));

        ItemBuilder trkItem = new ItemBuilder()
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
                );

        if (!Compassance.mainConfig.config.getBoolean(MainConfig.USE_TRACKING))
        {
            trkItem.lore("", "&cTracking is globally disabled.", "");
        }

        inv.setItem(12, trkItem.toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.NAME_TAG).data((byte) 0).amt(1)
                        .name("&9&lSettings")
                        .lore("", "&7Click to enter the preferences", "&7menu to adjust personal files.").toItemStack());

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
                    Compassance.themeMenu.show(p);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);
                    break;
                case 12:
                    String str = String.format(PlayerConfig.SETTING_TRACKING, p.getPlayer().getUniqueId().toString());
                    boolean b = Compassance.playerConfig.config.getBoolean(str);
                    Compassance.playerConfig.config.set(str, !b);
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);

                    if (!Compassance.mainConfig.config.getBoolean(MainConfig.USE_TRACKING))
                    {
                        Compassance.playerConfig.config.set(str, false);
                        p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cTracking is globally disabled."));
                    }
                    if (b)
                    {
                        Compassance.trackingManager.removeTrackingFrom(p);
                        Compassance.trackingManager.removeTrackingOf(p);
                    }

                    show(p);

                    break;
                case 14:
                    Compassance.settingsMenu.show(p);
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
