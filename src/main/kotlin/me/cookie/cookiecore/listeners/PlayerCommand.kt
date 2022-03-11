package me.cookie.cookiecore.listeners

import me.cookie.cookiecore.CookieCore
import me.cookie.cookiecore.formatMinimessage
import me.cookie.cookiecore.inDialogue
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class PlayerCommand(private val plugin: CookieCore) : Listener {
    @EventHandler fun onPlayerCommandPreProcess(event: PlayerCommandPreprocessEvent) {
        if(event.player.inDialogue){
            if (
                event.message.lowercase().startsWith("/skip") ||
                event.message.lowercase().startsWith("/skipdialogue")
            ) return

            event.player.sendMessage(Component.text("[DIALOGUE]")
                .append("[COOKIECORE]skip-dialogue".formatMinimessage())) // Genius!
            event.isCancelled = true

            return
        }
    }
}
