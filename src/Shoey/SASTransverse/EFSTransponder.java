package Shoey.SASTransverse;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

public class EFSTransponder implements EveryFrameScript {
    boolean done = false;

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        Global.getSector().getPlayerFleet().getAbility("transponder").pressButton();
        done = Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
