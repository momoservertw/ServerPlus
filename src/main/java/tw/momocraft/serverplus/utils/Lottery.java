package tw.momocraft.serverplus.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Lottery {

    public static void startLottery(CommandSender sender, String group) {
        // Get the group's property.
        Map<List<String>, Double> lotteryProp = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryProp != null) {
            // Get to total Chance.
            double totalChance = 0;
            for (List<String> key : lotteryProp.keySet()) {
                totalChance += lotteryProp.get(key);
            }
            double randTotalChance = Math.random() * totalChance;
            double value;
            Random rand = new Random();
            for (List<String> key : lotteryProp.keySet()) {
                value = lotteryProp.get(key);
                // Compare the group chance with the randomly total chance.
                if (randTotalChance <= value) {
                    // Random execute a reword command from that group.
                    CustomCommands.executeCommands((Player) sender, key.get(rand.nextInt(key.size())));
                    return;
                }
                randTotalChance -= value;
            }
        } else {
            ServerHandler.sendConsoleMessage("Can not find this type: " + group);
        }
    }
}
