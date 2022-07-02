package me.klop233.Noir;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.klop233.Noir.Events.BotSession;
import me.klop233.Noir.Events.GroupMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;


public final class Noir extends JavaPlugin {
    public static Noir INSTANCE;
    public static Configuration config;
    public static boolean init = false;
    public static String version;
    public static String miraiVersion;

    @Override
    public void onEnable() {
        long startLoadingTime = System.currentTimeMillis();
        long loadTime;

        INSTANCE = this;
        version = getDescription().getVersion();
        miraiVersion = Bukkit.getPluginManager().getPlugin("MiraiMC").getDescription().getVersion();
        info("-----------------------------------");
        info("Noir " + version + " 正在加载");

        loadTime = System.currentTimeMillis(); // 打时间戳
        // 读配置文件
        saveDefaultConfig();
        config = getConfig();
        readToData();
        info("读取配置文件成功 [" + (System.currentTimeMillis() - loadTime) + "ms]");

        loadTime = System.currentTimeMillis();
        Bukkit.getPluginManager().registerEvents(new GroupMessage(), this);
        Bukkit.getPluginManager().registerEvents(new BotSession(), this);
        info("事件监听器注册成功 [" + (System.currentTimeMillis() - loadTime) + "ms]");

        loadTime = System.currentTimeMillis();
        Bukkit.getPluginCommand("noir").setExecutor(new Command());
        info("命令执行器设置成功 [" + (System.currentTimeMillis() - loadTime) + "ms]");

        loadTime = System.currentTimeMillis();
        Bukkit.getPluginCommand("noir").setTabCompleter(new TabHandler());
        info("命令补全器设置成功 [" + (System.currentTimeMillis() - loadTime) + "ms]");

        loadTime = System.currentTimeMillis();
        if (MiraiBot.getBot(Data.botsID).isExist()) {
            if (MiraiBot.getBot(Data.botsID).getGroup(Data.groupsID) != null) {
                init = true;
                info("插件检测到QQ已经登录, Noir已就绪");
            } else {
                warning("QQ已登录, 但QQ群不存在, 请检查配置文件");
            }
        } else {
            warning("Noir 未检测到QQ登录, 稍后QQ登陆时, Noir将自动进入就绪状态");
        }
        info("Mirai检查完成 [" + (System.currentTimeMillis() - loadTime) + "ms]");

        info("Noir 加载成功 [" + (System.currentTimeMillis() - startLoadingTime) + "ms]");
        info("-----------------------------------");

        if ("null".equals(config.getString("version"))) {
            warning("你是第一次使用Noir, 请先修改配置文件!");
            config.set("version", version);
        }
    }

    @Override
    public void onDisable() {
        info("禁用插件, 下次再见!");
        info("作者QQ: 3337913379 | Noir by Klop233");
        INSTANCE = null;
    }

    public void readToData() {
        Data.botsID = config.getLong("qq.botID");
        Data.groupsID = config.getLong("qq.group");
        Data.adminsID = config.getLongList("qq.admin");
        Data.list = config.getString("qq.commands.list");
        Data.execute = config.getString("qq.commands.execute");
        Data.bind = config.getString("qq.commands.bind");
    }
    public static void reloadNoir() {
        INSTANCE.reloadConfig();
        config = INSTANCE.getConfig();
    }

    // 简化info warning方法
    public static void info(String msg) {
        INSTANCE.getLogger().info(msg);
    }
    public static void warning(String msg) {
        INSTANCE.getLogger().warning(msg);
    }
}
