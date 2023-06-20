package me.cookie.cookiecore.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.cookie.cookiecore.formatMinimessage
import me.cookie.cookiecore.inDialogue
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class AsyncPlayerChat : Listener {
    @EventHandler fun onAsyncPlayerChat(chat: AsyncChatEvent){
        if(chat.player.inDialogue){
            chat.player.sendMessage(Component.text("[DIALOGUE]")
                .append("[COOKIECORE]skip-dialogue".formatMinimessage())) // Genius!
            chat.isCancelled = true
            return
        }
    }
}
