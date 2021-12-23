package me.cookie.cookiecore

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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

fun String.formatMinimessage(): Component {
    return MiniMessage.get().parse(
        this
    )
}

// A modified handy method from spigot on stacking items in a list
fun List<ItemStack>.compressSimilarItems(): List<ItemStack> {
    val itemsCopy: MutableList<ItemStack> = this.toMutableList()
    itemsCopy.removeIf{ obj: ItemStack? -> obj == null}
    for (i in itemsCopy.indices) {
        for (j in i + 1 until itemsCopy.size) {
            if (itemsCopy[i].isSimilar(itemsCopy[j]) && itemsCopy[i].amount + itemsCopy[j]
                    .amount <= itemsCopy[j].maxStackSize
            ) {
                itemsCopy[i].amount = itemsCopy[i].amount + itemsCopy[j].amount
                itemsCopy[j] = ItemStack(Material.AIR) // Set to air as no nulls allowed
            }
        }
    }
    itemsCopy.removeIf{ obj: ItemStack -> obj.type == Material.AIR} // Clear all the airs
    return itemsCopy
}