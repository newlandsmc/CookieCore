package me.cookie.cookiecore

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemStackBuilder(private val type: Material) {
    private var itemStack = ItemStack(type)
    private var itemName = itemStack.displayName()
    private var itemMeta = itemStack.itemMeta
    private var itemLore = listOf<Component>()
    private var itemFlags: Array<out ItemFlag>? = null

    fun withName(name: Component): ItemStackBuilder {
        itemName = name
        return this
    }

    fun withLore(vararg lore: Component): ItemStackBuilder {
        itemLore = lore.toList()
        return this
    }

    fun withFlags(vararg flags: ItemFlag): ItemStackBuilder {
        itemFlags = flags
        return this
    }

    fun build(): ItemStack {
        itemMeta.displayName(itemName)
        itemMeta.lore(itemLore)
        itemFlags?.let { itemMeta.addItemFlags(*it) }

        itemStack.itemMeta = itemMeta

        return itemStack
    }
}