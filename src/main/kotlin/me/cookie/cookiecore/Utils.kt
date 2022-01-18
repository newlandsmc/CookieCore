package me.cookie.cookiecore

import me.cookie.cookiecore.data.Values
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.*


private val plugin = JavaPlugin.getPlugin(CookieCore::class.java)

// Component to plain string
fun Component.toPlainString(): String = PlainTextComponentSerializer.plainText().serialize(this)
fun Component.toJsonString(): String = GsonComponentSerializer.gson().serialize(this)
// Remove dashes from UUID
fun UUID.cleanUp(): String{
    return this.toString().replace("-", "")
}

// Player placeholder formatting
fun String.formatPlayerPlaceholders(player: Player): String {
    var formatted = this
    if(this.contains("(playerName)"))
        formatted = formatted.replace("(playerName)", player.name)
    if(this.contains("(playerHealth)"))
        formatted = formatted.replace("(playerHealth)", "${player.health}")
    return formatted
}

fun String.formatMinimessage(): Component {
    return MiniMessage.get().parse(
        this
    )
}

// A modified handy method from spigot on stacking items in a list
fun List<ItemStack>.compressSimilarItems(): ArrayList<ItemStack> {

    val sorted = ArrayList<ItemStack>()
    for (item in this) {
        var putInPlace = false
        for (sitem in sorted) {
            if (item.isSimilar(sitem)) {
                if (item.amount + sitem.amount < sitem.maxStackSize) {
                    sitem.amount = sitem.amount + item.amount
                    putInPlace = true
                } else {
                    item.amount = item.amount - (sitem.maxStackSize - sitem.amount)
                    sitem.amount = sitem.maxStackSize
                }
                break
            }
        }
        if (!putInPlace) {
            sorted.add(item)
        }
    }
    return sorted
}
// Create player in database
fun Player.initInDialogue(){
    val playerUUID = plugin.playerSettings.getRowsWhere(
        "playerSettings",
        "UUID",
        "UUID = '${this.uniqueId.cleanUp()}'",
    )
    // Doesnt exist in db
    if(playerUUID.isEmpty()){
        plugin.playerSettings.insertIntoTable(
            "playerSettings",
            listOf("UUID", "INDIALOGUE"),
            Values(this.uniqueId.cleanUp(), false)
        )
    }
}

var Player.inDialogue: Boolean
    get() {
        return plugin.playerSettings.getRowsWhere(
            "playerSettings",
            "INDIALOGUE",
            "UUID = '${this.uniqueId.cleanUp()}'",
            1,
        )[0].values[0].toString().toBoolean()
    }
    set(value) {
        plugin.playerSettings.updateColumnsWhere(
            "playerSettings",
            listOf("INDIALOGUE"),
            "UUID = '${this.uniqueId.cleanUp()}'",
            Values(value),
        )
    }

fun JavaPlugin.getCustomConfig(configName: String): YamlConfiguration{
    val customConfigFile = File(this.dataFolder, configName)
    if (!customConfigFile.exists()) {
        customConfigFile.parentFile.mkdirs()
        saveResource(configName, false)
    }

    val customConfig = YamlConfiguration()
    try {
        customConfig.load(customConfigFile)
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InvalidConfigurationException) {
        e.printStackTrace()
    }
    return customConfig
}

