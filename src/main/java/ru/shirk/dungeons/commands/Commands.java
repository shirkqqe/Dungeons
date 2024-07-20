package ru.shirk.dungeons.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.configs.Configuration;
import ru.shirk.dungeons.tools.ItemsHandler;

public class Commands implements CommandExecutor, TabCompleter {

    private final Configuration lang = Dungeons.getConfigurationManager().getConfig("lang.yml");

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender.hasPermission("dungeons.admin")) {
            if (args.length == 0) {
                sender.sendMessage(lang.c("feedback.help"));
            } else if (args[0].equalsIgnoreCase("give")) {
                if (sender instanceof Player player) {
                    if (args.length == 2) {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            if (ItemsHandler.giveItem(player, amount)) {
                                player.sendMessage(lang.c("feedback.give").replace("%amount%", String.valueOf(amount)));
                            } else {
                                player.sendMessage(lang.c("error.inventory_full"));
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(lang.c("amount_null"));
                        }
                    } else if (args.length == 3) {
                        try {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null && target.isOnline()) {
                                int amount = Integer.parseInt(args[2]);
                                if (ItemsHandler.giveItem(target, amount)) {
                                    player.sendMessage(lang.c("feedback.give_other").replace("%amount%"
                                            , String.valueOf(amount)).replace("%player%", args[1]));
                                } else {
                                    player.sendMessage(lang.c("error.inventory_full"));
                                }
                            } else {
                                player.sendMessage(lang.c("error.target_offline"));
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(lang.c("amount_null"));
                        }
                    }
                } else {
                    sender.sendMessage(lang.c("error.player_only"));
                }
            }
        }

        return true;
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender.hasPermission("dungeons.admin")) {
            if (args.length == 1) {
                return List.of("give", "reload");
            }

            if (args.length == 2) {
                List<String> complete = new ArrayList<>();
                for(Player player : Bukkit.getOnlinePlayers()) {
                    complete.add(player.getName());
                }
                complete.add("1");
                complete.add("64");

                return complete;
            }

            if (args.length == 3) {
                return List.of("1", "32", "64");
            }
        }

        return List.of();
    }
}
