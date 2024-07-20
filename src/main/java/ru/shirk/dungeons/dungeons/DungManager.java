package ru.shirk.dungeons.dungeons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import ru.shirk.dungeons.Dungeons;

public class DungManager {
    private final Map<UUID, Dung> dungeons = new HashMap<>();

    public DungManager() {
    }

    public void loadDungeons() {
        int index = 0;
        long startTime = System.currentTimeMillis();

        for (Dung dung : Dungeons.getConfigurationManager().getConfig("dungeons.yml").dl("dungeons")) {
            dungeons.putIfAbsent(UUID.randomUUID(), dung);
            index++;
        }

        Dungeons.getInstance().getSLF4JLogger().info("{} Dungeons successfully registered in {} ms!", index, System.currentTimeMillis() - startTime);
    }

    public void addDung(UUID uuid, Location location) {
        List<Dung> dungs = Dungeons.getConfigurationManager().getConfig("dungeons.yml").dl("dungeons");
        Dung dung = new Dung(location.getX(), location.getY(), location.getZ(), location.getWorld().getName());
        dungs.add(dung);
        Dungeons.getConfigurationManager().getConfig("dungeons.yml").save();
        Dungeons.getConfigurationManager().getConfig("dungeons.yml").reload();
        dungeons.putIfAbsent(uuid, dung);
    }

    public void removeDung(Location location) {
        if (this.isDungeon(location)) {
            Iterator<Map.Entry<UUID, Dung>> iterator = this.dungeons.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Dung> entry = iterator.next();
                final Dung dung = entry.getValue();
                if (dung.getX() == location.getX() && dung.getY() == location.getY() &&
                        dung.getZ() == location.getZ() && dung.getWorld().equalsIgnoreCase(location.getWorld().getName())) {
                    iterator.remove();
                }
            }

            Iterator<Dung> dungIterator = Dungeons.getConfigurationManager().getConfig("dungeons.yml").dl("dungeons").iterator();
            while (dungIterator.hasNext()) {
                final Dung dung = dungIterator.next();
                if (dung.getX() == location.getX() && dung.getY() == location.getY() && dung.getZ() == location.getZ() && dung.getWorld().equalsIgnoreCase(location.getWorld().getName())) {
                    Dungeons.getConfigurationManager().getConfig("dungeons.yml").dl("dungeons").remove(dung);
                    Dungeons.getConfigurationManager().getConfig("dungeons.yml").save();
                    Dungeons.getConfigurationManager().getConfig("dungeons.yml").reload();
                    break;
                }
            }
        }

    }

    public void clear() {
        dungeons.clear();
    }

    public boolean isDungeon(Location location) {
        for(Map.Entry<UUID, Dung> entry : dungeons.entrySet()) {
            final Dung dung = entry.getValue();

            if(dung.getX() == location.getX() && dung.getY() == location.getY() && dung.getZ() == location.getZ()
                    && dung.getWorld().equalsIgnoreCase(location.getWorld().getName())) {
                return true;
            }
        }
        return false;
    }
}
