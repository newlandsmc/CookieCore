package me.cookie.cookiecore

import me.cookie.cookiecore.commands.SkipDialogue
import me.cookie.cookiecore.listeners.MenuHandler
import me.cookie.cookiecore.message.messagequeueing.MessageQueueing
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SemiCore: JavaPlugin() {
    override fun onEnable() {
        logger.info("InfoMessage")
        getCommand("skipdialogue")!!.setExecutor(SkipDialogue())

        server.pluginManager.registerEvents(MenuHandler(), this)
        MessageQueueing().startRunnable()
        saveDefaultConfig()
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