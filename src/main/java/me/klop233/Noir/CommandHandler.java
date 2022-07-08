package me.klop233.Noir;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.bot.MiraiGroup;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public class CommandHandler implements CommandExecutor {
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

        switch (args[0].toLowerCase()) {
            case "help":
                sender.sendMessage(new String[]{
                        ChatColor.LIGHT_PURPLE + "--------------[ Noir ]--------------",
                        "/noir help        显示本帮助信息",
                        "/noir reload      重载插件",
                        "/noir version     查看插件信息",
                        "/noir group       显示QQ命令帮助信息",
                        ChatColor.LIGHT_PURPLE + "-----------[ By " + ChatColor.AQUA + "Klop233 " + ChatColor.LIGHT_PURPLE+ "]-----------"
                });
                return true;

            case "reload":
                long t1 = System.currentTimeMillis();
                Noir.reloadNoir();
                Noir.readToData();
                long t2 = System.currentTimeMillis();
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir 配置文件重载成功 [" + (t2-t1) + "ms]");
                return true;

            case "version":
                sender.sendMessage(new String[]{
                        ChatColor.LIGHT_PURPLE + "--------[ 插件信息 ]----------",
                        ChatColor.LIGHT_PURPLE + "Noir -----> " + Noir.version,
                        ChatColor.GREEN + "MiraiMC --> " + Noir.miraiVersion,
                        "QQ Bot号: " + Data.botsID,
                        "QQ 群号: " + Data.groupsID
                });
                if (Noir.init) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + ChatColor.GREEN +"已就绪");
                } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + ChatColor.RED + "未准备好");
                }
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------[ By " + ChatColor.AQUA + "Klop233 " + ChatColor.LIGHT_PURPLE+ "]--------");
                return true;

            case "group":
                if (args.length == 1) {
                    sender.sendMessage(new String[]{
                            ChatColor.LIGHT_PURPLE + "----------------[ Noir ]----------------",
                            "/noir group add-admin [QQ号]    添加QQ管理员",
                            "/noir group rm-admin [QQ号]     移除QQ管理员",
                            "/noir group say [信息]          向QQ群发送信息",
                            ChatColor.LIGHT_PURPLE + "----------------------------------------"
                    });
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "add-admin":
                        if (args.length <= 2) {
                            sender.sendMessage(new String[]{
                                    ChatColor.RED + "缺少参数 -> QQ号",
                                    ChatColor.RED + "用法 -> /noir group add-admin [QQ号]"
                            });
                            return true;
                        }
                        try {
                            Long.valueOf(args[2]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "参数错误 -> QQ号  应为Long类型");
                            return true;
                        }
                        Data.adminsID.add(Long.valueOf(args[2]));
                        Noir.config.set("qq.admin", Data.adminsID.toArray());
                        Noir.reloadNoir();
                        sender.sendMessage(ChatColor.GREEN + args[2] + "添加成功");
                        return true;

                    case "rm-admin":
                        if (args.length <= 2) {
                            sender.sendMessage(new String[]{
                                    ChatColor.RED + " 缺少参数 -> QQ号",
                                    ChatColor.RED + " 用法 -> /noir group rm-admin [QQ号]"
                            });
                            return true;
                        }

                        try {
                            Long.valueOf(args[2]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "参数错误 -> QQ号  应为Long类型");
                            return true;
                        }
                        if (!Data.adminsID.contains(Long.valueOf(args[2]))) {
                            sender.sendMessage(ChatColor.RED + "此QQ号非管理员");
                            return true;
                        }
                        Data.adminsID.remove(Long.valueOf(args[2]));
                        Noir.config.set("qq.admin", Data.adminsID.toArray());
                        Noir.reloadNoir();
                        sender.sendMessage(ChatColor.GREEN +args[2] + " 移除成功");
                        return true;

                    case "say":
                        if (args.length <= 2) {
                            sender.sendMessage(new String[]{
                                    ChatColor.RED +"缺少参数 -> 信息",
                                    ChatColor.RED + "用法 -> /noir group say [信息]"
                            });
                            return true;
                        }
                        String msg = "";
                        for (int i=2;i<args.length;i++) {
                            msg += args[i];
                            msg += " ";
                        }
                        msg = msg.substring(0,msg.length()-1);
                        // 异步发信息
                        String finalMsg = msg;
                        Bukkit.getScheduler().runTaskAsynchronously(Noir.INSTANCE, new Runnable() {
                            @Override
                            public void run() {
                                MiraiBot.getBot(Data.botsID).getGroup(Data.groupsID).sendMessage(finalMsg);
                            }
                        });
                        return true;
                    default:
                        sender.sendMessage("未知子命令, 使用 /noir group help 来获取帮助");
                        return true;
                }

            default:
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Noir " + Noir.version + ChatColor.GREEN + " 正在这个服务器上运行, 使用 /noir help 来获取帮助");
                return true;
        }
    }

    public void sendMsgAsync(String msg) {

    }
}
