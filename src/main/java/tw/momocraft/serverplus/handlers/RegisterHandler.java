package tw.momocraft.serverplus.handlers;

import tw.momocraft.serverplus.Commands;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.listeners.*;
import tw.momocraft.serverplus.listeners.event.*;
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
        if (ConfigHandler.getDepends().MpdbEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new SyncComplete(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "ItemJoin", "SyncCompleteEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().AuthMeEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new AuthMe(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "AuthMe", "LoginEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }

            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new Hotkey(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "Hotkey", "Hotkey", "continue",
                    new Throwable().getStackTrace()[0]);

        Map<String, Boolean> eventRegisterProp = ConfigHandler.getConfigPath().getEventRegisterProp();
        for (String event : eventRegisterProp.keySet()) {
            if (!eventRegisterProp.get(event)) {
                continue;
            }
            switch (event) {
                case "PlayerJoinEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new PlayerJoin(), ServerPlus.getInstance());
                    ServerHandler.sendFeatureMessage("Register-Event", "Event", "PlayerJoin", "continue",
                            new Throwable().getStackTrace()[0]);
                    break;
                case "PlayerQuitEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new PlayerQuit(), ServerPlus.getInstance());
                    ServerHandler.sendFeatureMessage("Register-Event", "Event", "PlayerQuitEvent", "continue",
                            new Throwable().getStackTrace()[0]);
                    break;
                case "PlayerInteractEntityEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new PlayerInteractEntity(), ServerPlus.getInstance());
                    ServerHandler.sendFeatureMessage("Register-Event", "Event", "PlayerInteractEntity", "continue",
                            new Throwable().getStackTrace()[0]);
                    break;
                case "PlayerItemHeldEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new PlayerItemHeld(), ServerPlus.getInstance());
                    ServerHandler.sendFeatureMessage("Register-Event", "Event", "PlayerItemHeld", "continue",
                            new Throwable().getStackTrace()[0]);
                    break;
                case "PlayerSwapHandItemsEvent":
                    ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new PlayerSwapHandItems(), ServerPlus.getInstance());
                    ServerHandler.sendFeatureMessage("Register-Event", "Event", "PlayerSwapHandItems", "continue",
                            new Throwable().getStackTrace()[0]);
                    break;
            }
        }
    }
}
