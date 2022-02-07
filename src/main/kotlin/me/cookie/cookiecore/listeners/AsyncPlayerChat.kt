package me.cookie.cookiecore.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.cookie.cookiecore.SKIP_DIALOGUE
import me.cookie.cookiecore.inDialogue
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class AsyncPlayerChat : Listener {
    @EventHandler fun onAsyncPlayerChat(chat: AsyncChatEvent){
        if(chat.player.inDialogue){
            chat.player.sendMessage(SKIP_DIALOGUE)
            chat.isCancelled = true
            return
        }
    }
}
