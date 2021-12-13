package me.cookie.semicore

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player
import java.util.*

// Component to plain string
fun Component.toPlainString(): String = PlainTextComponentSerializer.plainText().serialize(this)

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