package me.cookie.cookiecore.listeners

import me.cookie.cookiecore.inDialogue
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuit: Listener {
    @EventHandler fun onPlayerQuit(event: PlayerQuitEvent){
        event.player.inDialogue = false
    }
}