package me.cookie.cookiecore.listeners

import me.cookie.cookiecore.CookieCore
import me.cookie.cookiecore.SKIP_DIALOGUE
import me.cookie.cookiecore.inDialogue
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class PlayerCommand(private val plugin: CookieCore) : Listener {
    @EventHandler fun onPlayerCommandPreProcess(event: PlayerCommandPreprocessEvent) {
        if(event.player.inDialogue){
            if (event.message.lowercase().startsWith("/skip"))
                return
            event.player.sendMessage(SKIP_DIALOGUE)
            event.isCancelled = true
            return
        }
    }
}
