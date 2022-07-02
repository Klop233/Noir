package me.klop233.Noir.Events;


import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.klop233.Noir.Data;
import me.klop233.Noir.Noir;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class GroupMessage implements Listener {
    @EventHandler
    public void list(MiraiGroupMessageEvent e) {
        if (!(Data.botsID == e.getBotID()) ||
            !e.getMessage().replace(" ","").startsWith(Data.list) ||
            !Noir.init) {
            return ;
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

    }
}
