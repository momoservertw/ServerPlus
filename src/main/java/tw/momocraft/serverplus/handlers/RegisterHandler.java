package tw.momocraft.serverplus.handlers;

import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.Commands;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.listeners.*;
import tw.momocraft.serverplus.utils.TabComplete;

public class RegisterHandler {

    public static void registerEvents() {
        ServerPlus.getInstance().getCommand("ServerPlus").setExecutor(new Commands());
        ServerPlus.getInstance().getCommand("ServerPlus").setTabCompleter(new TabComplete());

        if (ConfigHandler.getDepends().MyPetEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MyPet(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "Register-Event", "MyPet", "MyPet", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().MorphToolEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MorphTool(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "Register-Event", "MorphTool", "PrepareSmithingEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().MpdbEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new SyncComplete(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "Register-Event", "ItemJoin", "SyncCompleteEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        if (ConfigHandler.getDepends().AuthMeEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new AuthMe(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "Register-Event", "AuthMe", "LoginEvent", "continue",
                    new Throwable().getStackTrace()[0]);
        }
    }
}
