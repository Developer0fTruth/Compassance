package com.hexsquared.compassance.misc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemBuilder implements Serializable
{
    Material mat;
    int amount;
    byte data;

    String name;
    ArrayList<String> loreList;
    HashMap<Enchantment,Integer> enchantList;

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

    public ItemBuilder data(byte b)
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
        loreList = list;
        return this;
    }

    public ItemBuilder lore(String... texts)
    {
        ArrayList<String> textList = new ArrayList<>();
        for (String text : texts)
        {
            textList.add(Misc.formatColorString(text));
        }
        loreList = textList;
        return this;
    }

    public ItemBuilder enchant(Enchantment e, int l)
    {
        enchantList.put(e,l);
        return this;
    }

    public ItemStack toItemStack()
    {
        ItemStack item = new ItemStack(mat,amount,data);
        ItemMeta itmMeta = item.getItemMeta();
        if (name != null && !name.isEmpty())
        {
            itmMeta.setDisplayName(Misc.formatColorString(name));
        }
        if (loreList != null)
        {
            itmMeta.setLore(loreList);
        }
        for(Enchantment e : enchantList.keySet())
        {
            item.addUnsafeEnchantment(e,enchantList.get(e));
        }
        item.setItemMeta(itmMeta);
        return item;
    }

}
