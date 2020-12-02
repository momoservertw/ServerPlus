package tw.momocraft.serverplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.RegisterHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

public class ServerPlus extends JavaPlugin {
    private static ServerPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData(false);
        RegisterHandler.registerEvents();
        ServerHandler.sendConsoleMessage("&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        ServerHandler.sendConsoleMessage("&fhas been Disabled.");
    }

    public static ServerPlus getInstance() {
        return instance;
    }
}