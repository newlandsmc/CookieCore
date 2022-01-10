package me.cookie.cookiecore

import org.bukkit.plugin.java.JavaPlugin


private val plugin = JavaPlugin.getPlugin(CookieCore::class.java)
val INVALID_USAGE = plugin.config.getString("invalid-usage")!!.formatMinimessage()
val NO_PERMISSION = plugin.config.getString("no-permission")!!.formatMinimessage()