package tw.momocraft.serverplus.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.handlers.ServerHandler;

public class BungeeCord implements PluginMessageListener {

	public static void SwitchServers(Player player, String server) {
		Messenger messenger = ServerPlus.getInstance().getServer().getMessenger();
		if (!messenger.isOutgoingChannelRegistered(ServerPlus.getInstance(), "BungeeCord")) {
			messenger.registerOutgoingPluginChannel(ServerPlus.getInstance(), "BungeeCord");
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (Exception e) { ServerHandler.sendDebugTrace(e); }
		player.sendPluginMessage(ServerPlus.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void ExecuteCommand(Player player, String cmd) {
		Messenger messenger = ServerPlus.getInstance().getServer().getMessenger();
		if (!messenger.isOutgoingChannelRegistered(ServerPlus.getInstance(), "BungeeCord")) {
			messenger.registerOutgoingPluginChannel(ServerPlus.getInstance(), "BungeeCord");
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		try {
			out.writeUTF("Subchannel");
			out.writeUTF("Argument");
			out.writeUTF(cmd);
		} catch (Exception e) { ServerHandler.sendDebugTrace(e); }
		player.sendPluginMessage(ServerPlus.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) { return; }
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in .readUTF();
		if (!subchannel.contains("PlayerCount")) {
			player.sendMessage(subchannel + " " + in .readByte());
		}
	} 
}