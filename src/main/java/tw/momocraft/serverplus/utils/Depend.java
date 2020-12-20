package tw.momocraft.serverplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

public class Depend {
    private boolean Residence = false;
    private boolean CMI = false;
    private boolean MyPet = false;
    private boolean ItemJoin = false;
    private boolean MorphTool = false;
    private boolean DiscordSRV = false;
    private boolean MysqlPlayerDataBridge = false;
    private boolean AuthMe = false;

    public Depend() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.Residence")) {
            this.setResidenceStatus(Bukkit.getServer().getPluginManager().getPlugin("Residence") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.CMI")) {
            this.setCMIStatus(Bukkit.getServer().getPluginManager().getPlugin("CMI") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.MyPet")) {
            this.setMyPetStatus(Bukkit.getServer().getPluginManager().getPlugin("MyPet") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.ItemJoin")) {
            this.setItemJoinStatus(Bukkit.getServer().getPluginManager().getPlugin("ItemJoin") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.MorphTool")) {
            this.setMorphToolStatus(Bukkit.getServer().getPluginManager().getPlugin("MorphTool") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.DiscordSRV")) {
            this.setDiscordSRVStatus(Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.MysqlPlayerDataBridge")) {
            this.setMpbdStatus(Bukkit.getServer().getPluginManager().getPlugin("MysqlPlayerDataBridge") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.AuthMe")) {
            this.setAuthMeStatus(Bukkit.getServer().getPluginManager().getPlugin("AuthMe") != null);
        }

        sendUtilityDepends();
    }

    private void sendUtilityDepends() {
        String hookMsg = "&fHooked [ &e"
                + (CMIEnabled() ? "CMI, " : "")
                + (ResidenceEnabled() ? "Residence, " : "")
                + (MyPetEnabled() ? "MyPet, " : "")
                + (ItemJoinEnabled() ? "ItemJoin, " : "")
                + (MorphToolEnabled() ? "MorphTool, " : "")
                + (DiscordSRVEnabled() ? "DiscordSRV, " : "")
                + (MpdbEnabled() ? "MysqlPlayerDataBridge, " : "")
                + (AuthMeEnabled() ? "AuthMe, " : "")
                + " &f]";
        try {
            CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), hookMsg.substring(0, hookMsg.lastIndexOf(", ")) + " &f]");
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


    public boolean ResidenceEnabled() {
        return this.Residence;
    }

    public boolean CMIEnabled() {
        return this.CMI;
    }

    public boolean MyPetEnabled() {
        return this.MyPet;
    }

    public boolean ItemJoinEnabled() {
        return this.ItemJoin;
    }

    public boolean MorphToolEnabled() {
        return this.MorphTool;
    }

    public boolean DiscordSRVEnabled() {
        return this.DiscordSRV;
    }

    public boolean MpdbEnabled() {
        return this.MysqlPlayerDataBridge;
    }


    public boolean AuthMeEnabled() {
        return this.AuthMe;
    }

    public void setResidenceStatus(boolean bool) {
        this.Residence = bool;
    }

    public void setCMIStatus(boolean bool) {
        this.CMI = bool;
    }

    public void setMyPetStatus(boolean bool) {
        this.MyPet = bool;
    }

    public void setItemJoinStatus(boolean bool) {
        this.ItemJoin = bool;
    }

    public void setMorphToolStatus(boolean bool) {
        this.MorphTool = bool;
    }

    public void setDiscordSRVStatus(boolean bool) {
        this.DiscordSRV = bool;
    }

    public void setMpbdStatus(boolean bool) {
        this.MysqlPlayerDataBridge = bool;
    }

    private void setAuthMeStatus(boolean bool) {
        this.AuthMe = bool;
    }
}
