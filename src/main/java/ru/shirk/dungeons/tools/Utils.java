package ru.shirk.dungeons.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.loot.Item;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private static final int MAX_ATTEMPTS = 100;
    private static final Set<String> checkedLocations = new HashSet<>();

    public static void playParticle(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.TOTEM, location.add(0.0, 1.0, 0.0), 30, 0.2, 0.2, 0.2, 0.1);
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 15, 0.2, 0.2, 0.2, 0.05);
        }

    }

    public static void generateLoot(Inventory inventory) {
        inventory.clear();
        List<Item> loot = Dungeons.getConfigurationManager().getConfig("loot.yml").il("loot");
        for(Item stack : loot) {
            int rand = (int)(Math.random() * (double)inventory.getSize() - 1.0);
            int chance = (int)(Math.random() * 100.0);
            if (inventory.getItem(rand) == null && stack != null && chance <= stack.getChance()) {
                inventory.setItem(rand, stack.getItemStack());
            }
        }
    }

    public static void generateLocationAsync(final int radius, final World world, final List<Material> blockList, final Biome biome, final LocationCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = generateLocation(radius, world, blockList, biome);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        callback.onLocationGenerated(location);
                    }
                }.runTask(Dungeons.getInstance());
            }
        }.runTaskAsynchronously(Dungeons.getInstance());
    }

    private static Location generateLocation(final int radius, final World world, final List<Material> blockList, final Biome biome) {
        Location loc = null;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            int x = ThreadLocalRandom.current().nextInt(-radius, radius);
            int z = ThreadLocalRandom.current().nextInt(-radius, radius);
            String coordKey = x + "," + z;

            if (checkedLocations.contains(coordKey)) {
                continue;
            }

            checkedLocations.add(coordKey);

            if (world.getBiome(x, z) != biome) {
                attempts++;
                continue;
            }

            final int y = world.getHighestBlockYAt(x, z) + 1;
            final Location location = new Location(world, x, y, z);

            if (blockList.contains(location.getBlock().getRelative(0, -1, 0).getType())) {
                attempts++;
            } else {
                Dungeons.getInstance().getSLF4JLogger().info("Location successfully generated in {} attempts", attempts + 1);
                loc = location;
                break;
            }
        }

        if (loc == null) {
            Dungeons.getInstance().getSLF4JLogger().warn("Failed to generate location within {} attempts", MAX_ATTEMPTS);
        }

        return loc;
    }

    public interface LocationCallback {
        void onLocationGenerated(Location location);
    }
}
