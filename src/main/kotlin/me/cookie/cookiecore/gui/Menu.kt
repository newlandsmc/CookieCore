package me.cookie.cookiecore.gui

import me.cookie.cookiecore.PlayerMenuUtility
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder


abstract class Menu(
    protected var playerMenuUtility: PlayerMenuUtility
) : InventoryHolder {
    protected var cInventory: Inventory? = null
    abstract val menuName: Component?

    abstract val slots: Int

    abstract fun handleClick(e: InventoryClickEvent)

    abstract fun handleClose(e: InventoryCloseEvent)

    abstract fun setMenuItems()

    fun open() {
        cInventory = Bukkit.createInventory(this, slots, menuName!!)
        setMenuItems()
        playerMenuUtility.player.openInventory(cInventory!!)
    }

    override fun getInventory(): Inventory {
        return cInventory!!
    }
}