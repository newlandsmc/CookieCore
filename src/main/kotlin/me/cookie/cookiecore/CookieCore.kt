package me.cookie.cookiecore


import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.cookie.cookiecore.commands.SkipDialogue
import me.cookie.cookiecore.data.sql.H2Storage
import me.cookie.cookiecore.listeners.*
import me.cookie.cookiecore.message.messagequeueing.MessageQueueing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CookieCore: JavaPlugin() {
    lateinit var playerSettings: H2Storage

    private var expeditions: JavaPlugin? = null
    private var joinHandler: JavaPlugin? = null


    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()
        WrapperPlayServerChatMessage.HANDLE_JSON = false
        PacketEvents.getAPI().settings.debug(true)
        PacketEvents.getAPI().settings.checkForUpdates(false)
    }

    override fun onEnable() {
        playerSettings = H2Storage(this, "playerSettings")

        if(Bukkit.getPluginManager().getPlugin("Expeditions") != null){
            expeditions = Bukkit.getPluginManager().getPlugin("Expeditions") as JavaPlugin

        }else{
            logger.warning("Expeditions not found..")
        }

        if(Bukkit.getPluginManager().getPlugin("JoinHandler") != null){
            joinHandler = Bukkit.getPluginManager().getPlugin("JoinHandler") as JavaPlugin
        }else{
            logger.warning("JoinHandler not found..")
        }

        playerSettings.connect()
        playerSettings.initTable(
            "playerSettings",
            listOf("UUID varchar(255)", "INDIALOGUE bool")
        )

        registerCommands()
        registerEvents()

        PacketEvents.getAPI().init()
        MessageQueueing(this).startRunnable()
        saveDefaultConfig()
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()
    }

    private fun registerCommands(){
        getCommand("skipdialogue")!!.setExecutor(SkipDialogue(this))
    }

    private fun registerEvents(){
        PacketEvents.getAPI().eventManager.registerListener(ServerChat(joinHandler!!)) // Server chat listener
        server.pluginManager.registerEvents(MenuHandler(), this)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(this), this)
        server.pluginManager.registerEvents(AsyncPlayerChat(), this)
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