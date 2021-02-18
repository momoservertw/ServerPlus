package tw.momocraft.serverplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.listeners.ItemJoin;
import tw.momocraft.serverplus.utils.BankReturn;


public class Commands implements CommandExecutor {


    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        int length = args.length;
        if (length == 0) {
            if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.use")) {
                CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                        ConfigHandler.getConfigPath().getMsgTitle(), sender);
                CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender,
                        "&f " + ServerPlus.getInstance().getDescription().getName()
                        + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                        ConfigHandler.getConfigPath().getMsgHelp(), sender);
                CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
            } else {
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                        "Message.noPermission", sender);
            }
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.use")) {
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            ConfigHandler.getConfigPath().getMsgTitle(), sender);
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender,
                            "&f " + ServerPlus.getInstance().getDescription().getName()
                            + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            ConfigHandler.getConfigPath().getMsgHelp(), sender);
                    if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.reload")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                ConfigHandler.getConfigPath().getMsgReload(), sender);
                    }
                    if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.version")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                ConfigHandler.getConfigPath().getMsgVersion(), sender);
                    }
                    if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.itemjoinfixconfig")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                ConfigHandler.getConfigPath().getMsgCmdItemJoinFix(), sender);
                    }
                    if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.bankreturn")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                ConfigHandler.getConfigPath().getMsgCmdBankReturn(), sender);
                    }
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "reload":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.reload")) {
                    ConfigHandler.generateData(true);
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.configReload", sender);
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "version":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.version")) {
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender,
                            "&f " + ServerPlus.getInstance().getDescription().getName()
                            + " &ev" + ServerPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getUpdateManager().check(ConfigHandler.getPrefix(), sender,
                            ServerPlus.getInstance().getName(), ServerPlus.getInstance().getDescription().getVersion(), true);
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "itemjoinfixconfig":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.itemjoinfixconfig")) {
                    if (!ConfigHandler.getConfigPath().isBankReturn()) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.featureDisabled", sender);
                        return true;
                    }
                    ItemJoin.itemJoinFixConfig();
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "bankreturn":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.bankreturn")) {
                    if (!ConfigHandler.getConfigPath().isBankReturn()) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.featureDisabled", sender);
                        return true;
                    }
                    Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                    if (player == null) {
                        String[] placeHolders = CorePlusAPI.getLangManager().newString();
                        placeHolders[1] = args[1]; // %targetplayer%
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.targetNotFound", sender, placeHolders);
                        return true;
                    }
                    BankReturn.give(player);
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "donate":
                if (CorePlusAPI.getPlayerManager().hasPerm(sender, "serverplus.command.donate")) {
                    if (length == 3) {
                        if (args[1].equalsIgnoreCase("add")) {

                        } else if (args[1].equalsIgnoreCase("add")) {

                        }
                    }
                    if (!ConfigHandler.getConfigPath().isDonate()) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.featureDisabled", sender);
                        return true;
                    }
                    if (!CorePlusAPI.getDependManager().LuckPermsEnabled()) {
                        String[] placeHolders = CorePlusAPI.getLangManager().newString();
                        placeHolders[2] = "LuckPerms"; // %plugin%
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.dependNotFound", sender);
                        return true;
                    }
                    Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[1]);
                    String[] placeHolders = CorePlusAPI.getLangManager().newString();
                    placeHolders[1] = args[1]; // %targetplayer%
                    if (player == null) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                                "Message.targetNotFound", sender, placeHolders);
                        return true;
                    }
                    Donate.promote(sender, player, args[2]);
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
        }
        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(),
                "Message.unknownCommand", sender);
        return true;
    }
}