package tw.momocraft.serverplus.utils;

import javafx.util.Pair;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.Map;

public class BankReturn {

    public static void createFile() {
        Map<String, Pair<String, String>> playerData;
        try {
            playerData = ConfigHandler.getMySQLApi().getBankData();
        } catch (Exception e) {
            CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), "failed to create Bank-Return file.");
            e.printStackTrace();
            return;
        }
        for (String uuid : playerData.keySet()) {
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ServerPlus, " + uuid + ":");
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ServerPlus,   money: " + playerData.get(uuid).getKey());
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ServerPlus,   exp: " + playerData.get(uuid).getValue());
        }
        CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), "succeed to create Bank-Return file.");
    }

    public static void give(Player player) {
        String playerName = player.getName();
        String uuid = player.getUniqueId().toString();
        int money = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".money");
        int exp = ConfigHandler.getConfig("temporary.yml").getInt(uuid + ".exp");

        if (money != 0) {
            CorePlusAPI.getCommandManager().executeCmd(ConfigHandler.getPrefix(), player, "console: cmi money give " + playerName + " " + money, false);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPlugin(), "Bank-Return", playerName, "money", "success", "console: cmi money give " + playerName + " " + money,
                    new Throwable().getStackTrace()[0]);
        }
        if (exp != 0) {
            CorePlusAPI.getCommandManager().executeCmd(ConfigHandler.getPrefix(), player, "console: cmi exp add " + playerName + " " + exp, false);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPlugin(), "Bank-Return", playerName, "money", "success", "console: cmi exp add " + playerName + " " + exp,
                    new Throwable().getStackTrace()[0]);
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "BankReturn, " + uuid + ":");
        }
    }
}
