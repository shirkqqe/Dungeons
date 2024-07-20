package ru.shirk.dungeons.tools;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.configs.Configuration;

public class RegionManager {

    private static final Configuration settings = Dungeons.getConfigurationManager().getConfig("settings.yml");
    private static final HashMap<StateFlag, StateFlag.State> flags = new HashMap<>();

    public static ProtectedCuboidRegion getCuboidRegion(Location location, int radius, UUID uuid) {
        Location point1 = new Location(location.getWorld(), location.getX() + (double)radius, location.getY()
                + (double)radius, location.getZ() + (double)radius);
        Location point2 = new Location(location.getWorld(), location.getX() - (double)radius, location.getY()
                - (double)radius, location.getZ() - (double)radius);
        return new ProtectedCuboidRegion("dungeon-" + uuid.toString(), BlockVector3.at(point1.getX(), point1.getY()
                , point1.getZ()), BlockVector3.at(point2.getX(), point2.getY(), point2.getZ()));
    }

    public static void setRegion(Location location, int radius, UUID uuid) {
        loadFlags();
        ProtectedCuboidRegion rg = getCuboidRegion(location, radius, uuid);
        World world = location.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldguard.protection.managers.RegionManager regions = container.get(BukkitAdapter.adapt(world));

        assert regions != null;

        try {
            for(Map.Entry<StateFlag, StateFlag.State> entry : flags.entrySet()) {
                rg.setFlag((Flag)entry.getKey(), entry.getValue());
            }
        } catch (Exception var9) {
            throw new IllegalStateException("region set error;");
        }

        regions.addRegion(rg);
    }

    public static void removeRegion(Location location, UUID uuid) {
        World world = location.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldguard.protection.managers.RegionManager regions = container.get(BukkitAdapter.adapt(world));
        if (regions.hasRegion("dungeon-" + uuid.toString())) {
            regions.removeRegion("dungeon-" + uuid.toString(), RemovalStrategy.REMOVE_CHILDREN);
        }
    }

    public static void loadFlags() {
        List<String> list = settings.cl("region.allow-flags");
        List<String> list2 = settings.cl("region.deny-flags");
        for(String flag : list) {
            if (!addFlag(flag, true)) {
                Dungeons.getInstance().getLogger().warning("Flag " + flag + " not loaded in");
            }
        }
        for(String flag : list2) {
            if (!addFlag(flag, false)) {
                Dungeons.getInstance().getLogger().warning("Flag " + flag + " not loaded in");
            }
        }
    }

    private static boolean addFlag(String flagname, boolean state) {
        Flag<?> flag = WorldGuard.getInstance().getFlagRegistry().get(flagname);
        if (flag == null) {
            return false;
        } else {
            StateFlag stateFlag = (StateFlag)flag;
            StateFlag.State saved_state = state ? State.ALLOW : State.DENY;
            flags.put(stateFlag, saved_state);
            return true;
        }
    }
}
