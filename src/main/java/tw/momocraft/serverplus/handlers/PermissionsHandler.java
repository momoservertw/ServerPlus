package tw.momocraft.serverplus.handlers;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class PermissionsHandler {

	public static boolean hasPermission(CommandSender sender, String permission) {
		if (sender.hasPermission(permission) || sender.hasPermission("ServerPlus.*") || sender.isOp() || (sender instanceof ConsoleCommandSender)) {
			return true;
		}
		return false;
	}
}