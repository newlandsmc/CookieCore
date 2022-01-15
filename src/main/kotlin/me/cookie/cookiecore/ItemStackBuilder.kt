package me.cookie.cookiecore

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackBuilder(private val type: Material) {
    private var itemName: Component? = null
    private var itemLore: List<Component>? = null

    fun withName(name: Component): ItemStackBuilder {
        this.itemName = name
        return this
    }

    fun withLore(vararg lore: Component): ItemStackBuilder {
        this.itemLore = lore.toList()
        return this
    }

    fun build(): ItemStack {
        val itemStack = ItemStack(type)
        val itemMeta = itemStack.itemMeta

        if(itemName == null){
            itemMeta.displayName(Component.text(type.name))
        }else{
            itemMeta.displayName(itemName)
        }

        if(itemLore != null){
            itemMeta.lore(itemLore)
        }

        itemStack.itemMeta = itemMeta

        return itemStack
    }
}