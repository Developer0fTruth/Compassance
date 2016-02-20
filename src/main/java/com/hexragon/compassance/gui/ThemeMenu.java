package com.hexragon.compassance.gui;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.compass.generator.GeneratorInfo;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackedTarget;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ItemBuilder;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.LinkedHashSet;

public class ThemeMenu implements Listener
{
    public final String name = Utils.fmtClr("&lThemes");

    public ThemeMenu()
    {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    public void show(Player p)
    {
        Inventory inv = Bukkit.createInventory(p, 6 * 9, Utils.fmtClr(name));

        int itemSlot = 10;
        int wrapCounter = 1;

        String selectedTheme = Main.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()));

        for (String id : Main.themeManager.getThemes().keySet())
        {
            Theme t = Main.themeManager.getTheme(id);

            byte data = (byte) (selectedTheme.equalsIgnoreCase(id) ? 11 : 7);


            ItemBuilder itmBuild1 =
                    new ItemBuilder().material(Material.STAINED_GLASS_PANE).data(data).amt(1)
                            .name(Utils.fmtClr("&r" + t.meta.name))
                            .lore("&7ID: &f" + t.id, "", Utils.fmtClr(t.meta.desc))
                            .shiny(selectedTheme.equalsIgnoreCase(id));
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
                Main.mainMenu.show(p);
                return;
            }

            for (int i = 1; i <= Main.themeManager.getThemes().keySet().size(); i++)
            {
                LinkedHashSet<String> idList = new LinkedHashSet<>(Main.themeManager.getThemes().keySet());

                if (slot == itemSlot && inv.getContents()[slot].getType() != Material.AIR)
                {
                    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 1);

                    String clickedId = idList.toArray()[i - 1].toString();

                    if (e.getClick() == ClickType.RIGHT)
                    {
                        Theme t = Main.themeManager.getTheme(clickedId);
                        //PREVIEW GENERATION
                        boolean cursor = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));

                        GeneratorInfo gi;
                        TrackedTarget target = Main.trackingManager.getTargetOf(p);
                        if (target != null && target.getLocation() != null)
                            gi = new GeneratorInfo(p, p.getLocation(), target.getLocation(), p.getLocation().getYaw(), cursor);
                        else gi = new GeneratorInfo(p, null, null, p.getLocation().getYaw(), cursor);
                        p.sendMessage(t.getGenerator().getString(gi));
                        return;
                    }

                    Main.playerConfig.config.set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), clickedId);
                    Main.taskManager.refresh(p);

                    e.getWhoClicked().sendMessage(
                            Utils.fmtClr(
                                    String.format(
                                            "&a&lCOMPASS &8» &7Switching your selected theme to &r%s&7.",
                                            Main.themeManager.getTheme(clickedId).meta.name)));

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