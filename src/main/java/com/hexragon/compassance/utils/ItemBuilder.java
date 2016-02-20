package com.hexragon.compassance.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ItemBuilder implements Serializable
{
    private final HashMap<Enchantment, Integer> enchantList;
    private Material mat;
    private int amount;
    private short data;
    private String name;
    private ArrayList<String> loreList;
    private boolean shiny;

    public ItemBuilder()
    {
        mat = Material.AIR;
        amount = 1;
        data = 0;
        enchantList = new HashMap<>();
    }

    public ItemBuilder material(Material m)
    {
        mat = m;
        return this;
    }

    public ItemBuilder data(short b)
    {
        data = b;
        return this;
    }

    public ItemBuilder amt(int a)
    {
        amount = a;
        return this;
    }

    public ItemBuilder name(String n)
    {
        name = n;
        return this;
    }

    public ItemBuilder lore(ArrayList<String> list)
    {
        if (list == null)
        {
            return this;
        }
        loreList = list;
        return this;
    }

    public ItemBuilder lore(String... texts)
    {
        ArrayList<String> list = new ArrayList<>();
        for (String text : texts)
        {
            if (text == null)
            {
                continue;
            }
            String[] lines = ChatColor.translateAlternateColorCodes('&', text).split("%nl%");
            Collections.addAll(list, lines);
        }
        loreList = list;
        return this;
    }

    public ItemBuilder enchant(Enchantment e, int l)
    {
        enchantList.put(e, l);
        return this;
    }

    public ItemBuilder shiny(boolean b)
    {
        shiny = b;
        return this;
    }

    public ItemStack toItemStack()
    {
        ItemStack item = new ItemStack(mat, amount, data);
        ItemMeta itmMeta = item.getItemMeta();
        if (name != null && !name.isEmpty())
        {
            itmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (shiny)
        {
            itmMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (loreList != null)
        {
            itmMeta.setLore(loreList);
        }
        for (Enchantment e : enchantList.keySet())
        {
            item.addUnsafeEnchantment(e, enchantList.get(e));
        }
        item.setItemMeta(itmMeta);
        return item;
    }

}
