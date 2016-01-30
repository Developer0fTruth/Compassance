package com.hexsquared.compassance.misc;

import com.hexsquared.compassance.Compassance;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

public class Misc
{

    public static String formatColorString(String s)
    {
        return s.replace('&','\u00A7').replaceAll("%nl%", String.valueOf('\n'));
    }

    public static ItemStack createItemStack(Material material, int amount, byte data, String name, ArrayList<String> loreList)
    {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (loreList != null)
        {
            itemMeta.setLore(loreList);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ArrayList<String> stringList(String... texts)
    {
        ArrayList<String> textList = new ArrayList<>();
        Collections.addAll(textList, texts);
        return textList;
    }

    public static void logHandle(Level l, String msg)
    {
        String note = "|+| ";
        if(l == Level.SEVERE)
        {
            note = "!#! ";
        }
        Compassance.getInstance().getLogger().log(l,note+msg);
    }
}
