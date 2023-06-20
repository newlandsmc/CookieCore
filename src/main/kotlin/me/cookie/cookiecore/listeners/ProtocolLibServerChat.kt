package me.cookie.cookiecore.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.cookie.cookiecore.inDialogue
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class ProtocolLibServerChat(plugin: JavaPlugin, joinHandler: JavaPlugin) {
    init {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.SYSTEM_CHAT){
                override fun onPacketSending(event: PacketEvent) {
                    if(event.packetType == PacketType.Play.Server.SYSTEM_CHAT){
                       //if(event.player !is Player) return
                        val player = event.player as Player

                        val packet = event.packet
                        val chatType = packet.chatTypes.read(0)

                        val modifier1 = packet.modifier.read(0)
                        //val chatComponents = packet.chat.read(0)

                        var component: Component = Component.empty()

                        /*if(chatComponents != null){
                            component = GsonComponentSerializer.gson().deserialize(chatComponents)
                        }*/

                        if(modifier1 != null){
                            component = modifier1 as Component
                        }
                        var json = GsonComponentSerializer.gson().serialize(component)
                        if(player.inDialogue){ // Mute chat if player is in dialogue
                            if(json.contains("[DIALOGUE]") && chatType == null){ // Check if message is part of dialogue
                                var formattedMessage = json
                                formattedMessage = formattedMessage
                                    .replace("{\"extra\":[{\"text\":\"", "")
                                    .replace("\"}],\"text\":\"[DIALOGUE]\"}", "")
                                val configMessage = if(formattedMessage.contains("[COOKIECORE]")) {
                                    plugin.config.getString(
                                        formattedMessage.replace("[COOKIECORE]", "")
                                    ) // uhhhhhhhhhhhhhhhhhhhhhhhhhhhh
                                } else {
                                    joinHandler.config.getString(formattedMessage)!!
                                }

                                json = GsonComponentSerializer.gson().serialize(
                                    MiniMessage.miniMessage().deserialize(
                                        configMessage!!
                                    )
                                )

                                packet.modifier.write(0, GsonComponentSerializer.gson().deserialize(json))
                                return
                            }
                            event.isCancelled = true
                            return
                        }
                    }
                }
            }
        )
    }
}