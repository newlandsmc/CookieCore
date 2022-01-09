package me.cookie.cookiecore


import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.cookie.cookiecore.commands.SkipDialogue
import me.cookie.cookiecore.data.sql.H2Storage
import me.cookie.cookiecore.listeners.MenuHandler
import me.cookie.cookiecore.listeners.PlayerJoin
import me.cookie.cookiecore.listeners.PlayerQuit
import me.cookie.cookiecore.listeners.ServerChat
import me.cookie.cookiecore.message.messagequeueing.MessageQueueing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CookieCore: JavaPlugin() {
    lateinit var playerSettings: H2Storage

    var expeditions: JavaPlugin? = null
    var joinHandler: JavaPlugin? = null


    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()
        WrapperPlayServerChatMessage.HANDLE_JSON = false
        PacketEvents.getAPI().settings.debug(true)
    }

    override fun onEnable() {
        playerSettings = H2Storage(this, "playerSettings")

        expeditions = Bukkit.getPluginManager().getPlugin("Expeditions") as JavaPlugin
        joinHandler = Bukkit.getPluginManager().getPlugin("JoinHandler") as JavaPlugin

        if(expeditions == null){
            logger.warning("Expeditions not found..")
        }
        if(joinHandler == null){
            logger.warning("JoinHandler not found..")
        }



        getCommand("skipdialogue")!!.setExecutor(SkipDialogue())

        playerSettings.connect()
        playerSettings.initTable(
            "playerSettings",
            listOf("UUID varchar(255)", "INDIALOGUE bool")
        )

        PacketEvents.getAPI().eventManager.registerListener(ServerChat()) // Server chat listener
        server.pluginManager.registerEvents(MenuHandler(), this)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)

        PacketEvents.getAPI().init()
        MessageQueueing().startRunnable()
        saveDefaultConfig()
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()
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