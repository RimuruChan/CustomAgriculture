package tech.rimuruchan.customagriculture.V1_12_2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import tech.rimuruchan.customagriculture.IMain;

import java.util.*;
import java.util.stream.Collectors;

public class NewMain implements IMain {

    public static JavaPlugin plugin;
    public static Map<String, ItemStack> seeds = new HashMap<>();
    public static List<Map.Entry<Location, CropTask>> crops = new ArrayList<>();

    public NewMain(JavaPlugin plugin) {
        NewMain.plugin = plugin;
    }

    public static void createTask(Block block) {
        Material type = block.getType();
        int time = NewMain.getTime(type);
        CropTask task = new CropTask(block);
        task.runTaskTimer(NewMain.plugin, time * 20L, time * 20L);
        NewMain.crops.add(new AbstractMap.SimpleEntry<>(block.getLocation(), task));
    }

    public static int getTime(Material material) {
        switch (material) {
            case CROPS:
                return plugin.getConfig().getInt("ca.time.crops");
            case CARROT:
                return plugin.getConfig().getInt("ca.time.carrot");
            case POTATO:
                return plugin.getConfig().getInt("ca.time.potato");
            case BEETROOT_BLOCK:
                return plugin.getConfig().getInt("ca.time.beetroot");
            case PUMPKIN_STEM:
                return plugin.getConfig().getInt("ca.time.pumpkin");
            case MELON_STEM:
                return plugin.getConfig().getInt("ca.time.melon");
            case COCOA:
                return plugin.getConfig().getInt("ca.time.cocoa");
            case NETHER_WARTS:
                return plugin.getConfig().getInt("ca.time.warts");
        }
        return -1;
    }

    public static boolean isPreventedType(Block block) {
        Material type = block.getType();
        return type == Material.CROPS ||
                type == Material.CARROT ||
                type == Material.POTATO ||
                type == Material.BEETROOT_BLOCK ||
                type == Material.MELON_STEM ||
                type == Material.PUMPKIN_STEM;
    }

    public static boolean isControlledType(Block block) {
        Material type = block.getType();
        return type == Material.CROPS ||
                type == Material.CARROT ||
                type == Material.POTATO ||
                type == Material.BEETROOT_BLOCK ||
                type == Material.MELON_STEM ||
                type == Material.PUMPKIN_STEM ||
                type == Material.NETHER_WARTS ||
                type == Material.COCOA;
    }

    public static String getSeedName(ItemStack seed) {
        for (Map.Entry<String, ItemStack> is : seeds.entrySet()) {
            if (seed.getItemMeta().equals(is.getValue().getItemMeta())) {
                return is.getKey();
            }
        }
        return null;
    }

    public static Material getRandomMaterial(String seed) {
        int crops = plugin.getConfig().getInt("ca.seed." + seed + ".chance.crops", 0);
        int carrot = plugin.getConfig().getInt("ca.seed." + seed + ".chance.carrot", 0);
        int potato = plugin.getConfig().getInt("ca.seed." + seed + ".chance.potato", 0);
        int beetroot = plugin.getConfig().getInt("ca.seed." + seed + ".chance.beetroot", 0);
        int pumpkin = plugin.getConfig().getInt("ca.seed." + seed + ".chance.pumpkin", 0);
        int melon = plugin.getConfig().getInt("ca.seed." + seed + ".chance.melon", 0);
        int bound = crops + carrot + potato + beetroot + pumpkin + melon;
        if (bound == 0)
            return Material.AIR;
        int random = new Random().nextInt(bound);
        int last = 0;
        if (random < (last += crops))
            return Material.CROPS;
        if (random < (last += carrot) && carrot != 0)
            return Material.CARROT;
        if (random < (last += potato) && potato != 0)
            return Material.POTATO;
        if (random < (last += beetroot) && beetroot != 0)
            return Material.BEETROOT_BLOCK;
        if (random < last + pumpkin && pumpkin != 0)
            return Material.PUMPKIN_STEM;
        else
            return Material.MELON_STEM;
    }

    public static CropTask findTaskByLocation(Location location) {
        for (Map.Entry<Location, CropTask> task : crops) {
            if (location.getBlockX() == task.getKey().getBlockX() &&
                    location.getBlockY() == task.getKey().getBlockY() &&
                    location.getBlockZ() == task.getKey().getBlockZ()) {
                return task.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {
        plugin.saveDefaultConfig();

        Set<String> keys = plugin.getConfig().getConfigurationSection("ca.seed").getKeys(false);
        keys.forEach(key -> {
            ItemStack is = new ItemStack(Material.SEEDS);
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ca.seed." + key + ".name")));
            meta.setLore(plugin.getConfig().getStringList("ca.seed." + key + ".lore").stream().map((s -> ChatColor.translateAlternateColorCodes('&', s))).collect(Collectors.toList()));
            is.setItemMeta(meta);
            seeds.put(key, is);
        });

        Bukkit.getPluginManager().registerEvents(new MainListener(), plugin);

        List<Location> locations = (List<Location>) plugin.getConfig().get("ca.data");
        if (locations == null)
            return;
        for (Location loc : locations) {
            Block block = loc.getBlock();
            if (isControlledType(block)) {
                createTask(block);
            }
        }
    }

    @Override
    public void onDisable() {
        List<Location> locations = new ArrayList<>();

        for (Map.Entry<Location, CropTask> task : crops) {
            locations.add(task.getKey());
        }

        plugin.reloadConfig();
        plugin.getConfig().set("ca.data", locations);
        plugin.saveConfig();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player
                && args.length == 1) {
            ItemStack is = seeds.get(args[0]);
            if (is == null)
                return false;
            ((Player) sender).getInventory().addItem(is);
            return true;
        } else
            return false;
    }
}
