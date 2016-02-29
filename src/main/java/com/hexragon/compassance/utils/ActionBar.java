package com.hexragon.compassance.utils;

import com.hexragon.compassance.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBar
{
    private static final String nmsver;

    private static Class<?> craftPlayer;
    private static Class<?> chatSer;
    private static Class<?> chatBase;
    private static Class<?> pktPOChat;
    private static Class<?> packet;
    private static Method getHandle;

    private static boolean works = true;

    static
    {
        String bukkitPackage = Main.instance.getServer().getClass().getPackage().getName();
        nmsver = bukkitPackage.substring(bukkitPackage.lastIndexOf(".") + 1);

        try
        {
            craftPlayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            pktPOChat = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");

            packet = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_"))
            {
                chatSer = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                chatBase = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
            }
            else
            {
                chatSer = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                chatBase = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
            }
            getHandle = craftPlayer.getDeclaredMethod("getHandle");
            Main.logger.info("Success! ActionBar lookup complete.");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            works = false;
            Main.logger.severe("Warning! ActionBar lookup failed! Are you running the latest Minecraft 1.8?");
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
            Object castPlayer = craftPlayer.cast(p);
            Object pkt;
            Object o;

            if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_"))
            {
                Method a = chatSer.getDeclaredMethod("a", String.class);
                o = chatBase.cast(a.invoke(chatSer, "{\"text\": \"" + str + "\"}"));
            }
            else
            {
                o = chatSer.getConstructor(new Class<?>[]{String.class}).newInstance(str);
            }
            pkt = pktPOChat.getConstructor(new Class<?>[]{chatBase, byte.class}).newInstance(o, (byte) 2);

            Object handle = getHandle.invoke(castPlayer);
            Field pCon = handle.getClass().getDeclaredField("playerConnection");
            Object pc = pCon.get(handle);
            Method sendPkt = pc.getClass().getDeclaredMethod("sendPacket", packet);
            sendPkt.invoke(pc, pkt);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Main.logger.severe("Warning! ActionBar execution failed! Are you running the latest Minecraft 1.8?");
            works = false;
        }
    }
}

