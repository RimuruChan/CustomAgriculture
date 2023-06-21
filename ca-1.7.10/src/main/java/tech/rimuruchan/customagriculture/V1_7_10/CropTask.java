package tech.rimuruchan.customagriculture.V1_7_10;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class CropTask extends BukkitRunnable {

    Block block;

    public CropTask(Block block) {
        this.block = block;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        BlockState bs = block.getState();
        switch (block.getType()) {
            case CROPS:
            case CARROT:
            case POTATO: {
                Crops crops = (Crops) bs.getData();
                switch (crops.getState()) {
                    case SEEDED:
                        crops.setState(CropState.GERMINATED);
                        break;
                    case GERMINATED:
                        crops.setState(CropState.VERY_SMALL);
                        break;
                    case VERY_SMALL:
                        crops.setState(CropState.SMALL);
                        break;
                    case SMALL:
                        crops.setState(CropState.MEDIUM);
                        break;
                    case MEDIUM:
                        crops.setState(CropState.TALL);
                        break;
                    case TALL:
                        crops.setState(CropState.VERY_TALL);
                        break;
                    case VERY_TALL:
                        crops.setState(CropState.RIPE);
                        cancel();
                        break;
                    default:
                        cancel();
                }
                bs.setData(crops);
                break;
            }
            case PUMPKIN_STEM:
            case MELON_STEM: {
                MaterialData md = bs.getData();
                if (md.getData() != 7)
                    md.setData((byte) (md.getData() + 1));
                else cancel();
                bs.setData(md);
                break;
            }
            case COCOA: {
                CocoaPlant cp = (CocoaPlant) bs.getData();
                switch (cp.getSize()) {
                    case SMALL:
                        cp.setSize(CocoaPlant.CocoaPlantSize.MEDIUM);
                        break;
                    case MEDIUM:
                        cp.setSize(CocoaPlant.CocoaPlantSize.LARGE);
                        cancel();
                        break;
                    default:
                        cancel();
                }
                bs.setData(cp);
                break;
            }
            case NETHER_WARTS: {
                NetherWarts nw = (NetherWarts) bs.getData();
                switch (nw.getState()) {
                    case SEEDED:
                        nw.setState(NetherWartsState.STAGE_ONE);
                        break;
                    case STAGE_ONE:
                        nw.setState(NetherWartsState.STAGE_TWO);
                        break;
                    case STAGE_TWO:
                        nw.setState(NetherWartsState.RIPE);
                        cancel();
                        break;
                    default:
                        cancel();
                }
                break;
            }
            default:
                cancel();
        }
        bs.update(true);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        for (Map.Entry<Location, CropTask> task : OldMain.crops) {
            if (task.getValue() == this) {
                OldMain.crops.remove(task);
                return;
            }
        }
    }
}
