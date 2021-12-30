package me.cookie.cookiecore.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.cookie.cookiecore.CookieCore
import me.cookie.cookiecore.inDialogue
import me.cookie.cookiecore.toJsonString
import me.cookie.cookiecore.toPlainString
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.plugin.java.JavaPlugin


class ServerChat {
    val plugin = JavaPlugin.getPlugin(CookieCore::class.java)
    init {
        plugin.protocolManager.addPacketListener(
            object: PacketAdapter(
                plugin, ListenerPriority.NORMAL,
                PacketType.Play.Server.CHAT
            ){
                override fun onPacketSending(event: PacketEvent?) {
                    val packet = event!!.packet
                    val player = event.player
                    val message = GsonComponentSerializer.gson().deserialize(packet.chatComponents.read(0).json)
                    println(packet.chatComponents.read(0).json)
                    if(player.inDialogue){ // Mute chat if player is in dialogue
                        event.isCancelled = true
                        if(packet.bytes.readSafely(0) == 1.toByte() &&
                            message.toPlainString().startsWith("[DIALOGUE]")){ // Check if message is part of dialogue
                            val formattedMessage = message.toJsonString()
                            formattedMessage.replace("[DIALOGUE]", "")
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(formattedMessage))
                        }
                        return
                    }
                }
            }
        )
    }
}