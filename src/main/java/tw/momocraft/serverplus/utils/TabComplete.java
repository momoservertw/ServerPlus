package tw.momocraft.serverplus.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import tw.momocraft.coreplus.api.CorePlusAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        /*
        Collection<?> playersOnlineNew;
        Player[] playersOnlineOld;
        try {
            if (Bukkit.class.getMethod("getOnlinePlayers").getReturnType() == Collection.class) {
                if (Bukkit.class.getMethod("getOnlinePlayers").getReturnType() == Collection.class) {
                    playersOnlineNew = ((Collection<?>) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                    for (Object objPlayer : playersOnlineNew) {
                        commands.add(((Player) objPlayer).getName());
                    }
                }
            } else {
                playersOnlineOld = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                for (Player player : playersOnlineOld) {
                    commands.add(player.getName());
                }
            }
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
         */
        switch (args.length) {
            case 1:
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.use")) {
                    commands.add("help");
                }
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.command.reload")) {
                    commands.add("reload");
                }
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.command.version")) {
                    commands.add("version");
                }
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.command.buy")) {
                    commands.add("buy");
                }
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.command.give")) {
                    commands.add("give");
                }
                if (CorePlusAPI.getPermManager().hasPermission(sender, "barrierplus.command.itemjoinfix")) {
                    commands.add("itemjoinfix");
                }
                break;
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}