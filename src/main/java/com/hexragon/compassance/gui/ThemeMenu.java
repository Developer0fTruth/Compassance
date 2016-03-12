package com.hexragon.compassance.gui;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.language.Tags;
import com.hexragon.compassance.managers.compass.CompassGenerator;
import com.hexragon.compassance.managers.compass.CompassGenerator.GeneratorInfo;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ItemBuilder;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ThemeMenu implements Listener
{

    private final String name;
    private ArrayList<Player> users = new ArrayList<>();

    public ThemeMenu()
    {
        String s = Utils.fmtClr("&lThemes");
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
        Inventory inv = Bukkit.createInventory(p, 6 * 9, Utils.fmtClr(name));

        int itemSlot = 10;
        int wrapCounter = 1;

        String selectedId = Main.playerConfig.config.getString(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()));

        LinkedHashSet<Theme> themeList = Main.themeManager.themesAccessibleTo(p); // If player do not have permission of themes, it is omitted.
        for (Theme t : themeList)
        {
            byte data = (byte) (selectedId.equalsIgnoreCase(t.id) ? 11 : 7);

            ItemBuilder itmBuild1 =
                    new ItemBuilder().material(Material.STAINED_GLASS_PANE).data(data).amt(1)
                            .name(Utils.fmtClr("&r" + t.meta.name))
                            .lore("&7ID: &f" + t.id, "", Utils.fmtClr(t.meta.desc), "", "&fClick &7to select.", "&fRight Click &7to preview.")
                            .hideEnchants(selectedId.equals(t.id))
                            .enchant(selectedId.equals(t.id) ? Enchantment.DURABILITY : null, 1);
            inv.setItem(itemSlot, itmBuild1.toItemStack());

            wrapCounter++;
            itemSlot += wrapCounter > 7 ? 3 : 1;
            if (wrapCounter > 7) wrapCounter = 1;
        }

        ItemBuilder itmBuild1 =
                new ItemBuilder().material(Material.BARRIER).data((byte) 0).amt(1)
                        .name("&c&lExit")
                        .lore("", "&7Return to meta menu.");
        inv.setItem(49, itmBuild1.toItemStack());

        users.add(p);
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if (inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
        {
            e.setCancelled(true);

            if (slot < 0) return;

            if (inv.getContents()[slot] != null && inv.getName().equalsIgnoreCase(name) && inv.getHolder() == p && users.contains(p))
            {
                int itemSlot = 10;
                int wrapCounter = 1;

                if (e.getSlot() == 49)
                {
                    Main.mainMenu.show(p);
                    return;
                }

                LinkedHashSet<Theme> themeList = Main.themeManager.themesAccessibleTo(p); // If player do not have permission of themes, it is omitted.

                for (int i = 1; i <= themeList.size(); i++)
                {
                    if (slot == itemSlot && inv.getContents()[slot].getType() != Material.AIR)
                    {
                        Theme t = (Theme) themeList.toArray()[i - 1];

                        // PREVIEW GENERATION
                        if (e.getClick() == ClickType.RIGHT)
                        {
                            boolean cursor = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));

                            GeneratorInfo gi;
                            gi = new CompassGenerator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p), cursor);
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&7Showing preview of " + t.meta.name + "&7."));
                            p.sendMessage(t.getGenerator().getString(gi));
                            return;
                        }
                        Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), t.id);
                        Main.taskManager.refresh(p);

                        e.getWhoClicked().sendMessage(
                                Utils.fmtClr(
                                        String.format(
                                                Tags.prefix + "&7Switching your selected theme to &r%s&7.",
                                                t.meta.name)));
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