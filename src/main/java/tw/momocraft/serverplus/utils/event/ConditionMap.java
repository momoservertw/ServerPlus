package tw.momocraft.serverplus.utils.event;

import java.util.List;

public class ConditionMap {
    private String holdingMenu;
    private List<String> placeholders;
    private List<ConditionMap> succeedMap;
    private List<ConditionMap> failedMap;


    public String getHoldingMenu() {
        return holdingMenu;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }


    public List<ConditionMap> getFailedMap() {
        return failedMap;
    }

    public List<ConditionMap> getSucceedMap() {
        return succeedMap;
    }

    public void setHoldingMenu(String holdingMenu) {
        this.holdingMenu = holdingMenu;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    public void setSucceedMap(List<ConditionMap> succeedMap) {
        this.succeedMap = succeedMap;
    }

    public void setFailedMap(List<ConditionMap> failedMap) {
        this.failedMap = failedMap;
    }
}
