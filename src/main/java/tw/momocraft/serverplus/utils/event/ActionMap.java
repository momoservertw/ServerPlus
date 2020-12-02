package tw.momocraft.serverplus.utils.event;

import java.util.List;

public class ActionMap {
    private List<String> commands;
    private String cancel;
    private String kill;
    private String killTarget;
    private List<String> cleanSlots;
    private List<String> cleanSlotTarget;
    private String petInfo;
    private List<ActionMap> succeedMap;
    private List<ActionMap> failedMap;


    public String getPetInfo() {
        return petInfo;
    }

    public String getKillTarget() {
        return killTarget;
    }

    public String getKill() {
        return kill;
    }

    public List<String> getCleanSlotTarget() {
        return cleanSlotTarget;
    }

    public List<String> getCleanSlots() {
        return cleanSlots;
    }

    public String getCancel() {
        return cancel;
    }

    public List<ActionMap> getFailedMap() {
        return failedMap;
    }

    public List<ActionMap> getSucceedMap() {
        return succeedMap;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setKillTarget(String killTarget) {
        this.killTarget = killTarget;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public void setCleanSlotTarget(List<String> cleanSlotTarget) {
        this.cleanSlotTarget = cleanSlotTarget;
    }

    public void setCleanSlots(List<String> cleanSlots) {
        this.cleanSlots = cleanSlots;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setFailedMap(List<ActionMap> failedMap) {
        this.failedMap = failedMap;
    }

    public void setSucceedMap(List<ActionMap> succeedMap) {
        this.succeedMap = succeedMap;
    }

    public void setPetInfo(String petInfo) {
        this.petInfo = petInfo;
    }
}
