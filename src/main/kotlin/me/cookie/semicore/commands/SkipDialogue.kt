package me.cookie.semicore.commands

import me.cookie.semicore.SemiCore
import me.cookie.semicore.message.dialogue.Dialogue
import me.cookie.semicore.message.messagequeueing.dialogueQueue
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SkipDialogue: CommandExecutor {
    private val plugin = JavaPlugin.getPlugin(SemiCore::class.java)
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){
            sender.sendMessage("Only players are allowed to execute this command")
        }
        val player = sender as Player
        if(!player.hasPermission("semicore.dialogue.skip")){
            player.sendMessage(
                MiniMessage.get().parse(
                    plugin.config.getString("no-permission")!!
                )
            )
            return true
        }

        val garbage = mutableListOf<Dialogue>()

        dialogueQueue.forEach dialogueLoop@ { dialogue ->
            if(dialogue.playerToSend!!.uniqueId == sender.uniqueId){
                garbage.add(dialogue)
                return@dialogueLoop
            }
            dialogue.playersToSend.forEach { loopPlayer ->
                if(loopPlayer.uniqueId == player.uniqueId){
                    garbage.add(dialogue)
                    return@dialogueLoop
                }
            }
        }

        dialogueQueue.removeAll(garbage)

        return true
    }
}