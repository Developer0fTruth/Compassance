package com.hexragon.compassance.gui;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.utils.ItemBuilder;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class MainMenu implements Listener
{
    private final String name;
    private ArrayList<Player> users = new ArrayList<>();

    public MainMenu()
    {
        String s = Utils.fmtClr("&lCompassance");
        if (s.length() > 32)
        {
            name = s.substring(0, 31);
        }
        else
        {
            name = s;
        }

        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
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

        boolean b = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_TRACKING.format(p.getPlayer().getUniqueId().toString()));

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

        if (!Main.mainConfig.config.getBoolean(MainConfig.USE_TRACKING.path))
        {
            trkItem.lore("", "&cTracking is globally disabled.", "");
        }

        inv.setItem(12, trkItem.toItemStack());

        inv.setItem(14,
                new ItemBuilder().material(Material.NAME_TAG).data((byte) 0).amt(1)
                        .name("&9&lSettings")
                        .lore("", "&7Click to enter the preferences", "&7menu to adjust personal text.").toItemStack());

        inv.setItem(16,
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                        .name("&c&lExit")
                        .lore("", "&7Close the menu.").toItemStack());

        users.add(p);
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();

        if (inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
        {
            e.setCancelled(true);

            if (e.getSlot() < 0) return;

            if (inv.getContents()[e.getSlot()] != null)
            {
                switch (e.getSlot())
                {
                    case 10:
                        Main.themeMenu.show(p);
                        break;
                    case 12:
                        String str = PlayerConfig.SETTING_TRACKING.format(p.getPlayer().getUniqueId().toString());
                        boolean b = Main.playerConfig.config.getBoolean(str);
                        Main.playerConfig.config.set(str, !b);

                        if (!Main.mainConfig.config.getBoolean(MainConfig.USE_TRACKING.path))
                        {
                            Main.playerConfig.config.set(str, false);
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cTracking is globally disabled."));
                        }
                        if (b)
                        {
                            Main.trackingManager.removeTrackingFrom(p);
                            Main.trackingManager.removeTrackingOf(p);
                        }
                        show(p);

                        break;
                    case 14:
                        Main.settingsMenu.show(p);
                        break;
                    case 16:
                        p.closeInventory();
                        break;

                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();

        if (inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
        {
            users.remove(p);
        }
    }

}
