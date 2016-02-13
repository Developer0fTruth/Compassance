package com.hexragon.compassance.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarUtil
{
    private static String nmsver;

    private static Class<?> craftPlayer;
    private static Class<?> chatSer;
    private static Class<?> chatBase;
    private static Class<?> pktPOChat;
    private static Class<?> packet;
    private static Method getHandle;

    static //Caching the classes looked up.
    {
        String bukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
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
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void send(Player p, String str)
    {
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
        }
    }

//    public static void sendActionBar2(Player p, String str)
//    {
//        IChatBaseComponent c = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + str.replace("\"", "") + "\"}");
//        PacketPlayOutChat packet = new PacketPlayOutChat(c, (byte) 2);
//        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
//    }
}
