package tw.momocraft.serverplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.BankReturn;
import tw.momocraft.serverplus.utils.ItemJoin;


public class Commands implements CommandExecutor {


    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        switch (args.length) {
            case 0:
                if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.use")) {
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
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.use")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgTitle(), sender);
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + ServerPlus.getInstance().getDescription().getName()
                                + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgHelp(), sender);
                        if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.command.reload")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgReload(), sender);
                        }
                        if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.command.version")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgVersion(), sender);
                        }
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "entityplus.command.reload")) {
                        ConfigHandler.generateData(true);
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.configReload", sender);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("bankcreate")) {
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "entityplus.command.bankcreate")) {
                        BankReturn.createFile();
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("bankreturn")) {
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.command.bankreturn")) {
                        if (!ConfigHandler.getConfigPath().isBankReturn()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                        if (player == null) {
                            String[] placeHolders = CorePlusAPI.getLangManager().newString();
                            placeHolders[1] = args[1]; // %targetplayer%
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.targetNotFound", sender, placeHolders);
                            return true;
                        }
                        BankReturn.give(player);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
                break;
            case 3:
                // serverplus itemjoinfix <player> [true/false]
                if (args[0].equalsIgnoreCase("itemjoinfix")) {
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.command.itemjoinfix")) {
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
                        Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                        if (player == null) {
                            String[] placeHolders = CorePlusAPI.getLangManager().newString();
                            placeHolders[1] = args[1]; // %targetplayer%
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.targetNotFound", sender, placeHolders);
                            return true;
                        }
                        boolean sendMsg;
                        try {
                            sendMsg = Boolean.getBoolean(args[2]);
                        } catch (Exception ex) {
                            sendMsg = true;
                        }
                        ItemJoin.itemJoinFix(player, sendMsg);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    if (CorePlusAPI.getPlayerManager().hasPermission(sender, "serverplus.command.update")) {
                        if (!ConfigHandler.getConfigPath().isDonate()) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                            return true;
                        }
                        if (!CorePlusAPI.getDependManager().LuckPermsEnabled()) {
                            String[] placeHolders = CorePlusAPI.getLangManager().newString();
                            placeHolders[2] = "LuckPerms"; // %plugin%
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.dependNotFound", sender);
                            return true;
                        }
                        Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                        String[] placeHolders = CorePlusAPI.getLangManager().newString();
                        placeHolders[1] = args[1]; // %targetplayer%
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