package tw.momocraft.serverplus.utils;

import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;


public class BankReturn {

    public static void give(Player player) {
        String playerName = player.getName();
        String uuid = player.getUniqueId().toString();
        int money = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".money");
        int exp = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".exp");

        if (money != 0) {
            CorePlusAPI.getCommandManager().executeCmd(ConfigHandler.getPrefix(), player,
                    "console: cmi money give " + playerName + " " + money, false);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                    "Bank-Return", playerName, "money", "success", "console: cmi money give " + playerName + " " + money,
                    new Throwable().getStackTrace()[0]);
        }
        if (exp != 0) {
            CorePlusAPI.getCommandManager().executeCmd(ConfigHandler.getPrefix(), player,
                    "console: cmi exp add " + playerName + " " + exp, false);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                    "Bank-Return", playerName, "money", "success", "console: cmi exp add " + playerName + " " + exp,
                    new Throwable().getStackTrace()[0]);
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "BankReturn, " + uuid + ":");
        }
    }
}
