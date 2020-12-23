package tw.momocraft.serverplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.ItemJoin;


public class Commands implements CommandExecutor {


    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        switch (args.length) {
            case 0:
                if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.use")) {
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgTitle(), sender);
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + ServerPlus.getInstance().getDescription().getName()
                            + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgHelp(), sender);
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                }
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.use")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgTitle(), sender);
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + ServerPlus.getInstance().getDescription().getName()
                                + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgHelp(), sender);
                        if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.command.reload")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgReload(), sender);
                        }
                        if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.command.version")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgVersion(), sender);
                        }
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "entityplus.command.reload")) {
                        ConfigHandler.generateData(true);
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.configReload", sender);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("version")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "entityplus.command.version")) {
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + ServerPlus.getInstance().getDescription().getName()
                                + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                        CorePlusAPI.getUpdateManager().check(ConfigHandler.getPrefix(), sender, ServerPlus.getInstance().getName(), ServerPlus.getInstance().getDescription().getVersion());
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                    // serverplus itemjoinfix
                } else if (args[0].equalsIgnoreCase("itemjoinfix")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.command.itemjoinfix")) {
                        if (!ConfigHandler.getConfigPath().isItemjoin()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!ConfigHandler.getConfigPath().isIjFixOldItem()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!ConfigHandler.getDepends().ItemJoinEnabled()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if ((sender instanceof ConsoleCommandSender)) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.onlyPlayer", sender);
                            return true;
                        }
                        ItemJoin.itemJoinFix((Player) sender, true);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
                break;
            case 2:
                // serverplus itemjoinfix <player>
                if (args[0].equalsIgnoreCase("itemjoinfix")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.command.itemjoinfix")) {
                        if (!ConfigHandler.getConfigPath().isItemjoin()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!ConfigHandler.getConfigPath().isIjFixOldItem()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!ConfigHandler.getDepends().ItemJoinEnabled()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if ((sender instanceof ConsoleCommandSender)) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.onlyPlayer", sender);
                            return true;
                        }
                        boolean msg;
                        try {
                            msg = Boolean.getBoolean(args[1]);
                        } catch (Exception ex) {
                            msg = true;
                        }
                        ItemJoin.itemJoinFix((Player) sender, msg);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
                break;
            case 3:
                // serverplus update <player> <group>
                if (args[0].equalsIgnoreCase("update")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "serverplus.command.update")) {
                        if (!ConfigHandler.getConfigPath().isDonate()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!CorePlusAPI.getDependManager().LuckPermsEnabled()) {
                            String[] placeHolders = CorePlusAPI.getLangManager().newString();
                            placeHolders[13] = "LuckPerms"; // %plugin%
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.dependNotFound", sender);
                            return true;
                        }
                        Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                        String[] placeHolders = CorePlusAPI.getLangManager().newString();
                        placeHolders[2] = args[1]; // %targetplayer%
                        if (player == null) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.targetNotFound", sender, placeHolders);
                            return true;
                        }
                        Donate.promote(sender, player, args[2]);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
                break;
        }
        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.unknownCommand", sender);
        return true;
    }
}