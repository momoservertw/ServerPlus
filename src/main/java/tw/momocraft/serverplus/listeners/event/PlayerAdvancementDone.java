package tw.momocraft.serverplus.listeners.event;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.api.v3.AuthMePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.Language;

public class PlayerAdvancementDone implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onAsyncPlayerChatEvent(PlayerAdvancementDoneEvent e) {
        if (!ConfigHandler.getConfigPath().isAuthMe()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isAuthMeMail()) {
            return;
        }
        Player player = e.getPlayer();
        String playerName = player.getName();
        String email = AuthMeApi.getInstance().getPlayerInfo(playerName).
                flatMap(AuthMePlayer::getEmail).orElse("EMPTY");
        if (email.equals("EMPTY")) {
            Language.sendLangMessage("Message.ServerPlus.AuthMeMailEmpty", player);
            ServerHandler.sendFeatureMessage("Event", playerName, "", "return",
                    new Throwable().getStackTrace()[0]);
        }
    }
}
