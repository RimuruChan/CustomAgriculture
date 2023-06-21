package tech.rimuruchan.customagriculture;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import tech.rimuruchan.customagriculture.V1_12_2.NewMain;
import tech.rimuruchan.customagriculture.V1_7_10.OldMain;

@SuppressWarnings("unused")
public final class CustomAgriculture extends JavaPlugin {

    IMain main;

    @Override
    public void onEnable() {
        // Plugin startup logic
        String version = getServer().getBukkitVersion().split("-")[0];
        switch (version) {
            case "1.12.2": {
                getLogger().info("Running on version 1.12.2");
                main = new NewMain(this);
                break;
            }
            case "1.7.10": {
                getLogger().info("Running on version 1.7.10");
                main = new OldMain(this);
                break;
            }
            default: {
                throw new RuntimeException("Not support version: " + version);
            }
        }
        main.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (main != null)
            main.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return main.onCommand(sender, command, label, args);
    }

}
