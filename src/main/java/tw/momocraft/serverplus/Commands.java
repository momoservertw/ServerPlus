package tw.momocraft.serverplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.PermissionsHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.ItemJoin;
import tw.momocraft.serverplus.utils.Language;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        if (args.length == 0) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.use")) {
                Language.dispatchMessage(sender, "");
                Language.sendLangMessage("Message.ServerPlus.Commands.title", sender, false);
                if (PermissionsHandler.hasPermission(sender, "ServerPlus.command.version")) {
                    Language.dispatchMessage(sender, "&d&lServerPlus &e&lv" + ServerPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                }
                Language.sendLangMessage("Message.ServerPlus.Commands.help", sender, false);
                Language.dispatchMessage(sender, "");
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.use")) {
                Language.dispatchMessage(sender, "");
                Language.sendLangMessage("Message.ServerPlus.Commands.title", sender, false);
                if (PermissionsHandler.hasPermission(sender, "ServerPlus.command.version")) {
                    Language.dispatchMessage(sender, "&d&lServerPlus &e&lv" + ServerPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                }
                Language.sendLangMessage("Message.ServerPlus.Commands.help", sender, false);
                if (PermissionsHandler.hasPermission(sender, "ServerPlus.command.reload")) {
                    Language.sendLangMessage("Message.ServerPlus.Commands.reload", sender, false);
                }
                Language.dispatchMessage(sender, "");
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.command.reload")) {
                // working: close purge.Auto-Clean schedule
                ConfigHandler.generateData(true);
                Language.sendLangMessage("Message.configReload", sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.command.version")) {
                Language.dispatchMessage(sender, "&d&lServerPlus &e&lv" + ServerPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                ConfigHandler.getUpdater().checkUpdates(sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("itemjoinfix")) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.command.itemjoinfix")) {
                if (!ConfigHandler.getConfigPath().isItemjoin()) {
                    ServerHandler.sendConsoleMessage("ItemJoin: Disabled");
                    return true;
                }
                if (!ConfigHandler.getConfigPath().isIjFixOldItem()) {
                    ServerHandler.sendConsoleMessage("FixOldItem: Disabled");
                    return true;
                }
                if (!ConfigHandler.getDepends().ItemJoinEnabled()) {
                    ServerHandler.sendConsoleMessage("ItemJoin plugin: Disabled");
                    return true;
                }
                if ((sender instanceof ConsoleCommandSender)) {
                    ServerHandler.sendConsoleMessage("Only player can run this item.");
                    return true;
                }
                ItemJoin.itemJoinFix((Player) sender, true);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("itemjoinfix")) {
            if (PermissionsHandler.hasPermission(sender, "serverplus.command.itemjoinfix")) {
                if (!ConfigHandler.getConfigPath().isItemjoin()) {
                    ServerHandler.sendConsoleMessage("ItemJoin: Disabled");
                    return true;
                }
                if (!ConfigHandler.getConfigPath().isIjFixOldItem()) {
                    ServerHandler.sendConsoleMessage("FixOldItem: Disabled");
                    return true;
                }
                if (!ConfigHandler.getDepends().ItemJoinEnabled()) {
                    ServerHandler.sendConsoleMessage("ItemJoin plugin: Disabled");
                    return true;
                }
                if ((sender instanceof ConsoleCommandSender)) {
                    ServerHandler.sendConsoleMessage("Only player can run this item.");
                    return true;
                }
                boolean msg = Boolean.getBoolean(args[1]);
                ItemJoin.itemJoinFix((Player) sender, msg);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else {
            Language.sendLangMessage("Message.unknownCommand", sender);
            return true;
        }
    }
}