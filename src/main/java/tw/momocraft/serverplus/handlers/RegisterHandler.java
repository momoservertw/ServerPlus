package tw.momocraft.serverplus.handlers;

import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.Commands;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.TabComplete;
import tw.momocraft.serverplus.listeners.AuthMe;
import tw.momocraft.serverplus.listeners.ItemJoin;
import tw.momocraft.serverplus.listeners.MorphTool;
import tw.momocraft.serverplus.listeners.MyPet;

public class RegisterHandler {

    public static void registerEvents() {
        ServerPlus.getInstance().getCommand("ServerPlus").setExecutor(new Commands());
        ServerPlus.getInstance().getCommand("ServerPlus").setTabCompleter(new TabComplete());

        if (CorePlusAPI.getDependManager().MorphToolEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MorphTool(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                    "Register-Event", "MorphTool", "PrepareSmithingEvent", "continue", new Throwable().getStackTrace()[0]);
        }
        if (CorePlusAPI.getDependManager().MyPetEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new MyPet(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginName(),
                    "Register-Event", "MyPet", "MyPet", "continue", new Throwable().getStackTrace()[0]);
        }
        if (CorePlusAPI.getDependManager().AuthMeEnabled()) {
            ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new AuthMe(), ServerPlus.getInstance());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                    "Register-Event", "AuthMe", "LoginEvent", "continue", new Throwable().getStackTrace()[0]);
        }
        ServerPlus.getInstance().getServer().getPluginManager().registerEvents(new ItemJoin(), ServerPlus.getInstance());
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                "Register-Event", "ItemJoin", "InventoryClickEvent", "continue", new Throwable().getStackTrace()[0]);
        sendUtilityDepends();
    }

    private static void sendUtilityDepends() {
        String hookMsg = "&fHooked ["
                + (CorePlusAPI.getDependManager().CMIEnabled() ? "CMI, " : "")
                + (CorePlusAPI.getDependManager().MyPetEnabled() ? "MyPet, " : "")
                + (CorePlusAPI.getDependManager().MorphToolEnabled() ? "MorphTool, " : "")
                + (CorePlusAPI.getDependManager().AuthMeEnabled() ? "AuthMe, " : "")
                + (CorePlusAPI.getDependManager().PvPManagerEnabled() ? "PvPManager, " : "");
        try {
            CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPluginPrefix(), hookMsg.substring(0, hookMsg.lastIndexOf(", ")) + "]");
        } catch (Exception ignored) {
        }
        /*
        if (ResidenceEnabled()) {
            if (ConfigHandler.getConfigPath().isSpawnResFlag()) {
                FlagPermissions.addFlag("spawnbypass");
            }
        }
         */
    }
}
