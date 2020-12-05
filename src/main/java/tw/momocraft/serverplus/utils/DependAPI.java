package tw.momocraft.serverplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

public class DependAPI {
    private VaultAPI vaultApi;
    private PlayerPointsAPI playerPointsApi;

    private boolean Vault = false;
    private boolean PlaceHolderAPI = false;
    private boolean Residence = false;
    private boolean CMI = false;
    private boolean MyPet = false;
    private boolean ItemJoin = false;
    private boolean MorphTool = false;
    private boolean DiscordSRV = false;
    private boolean MysqlPlayerDataBridge = false;
    private boolean AuthMe = false;
    private boolean LangUtils = false;
    private boolean PlayerPoints = false;

    public DependAPI() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.Vault")) {
            this.setVaultStatus(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null);
            if (Vault) {
                setVaultApi();
            }
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.PlayerPoints")) {
            this.setPlayerPointsStatus(Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints") != null);
            if (PlayerPoints) {
                setPlayerPointsApi();
            }
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.LangUtils")) {
            this.setLangUtilsStatus(Bukkit.getServer().getPluginManager().getPlugin("LangUtils") != null);
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.PlaceHolderAPI")) {
            this.setPlaceHolderStatus(Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null);
        }
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
        ServerHandler.sendConsoleMessage("&fHooked [ &e"
                + (VaultEnabled() ? "Vault, " : "")
                + (CMIEnabled() ? "CMI, " : "")
                + (ResidenceEnabled() ? "Residence, " : "")
                + (PlaceHolderAPIEnabled() ? "PlaceHolderAPI, " : "")
                + (MyPetEnabled() ? "MyPet, " : "")
                + (ItemJoinEnabled() ? "ItemJoin, " : "")
                + (MorphToolEnabled() ? "MorphTool, " : "")
                + (DiscordSRVEnabled() ? "DiscordSRV, " : "")
                + (MpdbEnabled() ? "MysqlPlayerDataBridge, " : "")
                + (AuthMeEnabled() ? "AuthMe, " : "")
                + (LangUtilsEnabled() ? "LangUtils, " : "")
                + (PlayerPointsEnabled() ? "PlayerPoints, " : "")
                + " &f]");
        /*
        if (ResidenceEnabled()) {
            if (ConfigHandler.getConfigPath().isSpawnResFlag()) {
                FlagPermissions.addFlag("spawnbypass");
            }
        }
         */
    }

    public boolean VaultEnabled() {
        return this.Vault;
    }

    public boolean PlaceHolderAPIEnabled() {
        return this.PlaceHolderAPI;
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

    public boolean LangUtilsEnabled() {
        return this.LangUtils;
    }

    public boolean PlayerPointsEnabled() {
        return this.PlayerPoints;
    }


    public void setVaultStatus(boolean bool) {
        this.Vault = bool;
    }

    public boolean AuthMeEnabled() {
        return this.AuthMe;
    }

    public void setPlaceHolderStatus(boolean bool) {
        this.PlaceHolderAPI = bool;
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

    private void setLangUtilsStatus(boolean bool) {
        this.LangUtils = bool;
    }

    private void setPlayerPointsStatus(boolean bool) {
        this.PlayerPoints = bool;
    }

    public VaultAPI getVaultApi() {
        return this.vaultApi;
    }

    private void setVaultApi() {
        vaultApi = new VaultAPI();
    }

    public PlayerPointsAPI getPlayerPointsApi() {
        return this.playerPointsApi;
    }

    private void setPlayerPointsApi() {
        playerPointsApi = new PlayerPointsAPI();
    }
}
