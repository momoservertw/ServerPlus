package tw.momocraft.serverplus.handlers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tw.momocraft.serverplus.Commands;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.listeners.AuthMe;
import tw.momocraft.serverplus.listeners.MorphTool;
import tw.momocraft.serverplus.listeners.MyPet;
import tw.momocraft.serverplus.listeners.SyncComplete;
import tw.momocraft.serverplus.utils.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigHandler {

    private static YamlConfiguration configYAML;
    private static YamlConfiguration spigotYAML;
    private static DependAPI depends;
    private static ConfigPath configPath;
    private static UpdateHandler updater;
    private static Logger logger;
    private static Zip ziper;

    public static void generateData(boolean reload) {
        genConfigFile("config.yml");
        setDepends(new DependAPI());
        sendUtilityDepends();
        setConfigPath(new ConfigPath());
        if (!reload) {
            setUpdater(new UpdateHandler());
        }
        setLogger(new Logger());
        setZip(new Zip());
    }

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
    }

    private static void sendUtilityDepends() {
        ServerHandler.sendConsoleMessage("&fHooked [ &e"
                + (getDepends().VaultEnabled() ? "Vault, " : "")
                + (getDepends().CMIEnabled() ? "CMI, " : "")
                + (getDepends().ResidenceEnabled() ? "Residence, " : "")
                + (getDepends().PlaceHolderAPIEnabled() ? "PlaceHolderAPI, " : "")
                + (getDepends().MyPetEnabled() ? "MyPet, " : "")
                + (getDepends().ItemJoinEnabled() ? "ItemJoin, " : "")
                + (getDepends().MorphToolEnabled() ? "MorphTool, " : "")
                + (getDepends().DiscordSRVEnabled() ? "DiscordSRV, " : "")
                + (getDepends().MpdbEnabled() ? "MysqlPlayerDataBridge, " : "")
                + (getDepends().AuthMeEnabled() ? "AuthMe, " : "")
                + " &f]");
        /*
        if (ConfigHandler.getDepends().ResidenceEnabled()) {
            if (ConfigHandler.getConfigPath().isSpawnResFlag()) {
                FlagPermissions.addFlag("spawnbypass");
            }
        }
         */
    }

    public static FileConfiguration getConfig(String fileName) {
        File filePath = ServerPlus.getInstance().getDataFolder();
        File file;
        switch (fileName) {
            case "config.yml":
                filePath = Bukkit.getWorldContainer();
                if (configYAML == null) {
                    getConfigData(filePath, fileName);
                }
                break;
            case "spigot.yml":
                filePath = Bukkit.getServer().getWorldContainer();
                if (spigotYAML == null) {
                    getConfigData(filePath, fileName);
                }
                break;
            default:
                break;
        }
        file = new File(filePath, fileName);
        return getPath(fileName, file, false);
    }

    private static void getConfigData(File filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (!(file).exists()) {
            try {
                ServerPlus.getInstance().saveResource(fileName, false);
            } catch (Exception e) {
                ServerHandler.sendErrorMessage("&cCannot save " + fileName + " to disk!");
                return;
            }
        }
        getPath(fileName, file, true);
    }

    private static YamlConfiguration getPath(String fileName, File file, boolean saveData) {
        switch (fileName) {
            case "config.yml":
                if (saveData) {
                    configYAML = YamlConfiguration.loadConfiguration(file);
                }
                return configYAML;
            case "spigot.yml":
                if (saveData) {
                    spigotYAML = YamlConfiguration.loadConfiguration(file);
                }
                return spigotYAML;
        }
        return null;
    }

    private static void genConfigFile(String fileName) {
        String[] fileNameSlit = fileName.split("\\.(?=[^\\.]+$)");
        int configVersion = 0;
        File filePath = ServerPlus.getInstance().getDataFolder();
        switch (fileName) {
            case "config.yml":
                configVersion = 1;
                break;
        }
        getConfigData(filePath, fileName);
        File file = new File(filePath, fileName);
        if (file.exists() && getConfig(fileName).getInt("Config-Version") != configVersion) {
            if (ServerPlus.getInstance().getResource(fileName) != null) {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
                String currentTime = currentDate.format(formatter);
                String newGen = fileNameSlit[0] + " " + currentTime + "." + fileNameSlit[0];
                File newFile = new File(filePath, newGen);
                if (!newFile.exists()) {
                    file.renameTo(newFile);
                    File configFile = new File(filePath, fileName);
                    configFile.delete();
                    getConfigData(filePath, fileName);
                    ServerHandler.sendConsoleMessage("&4The file \"" + fileName + "\" is out of date, generating a new one!");
                }
            }
        }
        getConfig(fileName).options().copyDefaults(false);
    }

    public static DependAPI getDepends() {
        return depends;
    }

    private static void setDepends(DependAPI depend) {
        depends = depend;
    }


    private static void setConfigPath(ConfigPath configPath) {
        ConfigHandler.configPath = configPath;
    }

    public static ConfigPath getConfigPath() {
        return configPath;
    }

    public static boolean isDebugging() {
        return ConfigHandler.getConfig("config.yml").getBoolean("Debugging");
    }

    public static UpdateHandler getUpdater() {
        return updater;
    }

    private static void setUpdater(UpdateHandler update) {
        updater = update;
    }

    private static void setLogger(Logger log) {
        logger = log;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static void setZip(Zip zip) {
        ziper = zip;
    }

    public static Zip getZip() {
        return ziper;
    }
}