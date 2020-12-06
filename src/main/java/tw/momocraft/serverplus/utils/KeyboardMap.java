package tw.momocraft.serverplus.utils;

import java.util.List;

public class KeyboardMap {

    private boolean cancel;
    private boolean cooldownMsg;
    private int cooldown;
    private List<String> commands;


    public boolean isCooldownMsg() {
        return cooldownMsg;
    }

    public int getCooldown() {
        return cooldown;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setCooldownMsg(boolean cooldownMsg) {
        this.cooldownMsg = cooldownMsg;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
