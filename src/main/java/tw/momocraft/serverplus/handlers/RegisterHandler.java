package tw.momocraft.serverplus.handlers;

import tw.momocraft.serverplus.Commands;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.listeners.AuthMe;
import tw.momocraft.serverplus.listeners.MorphTool;
import tw.momocraft.serverplus.listeners.MyPet;
import tw.momocraft.serverplus.listeners.SyncComplete;
import tw.momocraft.serverplus.utils.TabComplete;

import java.util.Map;

public class RegisterHandler {

    public static void registerEvents() {
        ServerPlus.getInstance().getCommand("ServerPlus").setExecutor(new Commands());
        ServerPlus.getInstance().getCommand("ServerPlus").setTabCompleter(new TabComplete());

        if (ConfigHandler.getDepends().MyPetEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MyPet(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "MyPet", "MyPet", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().MorphToolEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MorphTool(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "MorphTool", "PrepareSmithingEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().ItemJoinEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new SyncComplete(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "ItemJoin", "PlayerJoinEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().AuthMeEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new AuthMe(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "AuthMe", "LoginEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }

        Map<String, Boolean> eventRegisterProp = ConfigHandler.getConfigPath().getEventRegisterProp();
        for (String event : eventRegisterProp.keySet()) {
            if (!eventRegisterProp.get(event)) {
                continue;
            }
            switch (event) {
                case "AsyncPlayerChatEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new AuthMe(), ServerPlus.getInstance());
                    break;
            }
        }
    }
}
