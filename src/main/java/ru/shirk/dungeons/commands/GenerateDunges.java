package ru.shirk.dungeons.commands;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.configs.Configuration;
import ru.shirk.dungeons.tools.RegionManager;
import ru.shirk.dungeons.tools.SchematicUtils;
import ru.shirk.dungeons.tools.Utils;

public class GenerateDunges implements CommandExecutor {
    private final Configuration config = Dungeons.getConfigurationManager().getConfig("settings.yml");

    public GenerateDunges() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            long start = System.currentTimeMillis();

            try {
                for(int i = 1; i < 6; ++i) {
                    int index = i;
                    Utils.generateLocationAsync(this.config.ch("location.radius"), this.config.wd("location.world"), this.config.ml("location.blockList"), Biome.OCEAN, (location) -> {
                        if (location == null) {
                            sender.sendMessage("Failed to generate location for dungeon " + index);
                        } else {
                            UUID uuid = UUID.randomUUID();
                            RegionManager.setRegion(location, 20, uuid);
                            SchematicUtils.pasteSchematic(location, "second.schem");
                            Dungeons.getDungManager().addDung(uuid, location.clone().set(location.getX() + 5.0
                                    , location.getY(), location.getZ() - 1.0));
                            Bukkit.getScheduler().runTask(Dungeons.getInstance(), () -> {
                                Dungeons.getDungManager().addDung(uuid, location.clone().set(location.getX() - 10.0
                                        , location.getY() + 2.0, location.getZ() + 1.0));
                            });
                            Dungeons.getInstance().getSLF4JLogger().info("Dungeon {} index successfully registered!", index);
                        }
                    });
                }
                for(int i = 1; i < 6; ++i) {
                    int index = i;
                    Utils.generateLocationAsync(this.config.ch("location.radius"), this.config.wd("location.world"), this.config.ml("location.blockList"), Biome.DESERT, (location) -> {
                        if (location == null) {
                            sender.sendMessage("Failed to generate location for dungeon " + index);
                        } else {
                            UUID uuid = UUID.randomUUID();
                            RegionManager.setRegion(location, 20, uuid);
                            SchematicUtils.pasteSchematic(location, "first.schem");
                            Dungeons.getDungManager().addDung(uuid, location);
                            Dungeons.getInstance().getSLF4JLogger().info("Dungeon {} index successfully registered!", index);
                        }
                    });
                }

                Dungeons.getInstance().getSLF4JLogger().info("Все данжи успешно сгенерированы за {} мс.", System.currentTimeMillis() - start);
                Dungeons.getConfigurationManager().reloadConfigs();
            } catch (Exception e) {
                sender.sendMessage(e.getMessage());
            }
        }
        return true;
    }
}
