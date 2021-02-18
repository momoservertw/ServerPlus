package tw.momocraft.serverplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

public class Dependence {
    private boolean CMI = false;
    private boolean MyPet = false;
    private boolean MorphTool = false;
    private boolean AuthMe = false;
    private boolean PvPManager = false;

    public Dependence() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.CMI")) {
            this.setCMIStatus(Bukkit.getServer().getPluginManager().getPlugin("CMI") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.MyPet")) {
            this.setMyPetStatus(Bukkit.getServer().getPluginManager().getPlugin("MyPet") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.MorphTool")) {
            this.setMorphToolStatus(Bukkit.getServer().getPluginManager().getPlugin("MorphTool") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.PvPManager")) {
            this.setPvPManagerStatus(Bukkit.getServer().getPluginManager().getPlugin("PvPManager") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.AuthMe")) {
            this.setAuthMeStatus(Bukkit.getServer().getPluginManager().getPlugin("AuthMe") != null);
        }

        sendUtilityDepends();
    }

    private void sendUtilityDepends() {
        String hookMsg = "&fHooked ["
                + (CMIEnabled() ? "CMI, " : "")
                + (MyPetEnabled() ? "MyPet, " : "")
                + (MorphToolEnabled() ? "MorphTool, " : "")
                + (AuthMeEnabled() ? "AuthMe, " : "")
                + (PvPManagerEnabled() ? "PvPManager, " : "");
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

    public boolean CMIEnabled() {
        return this.CMI;
    }

    public boolean MyPetEnabled() {
        return this.MyPet;
    }

    public boolean MorphToolEnabled() {
        return this.MorphTool;
    }

    public boolean PvPManagerEnabled() {
        return this.PvPManager;
    }


    public boolean AuthMeEnabled() {
        return this.AuthMe;
    }

    public void setCMIStatus(boolean bool) {
        this.CMI = bool;
    }

    public void setMyPetStatus(boolean bool) {
        this.MyPet = bool;
    }

    public void setMorphToolStatus(boolean bool) {
        this.MorphTool = bool;
    }

    private void setAuthMeStatus(boolean bool) {
        this.AuthMe = bool;
    }

    private void setPvPManagerStatus(boolean bool) {
        this.PvPManager = bool;
    }
}
