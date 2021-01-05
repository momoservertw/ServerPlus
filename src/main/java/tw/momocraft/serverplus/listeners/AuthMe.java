package tw.momocraft.serverplus.listeners;

import com.spawnchunk.auctionhouse.events.AuctionItemEvent;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.api.v3.AuthMePlayer;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

public class AuthMe implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onLoginEvent(LoginEvent e) {
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
            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgAuthMeMailEmpty(), player);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPlugin(), "AuthMe", playerName, "mail", "warning",
                    new Throwable().getStackTrace()[0]);
        }
    }
}
