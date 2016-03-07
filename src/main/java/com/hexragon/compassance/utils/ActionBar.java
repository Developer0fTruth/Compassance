package com.hexragon.compassance.utils;

import com.hexragon.compassance.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBar
{
    private static Class<?> craftPlayer;
    private static Class<?> chatSer;
    private static Class<?> chatBase;
    private static Class<?> pktPOChat;
    private static Class<?> packet;
    private static Method getHandle;
    private static Method a;

    private static boolean works = true;

    static
    {
        setup();
    }

    public static void setup()
    {
        try
        {
            craftPlayer = Class.forName("org.bukkit.craftbukkit." + Main.nmsver + ".entity.CraftPlayer");
            pktPOChat = Class.forName("net.minecraft.server." + Main.nmsver + ".PacketPlayOutChat");

            packet = Class.forName("net.minecraft.server." + Main.nmsver + ".Packet");

            if ((Main.nmsver.equalsIgnoreCase("v1_8_R1") || !Main.nmsver.startsWith("v1_8_")) && !Main.nmsver.startsWith("v1_9_"))
            {
                chatSer = Class.forName("net.minecraft.server." + Main.nmsver + ".ChatSerializer");
                chatBase = Class.forName("net.minecraft.server." + Main.nmsver + ".IChatBaseComponent");
                a = chatSer.getDeclaredMethod("a", String.class);
            }
            else
            {
                chatSer = Class.forName("net.minecraft.server." + Main.nmsver + ".ChatComponentText");
                chatBase = Class.forName("net.minecraft.server." + Main.nmsver + ".IChatBaseComponent");
            }
            getHandle = craftPlayer.getDeclaredMethod("getHandle");

            Main.logger.severe("ActionBar reflection success.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Main.logger.severe("Warning! ActionBar reflection failed!");
            works = false;
        }
    }

    public static void send(Player p, String str)
    {
        if (!works)
        {
            return;
        }
        try
        {
            Object pl = craftPlayer.cast(p);
            Object o;
            Object pkt;
            if ((Main.nmsver.equalsIgnoreCase("v1_8_R1") || !Main.nmsver.startsWith("v1_8_")) && !Main.nmsver.startsWith("v1_9_"))
            {
                o = chatBase.cast(a.invoke(chatSer, "{\"text\": \"" + str + "\"}"));
            }
            else
            {
                o = chatSer.getConstructor(new Class<?>[]{String.class}).newInstance(str);
            }
            pkt = pktPOChat.getConstructor(new Class<?>[]{chatBase, byte.class}).newInstance(o, (byte) 2);
            Object handle = getHandle.invoke(pl);
            Field pCon = handle.getClass().getDeclaredField("playerConnection");
            Object pc = pCon.get(handle);
            Method sendPacket = pc.getClass().getDeclaredMethod("sendPacket", packet);
            sendPacket.invoke(pc, pkt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Main.logger.severe("Warning! ActionBar execution failed. Reflection functions threw an exception.");
            works = false;
        }
    }
}

