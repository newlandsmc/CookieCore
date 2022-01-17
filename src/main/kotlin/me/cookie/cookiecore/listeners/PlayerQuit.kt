package me.cookie.cookiecore.listeners

import me.cookie.cookiecore.inDialogue
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class PlayerQuit(private val plugin: JavaPlugin): Listener {
    @EventHandler fun onPlayerQuit(event: PlayerQuitEvent){
        object : BukkitRunnable() {
            override fun run() {
                event.player.inDialogue = false
            }
        }.runTaskLater(plugin, 20)
    }
}