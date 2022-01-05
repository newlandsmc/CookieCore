package me.cookie.cookiecore.listeners

/*import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.event.impl.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import me.cookie.cookiecore.CookieCore
import me.cookie.cookiecore.inDialogue
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ServerChat: PacketListenerAbstract(PacketListenerPriority.NORMAL) {
    val plugin = JavaPlugin.getPlugin(CookieCore::class.java)

    override fun onPacketSend(event: PacketSendEvent) {
        if(event.packetType != PacketType.Play.Server.CHAT_MESSAGE) return
        event.isCancelled = true
        if(event.player !is Player) return

        val player = event.player as Player
        val packet = WrapperPlayServerChatMessage(event)
        println(packet.chatComponentJson)
        if(player.inDialogue){ // Mute chat if player is in dialogue
            // event.isCancelled = true
            if(packet.position == WrapperPlayServerChatMessage.ChatPosition.SYSTEM_MESSAGE &&
                packet.chatComponentJson.contains("[DIALOGUE]")){ // Check if message is part of dialogue
                var formattedMessage = message.toJsonString()
                formattedMessage = formattedMessage
                    .replace("{\"extra\":[{\"extra\":[{\"text\":", "[\"\", ")
                    .replace("],\"text\":\"\"}],\"text\":\"[DIALOGUE]\"}", "")

                packet.chatComponentJson = formattedMessage
                println("NEW: $formattedMessage")
               // player.sendMessage(GsonComponentSerializer.gson().deserialize(formattedMessage))
            }
            return
        }

    }

}*/

// Protocol lib verison, doesnt work.

/*init {
    plugin.protocolManager.addPacketListener(
        object: PacketAdapter(
            plugin, ListenerPriority.NORMAL,
            PacketType.Play.Server.CHAT
        ){
            override fun onPacketSending(event: PacketEvent?) {
                if(event == null){
                    println("null event")
                    return
                }
                if(event.packetType != PacketType.Play.Server.CHAT){
                    println("somehow not chat packet")
                    return
                }
                val packet = event.packet
                val player = event.player
                if(packet.chatComponents.read(0) == null) {
                    println("null")
                    return
                }
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
}*/