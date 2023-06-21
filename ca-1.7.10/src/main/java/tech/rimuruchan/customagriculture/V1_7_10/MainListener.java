package tech.rimuruchan.customagriculture.V1_7_10;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

public class MainListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        String name;
        if ((name = OldMain.getSeedName(item)) != null) {
            Material type = OldMain.getRandomMaterial(name);
            block.setType(type);
            if (type != Material.AIR)
                OldMain.createTask(block);
        } else if (OldMain.isPreventedType(block))
            event.setCancelled(true);
        else if (OldMain.isControlledType(block)) {
            OldMain.createTask(block);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        cancelCrop(block);
    }

    @EventHandler
    public void onChange(BlockFromToEvent event) {
        Block block = event.getToBlock();
        cancelCrop(block);
    }

    @EventHandler
    public void onEntityChange(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SOIL) {
            CropTask task = OldMain.findTaskByLocation(block.getRelative(BlockFace.UP).getLocation());
            if (task != null) {
                task.cancel();
            }
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        if (OldMain.isControlledType(event.getBlock()))
            event.setCancelled(true);
    }

    public void cancelCrop(Block block) {
        if (OldMain.isControlledType(block)) {
            CropTask task = OldMain.findTaskByLocation(block.getLocation());
            if (task != null) {
                task.cancel();
            }
        }
    }

}
