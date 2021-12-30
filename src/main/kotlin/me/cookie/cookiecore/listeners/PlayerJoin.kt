package me.cookie.cookiecore.listeners

import me.cookie.cookiecore.initInDialogue
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin: Listener {
    @EventHandler fun onPlayerJoin(event: PlayerJoinEvent){
        event.player.initInDialogue()
    }
}