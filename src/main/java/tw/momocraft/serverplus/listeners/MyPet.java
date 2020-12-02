package tw.momocraft.serverplus.listeners;

import de.Keyle.MyPet.api.event.MyPetCallEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.CustomCommands;

import java.util.List;
import java.util.Random;

public class MyPet implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onMyPetCallEvent(MyPetCallEvent e) {
        if (ConfigHandler.getConfigPath().isMypet()) {
            if (ConfigHandler.getConfigPath().isMypetSkillAuto()) {
                if (e.getMyPet().getSkilltree() == null) {
                    List<String> skillList = ConfigHandler.getConfigPath().getSkillProp().get(e.getMyPet().getPetType().name());
                    if (skillList == null) {
                        skillList = ConfigHandler.getConfigPath().getSkillProp().get("Default");
                    }
                    String command = skillList.get(new Random().nextInt(skillList.size()));
                    Player player = e.getOwner().getPlayer();
                    if (player == null) {
                        player = Bukkit.getPlayer(e.getOwner().getName());
                    }
                    CustomCommands.executeMultipleCmds(player, command);
                    ServerHandler.sendFeatureMessage("MyPet", e.getOwner().getName(), "skill", "continue", command,
                            new Throwable().getStackTrace()[0]);
                }
            }
        }
    }
}
