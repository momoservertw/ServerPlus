package tw.momocraft.serverplus.utils.event;

import java.util.List;
import java.util.Map;

public class EventMap {

    private long priority;

    private List<String> events;
    private Map<String, List<String>> targets;
    private ConditionMap conditions;
    private ActionMap actions;
    private ActionMap actionsFailed;

    public long getPriority() {
        return priority;
    }

    public List<String> getEvents() {
        return events;
    }

    public Map<String, List<String>> getTargets() {
        return targets;
    }

    public ConditionMap getConditions() {
        return conditions;
    }

    public ActionMap getActions() {
        return actions;
    }

    public ActionMap getActionsFailed() {
        return actionsFailed;
    }


    public void setPriority(long priority) {
        this.priority = priority;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public void setTargets(Map<String, List<String>> targets) {
        this.targets = targets;
    }

    public void setConditions(ConditionMap conditions) {
        this.conditions = conditions;
    }

    public void setActions(ActionMap actions) {
        this.actions = actions;
    }

    public void setActionsFailed(ActionMap actionsFailed) {
        this.actionsFailed = actionsFailed;
    }
}
