package tw.momocraft.serverplus.utils;

import java.util.List;

public class DonateMap {

    private String group;
    private String nextGroup;
    private List<String> commands;
    private List<String> failedCommands;

    public String getGroup() {
        return group;
    }

    public String getNextGroup() {
        return nextGroup;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getFailedCommands() {
        return failedCommands;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setNextGroup(String nextGroup) {
        this.nextGroup = nextGroup;
    }

    public void setCommands(List<String> commandList) {
        this.commands = commandList;
    }

    public void setFailedCommands(List<String> failedCommands) {
        this.failedCommands = failedCommands;
    }
}
