package me.klop233.Noir.Events;


import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.klop233.Noir.Commander;
import me.klop233.Noir.Data;
import me.klop233.Noir.Noir;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class GroupMessage implements Listener {
    @EventHandler
    public void list(MiraiGroupMessageEvent e) {
        if (!(Data.botsID == e.getBotID()) ||
                !e.getMessage().replace(" ", "").startsWith(Data.list) ||
                !Noir.init) {
            return;
        }


        StringBuilder listMessage = new StringBuilder();
        listMessage.append("在线: ").append(Noir.INSTANCE.getServer().getOnlinePlayers().size()).append("| 玩家列表: ");

        if (Noir.INSTANCE.getServer().getOnlinePlayers().size() == 0) {
            listMessage.append("无");
        } else {
            for (Player s : Noir.INSTANCE.getServer().getOnlinePlayers()) {
                listMessage.append(s.getDisplayName()).append(",");
            }
            // 把最后一个逗号删掉
            listMessage.deleteCharAt(listMessage.length() - 1);
        }

        e.sendMessage(listMessage.toString());
    }

    @EventHandler
    public void bind(MiraiGroupMessageEvent e) {
        if (!(Data.botsID == e.getBotID()) ||
                !e.getMessage().replace(" ", "").startsWith(Data.bind) ||
                !Noir.init) {
            return;
        }

        String name = e.getMessage().replace(" ", "").substring(Data.bind.length());
        long bindQQID = e.getSenderID();
        if ("".equals(name)) {
            e.sendMessage("用法：#绑定 [ID]");
        } else if (name.length() > 25) {
            e.sendMessage("你的ID长度超过限制！[25]");
        } else {
            if (MiraiMC.getBinding(name) != 0L) {
                e.getGroup().sendMessage("绑定失败，你要绑定的ID已经绑定了一个QQ: " + Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(MiraiMC.getBinding(bindQQID)))).getDisplayName() + "，请更换你的游戏ID");
            } else {
                if (!MiraiMC.getBinding(bindQQID).equals("")) {
                    e.getGroup().sendMessage("已替换绑定[" + name + "]！请勿频繁替换绑定！");
                } else {
                    e.getGroup().sendMessage("绑定成功[" + name + "]，白名单已自动添加！");
                }

                MiraiMC.addBinding(Bukkit.getOfflinePlayer(name).getUniqueId().toString(), bindQQID);

                Commander send = new Commander();

                try {
                    for (String cmd : Noir.config.getStringList("qq.commands-after-bind")) {
                        Bukkit.getScheduler().callSyncMethod(Noir.INSTANCE, () -> Bukkit.dispatchCommand(send, cmd.replace("[player-name]", name))).get();
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    Noir.info("Noir 执行命令时出错, 但是这一错误一般无需处理");
                    e.sendMessage("执行命令出错");
                }

//                if (send.message.size() == 0) {
//                    e.sendMessage("已执行, 命令无返回");
//                    return ;
//                }
//
//                StringBuilder reply = new StringBuilder();
//                for (String s : send.message) {
//                    reply.append(s).append("\n");
//                }
//
//                e.sendMessage(reply.toString());
            }
        }
    }

    @EventHandler
    public void execute(MiraiGroupMessageEvent e) {
        if (!(Data.botsID == e.getBotID()) ||
                !e.getMessage().replace(" ", "").startsWith(Data.execute) ||
                !Noir.init) {
            return;
        }
        if (!Data.adminsID.contains(e.getSenderID())) {
            e.sendMessage("你没有权限");
            return ;
        }

        Commander send = new Commander();

        try {
            Bukkit.getScheduler().callSyncMethod(Noir.INSTANCE, () -> Bukkit.dispatchCommand(send, e.getMessage().substring(Data.execute.length()))).get();
        } catch (ExecutionException | InterruptedException var6) {
            Noir.info("Noir 执行命令时出错, 但是这一错误一般无需处理");
            e.sendMessage("执行命令出错");
            return ;
        }

        if (send.message.size() == 0) {
            e.getGroup().sendMessage("已执行，命令无返回");
        } else {
            StringBuilder groupReply = new StringBuilder();

            for (String s : send.message) {
                groupReply.append(s).append("\n");
            }

            groupReply.delete(groupReply.length() - 1, groupReply.length());
            e.getGroup().sendMessage(ChatColor.stripColor(groupReply.toString()));
        }
    }
}
