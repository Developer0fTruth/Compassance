package com.hexsquared.compassance.misc;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarUtil
{
    public static void sendActionBar(Player p, String str)
    {
        IChatBaseComponent c = ChatSerializer.a("{\"text\": \"" + str.replace("\"", "") + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(c, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
