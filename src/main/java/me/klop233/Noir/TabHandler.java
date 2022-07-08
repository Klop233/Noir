package me.klop233.Noir;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission("noir.access")) {
            return Arrays.asList("help", "reload", "version");
        } else {
            return Collections.singletonList("你没有权限使用 Noir");
        }
    }
}
