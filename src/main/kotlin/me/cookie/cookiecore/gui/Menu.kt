package me.cookie.cookiecore.gui

import me.cookie.cookiecore.PlayerMenuUtility
import me.cookie.cookiecore.gui.SlotsType.DROPPER_9
import me.cookie.cookiecore.gui.SlotsType.HOPPER_5
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class Menu(
    protected var playerMenuUtility: PlayerMenuUtility
) : InventoryHolder {
    protected var cInventory: Inventory? = null
    abstract val menuName: Component?

    abstract val slots: SlotsType

    abstract fun handleClick(e: InventoryClickEvent)

    abstract fun handleClose(e: InventoryCloseEvent)

    abstract fun setMenuItems()

    fun open() {
        cInventory = if(slots == HOPPER_5 || slots == DROPPER_9){
            if(slots == HOPPER_5)
                Bukkit.createInventory(this, InventoryType.HOPPER, menuName!!)
            else
                Bukkit.createInventory(this, InventoryType.DROPPER, menuName!!)
        }else{
            val slotNum: Int = slots.size
            Bukkit.createInventory(this, slotNum, menuName!!)
        }

        setMenuItems()
        playerMenuUtility.player.openInventory(cInventory!!)
    }

    override fun getInventory(): Inventory {
        return cInventory!!
    }
}