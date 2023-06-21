package tech.rimuruchan.customagriculture.V1_12_2;

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
        if ((name = NewMain.getSeedName(item)) != null) {
            Material type = NewMain.getRandomMaterial(name);
            block.setType(type);
            if (type != Material.AIR)
                NewMain.createTask(block);
        } else if (NewMain.isPreventedType(block))
            event.setCancelled(true);
        else if (NewMain.isControlledType(block)) {
            NewMain.createTask(block);
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
            CropTask task = NewMain.findTaskByLocation(block.getRelative(BlockFace.UP).getLocation());
            if (task != null) {
                task.cancel();
            }
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        if (NewMain.isControlledType(event.getBlock()))
            event.setCancelled(true);
    }

    public void cancelCrop(Block block) {
        if (NewMain.isControlledType(block)) {
            CropTask task = NewMain.findTaskByLocation(block.getLocation());
            if (task != null) {
                task.cancel();
            }
        }
    }

}
