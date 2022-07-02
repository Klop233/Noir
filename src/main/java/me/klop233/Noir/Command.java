package me.klop233.Noir;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!sender.hasPermission("noir.access")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用这个命令");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + Noir.version + ChatColor.GREEN + " 正在这个服务器上运行, 使用 /noir help 来获取帮助");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "-------------[ Noir ]-------------");
            sender.sendMessage("/noir help        显示本帮助信息");
            sender.sendMessage("/noir reload      重载插件");
            sender.sendMessage("/noir version     查看插件信息");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "----------[ By " + ChatColor.AQUA + "Klop233 " + ChatColor.LIGHT_PURPLE+ "]----------");
        } else if (args[0].equalsIgnoreCase("reload")) {
            long t1 = System.currentTimeMillis();
            Noir.reloadNoir();
            long t2 = System.currentTimeMillis();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir 配置文件重载成功 [" + (t2-t1) + "ms]");


        } else if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------[ 插件信息 ]----------");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir -> " + Noir.version);
            sender.sendMessage(ChatColor.GREEN + "MiraiMC -> " + Noir.miraiVersion);
            sender.sendMessage("QQ Bot号: " + Data.botsID);
            sender.sendMessage("QQ 群号: " + Data.groupsID);
            if (Noir.init) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + ChatColor.GREEN +"已就绪");
            } else {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + ChatColor.RED + "未准备好");
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------[ By " + ChatColor.AQUA + "Klop233 " + ChatColor.LIGHT_PURPLE+ "]--------");
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + Noir.version + ChatColor.GREEN + " 正在这个服务器上运行, 使用 /noir help 来获取帮助");
        }

        return true;
    }
}
