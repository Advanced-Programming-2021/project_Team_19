package model;

import controller.DuelControllers.Actions.Action;

public class TriggerLabel {

    public boolean shouldAskFromFirstGamer = true;
    public boolean shouldAskFromSecondGamer = true;
    public boolean inProgress = false;
    public Action action;

    public TriggerLabel(Action action){
        this.action = action;
    }
}
