package tw.momocraft.serverplus.listeners;

import de.Keyle.MyPet.api.event.MyPetCallEvent;
import de.Keyle.MyPet.api.event.MyPetCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.CustomCommands;

import java.util.List;
import java.util.Random;

public class MyPet implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onMyPetCallEvent(MyPetCallEvent e) {
        if (ConfigHandler.getConfigPath().isMypet()) {
            if (ConfigHandler.getConfigPath().isMypetSkillAuto()) {
                if (ConfigHandler.getConfigPath().isMypetSkillCall()) {
                    if (e.getMyPet().getSkilltree() == null) {
                        List<String> skillList = ConfigHandler.getConfigPath().getSkillProp().get(e.getMyPet().getPetType().name());
                        if (skillList.isEmpty()) {
                            skillList = ConfigHandler.getConfigPath().getSkillProp().get("all");
                            if (skillList.isEmpty()) {
                                return;
                            }
                        }
                        CustomCommands.executeCommands(e.getOwner().getPlayer(), skillList.get(new Random().nextInt(skillList.size())));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMyPetCreateEvent(MyPetCreateEvent e) {
        if (ConfigHandler.getConfigPath().isMypet()) {
            if (ConfigHandler.getConfigPath().isMypetSkillAuto()) {
                if (ConfigHandler.getConfigPath().isMypetSkillCreate()) {
                    if (e.getMyPet().getSkilltree() == null) {
                        List<String> skillList = ConfigHandler.getConfigPath().getSkillProp().get(e.getMyPet().getPetType().name());
                        if (skillList.isEmpty()) {
                            skillList = ConfigHandler.getConfigPath().getSkillProp().get("all");
                            if (skillList.isEmpty()) {
                                return;
                            }
                        }
                        CustomCommands.executeCommands(e.getOwner().getPlayer(), skillList.get(new Random().nextInt(skillList.size())));
                    }
                }
            }
        }
    }
}
