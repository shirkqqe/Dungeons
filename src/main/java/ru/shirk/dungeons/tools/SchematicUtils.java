package ru.shirk.dungeons.tools;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import ru.shirk.dungeons.Dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SchematicUtils {
    public static void pasteSchematic(Location location, String name) {
        try {
            File file = new File(Dungeons.getInstance().getDataFolder() + "/schematics/" + name);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();
            World adaptedWorld = BukkitAdapter.adapt(location.getWorld());
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
            Operation operation = (new ClipboardHolder(clipboard)).createPaste(editSession).to(BlockVector3
                    .at(location.getX(), location.getY(), location.getZ())).ignoreAirBlocks(false).build();

            try {
                Operations.complete(operation);
                editSession.flushSession();
            } catch (WorldEditException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
