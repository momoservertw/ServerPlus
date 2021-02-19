package tw.momocraft.serverplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.UUID;


public class BankReturn {

    public static void start() {
        ConfigurationSection config = ConfigHandler.getConfig("temporary.yml").getConfigurationSection("");
        if (config == null) {
            return;
        }
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            int money = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".money");
            if (money != 0) {
                CorePlusAPI.getPlayerManager().giveCurrency(uuid, "money", money);
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                        "Bank-Return", uuidString, "money", "success", String.valueOf(money),
                        new Throwable().getStackTrace()[0]);
            }
            int exp = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".exp");
            if (exp != 0) {
                CorePlusAPI.getPlayerManager().giveExp(ConfigHandler.getPluginName(), uuid, exp);
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                        "Bank-Return", uuidString, "exp", "success", String.valueOf(exp),
                        new Throwable().getStackTrace()[0]);
            }
        }
    }
}
