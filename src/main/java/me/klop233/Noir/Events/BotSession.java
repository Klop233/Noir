package me.klop233.Noir.Events;

import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOfflineEvent;
import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOnlineEvent;
import me.klop233.Noir.Data;
import me.klop233.Noir.Noir;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BotSession implements Listener {
    @EventHandler
    public void onBotLogin(MiraiBotOnlineEvent e) {
        if (!(e.getBotID() == Data.botsID)) {
            return ;
        }
        Noir.info("Noir 检测到QQ登录, Noir已就绪");
        Noir.init = true;
    }

    @EventHandler
    public void onBotLogout(MiraiBotOfflineEvent e) {
        if (!(e.getBotID() == Data.botsID)) {
            return ;
        }
        Noir.info("Noir 检测到QQ登出, Noir将禁用");
        Noir.init = false;
    }
}
