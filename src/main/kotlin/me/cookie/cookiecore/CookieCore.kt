package me.cookie.cookiecore

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import me.cookie.cookiecore.commands.SkipDialogue
import me.cookie.cookiecore.data.sql.H2Storage
import me.cookie.cookiecore.listeners.MenuHandler
import me.cookie.cookiecore.listeners.PlayerJoin
import me.cookie.cookiecore.listeners.PlayerQuit
import me.cookie.cookiecore.listeners.ServerChat
import me.cookie.cookiecore.message.messagequeueing.MessageQueueing
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CookieCore: JavaPlugin() {
    lateinit var playerSettings: H2Storage
    lateinit var protocolManager: ProtocolManager

    override fun onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager()
        playerSettings = H2Storage(this, "playerSettings")

        getCommand("skipdialogue")!!.setExecutor(SkipDialogue())

        playerSettings.connect()
        playerSettings.initTable(
            "playerSettings",
            listOf("UUID varchar(255)", "INDIALOGUE bool")
        )

        ServerChat() // Server chat listener
        server.pluginManager.registerEvents(MenuHandler(), this)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)

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