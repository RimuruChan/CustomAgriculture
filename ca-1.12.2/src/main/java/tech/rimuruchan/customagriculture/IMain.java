package tech.rimuruchan.customagriculture;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface IMain {

    void onEnable();

    void onDisable();

    boolean onCommand(CommandSender sender, Command command, String label, String[] args);



}
