package me.nivyox.duels.commands;

import me.nivyox.duels.game.Game;
import me.nivyox.duels.game.GameManager;
import me.nivyox.duels.utils.ChatMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niek on 5-2-2017.
 */
public class CommandSpectate implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!GameManager.isIngame(player)) {
                if (args.length == 0) {
                    sender.sendMessage(ChatMessages.spectate_usage);
                } else {
                    if (Bukkit.getPlayer(args[0]).isOnline()) {
                        Player gamePlayer = Bukkit.getPlayer(args[0]);
                        if (GameManager.isIngame(gamePlayer)) {
                            Game game = GameManager.getGame(gamePlayer);
                            game.addSpectator(player, Bukkit.getPlayer(args[0]));
                        } else {
                            sender.sendMessage(ChatMessages.player_is_not_ingame);
                        }
                    } else {
                        sender.sendMessage(ChatMessages.player_is_not_online);
                    }
                }
            } else {
                sender.sendMessage(ChatMessages.you_are_in_game);
            }
        } else {
            sender.sendMessage(ChatMessages.no_player);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> returning = new ArrayList<>();
        if (sender instanceof Player) {
            returning.clear();
            if (args.length == 1) {
                ArrayList<Player> players = GameManager.getIngamePlayers();
                for (Player player : players) {
                    returning.add(player.getName());
                }
                returning.remove(sender.getName());
                if (returning.isEmpty()) {
                    sender.sendMessage(ChatMessages.no_one_to_spectate);
                }
            }
        }
        return returning;
    }
}
