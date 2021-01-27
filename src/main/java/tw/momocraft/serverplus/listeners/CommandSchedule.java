package tw.momocraft.serverplus.listeners;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.scheduler.BukkitRunnable;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.handlers.ConfigHandler;

public class CommandSchedule {

    /*
    1. Information send arrive.
    2. Start waiting...
    3.

     */
    // Table<PlayerName, sendTime, waitingTime>
    private static final Table<String, Long, Long> waitingTable = HashBasedTable.create();

    // /serverplus runcmd COMMAND
    private static void setWaitingCommand(String input) {
        if (!input.contains("%waiting_%")) {
        }
    }

    // %waiting_PlayerName_WaitingTime%
    private static void addWaiting(String input) {
        input = input.replace("%waiting_", "");
        input = input.replace("%", "");
        String[] inputSplit = input.split("_");
        startWaiting(inputSplit[0], System.currentTimeMillis(),Long.parseLong(inputSplit[1]));
    }

    /**
     *
     * @param playerName the target player name.
     * @param sendTime the send arrive Millis.
     * @param waitingTime the waiting seconds.
     */
    private static void startWaiting(String playerName, long sendTime, long waitingTime) {
        waitingTable.put(playerName, sendTime, waitingTime * 1000);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    waitingTable.remove(playerName, sendTime);
                } catch (Exception ignored) {
                }
            }
        }.runTaskLater(ServerPlus.getInstance(), waitingTime / 20);
    }

    private static String isWaiting(String playerName) {
        if (!waitingTable.rowKeySet().contains(playerName)) {
            return false;
        }
        long waitingTime;
        for (long sendTime : waitingTable.row(playerName).keySet()) {
            waitingTime = waitingTable.get(playerName, sendTime);
            if (System.currentTimeMillis() - waitingTime < sendTime) {
                CorePlusAPI.getCommandManager().ex
                        (ConfigHandler.isDebugging(), ConfigHandler.getPluginName(), );
                waitingTable.remove(playerName, sendTime);
                return true;
            }
        }
        return false;
    }
}
