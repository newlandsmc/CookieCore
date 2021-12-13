package me.cookie.semicore

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SemiCore: JavaPlugin() {
    override fun onEnable() {
        logger.info("InfoMessage")
        MiniMessage.get()
    }
}


private val playerMenuUtilityMap = HashMap<Player, PlayerMenuUtility>()

val Player.playerMenuUtility: PlayerMenuUtility
    get() {
        val playerMenuUtility: PlayerMenuUtility
        if (!(playerMenuUtilityMap.containsKey(this))) {
            playerMenuUtility = PlayerMenuUtility(this)
            playerMenuUtilityMap[this] = playerMenuUtility
            return playerMenuUtility
        }
        return playerMenuUtilityMap[this]!!
    }