package me.cookie.cookiecore.listeners

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.event.impl.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import me.cookie.cookiecore.formatMinimessage
import me.cookie.cookiecore.formatPlayerPlaceholders
import me.cookie.cookiecore.inDialogue
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ServerChat(private val joinHandler: JavaPlugin): PacketListenerAbstract(PacketListenerPriority.NORMAL) {
    override fun onPacketSend(event: PacketSendEvent) {
        if(event.packetType != PacketType.Play.Server.CHAT_MESSAGE) return
        if(event.player !is Player) return

        val player = event.player as Player
        val packet = WrapperPlayServerChatMessage(event)

        if(!player.hasPlayedBefore()){
            if(packet.chatComponentJson.contains( // Disable first-join message for joining player
                    GsonComponentSerializer.gson().serialize(
                        joinHandler.config.getString("first-join")!!
                            .formatPlayerPlaceholders(player)
                            .formatMinimessage()
                    )
                )) {
                event.isCancelled = true
            }
        }

        if(player.inDialogue){ // Mute chat if player is in dialogue
            if(packet.position == WrapperPlayServerChatMessage.ChatPosition.SYSTEM_MESSAGE &&
                packet.chatComponentJson.contains("[DIALOGUE]")){ // Check if message is part of dialogue

                var formattedMessage = packet.chatComponentJson
                formattedMessage = formattedMessage
                    .replace("{\"extra\":[{\"text\":\"", "")
                    .replace("\"}],\"text\":\"[DIALOGUE]\"}", "")

                packet.chatComponentJson = GsonComponentSerializer.gson().serialize(
                    MiniMessage.get().parse(
                        joinHandler.config.getString(formattedMessage)!!
                    )
                )
                return
            }
            event.isCancelled = true
            return
        }

    }

}