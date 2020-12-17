package tw.momocraft.serverplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.RegisterHandler;

public class ServerPlus extends JavaPlugin {
    private static ServerPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData(false);
        RegisterHandler.registerEvents();
        CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), "&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), "&fhas been Disabled.");
    }

    public static ServerPlus getInstance() {
        return instance;
    }
}