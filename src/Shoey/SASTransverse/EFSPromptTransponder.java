package Shoey.SASTransverse;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;

public class EFSPromptTransponder implements EveryFrameScript {

    boolean done = false;
    String message = "Activate transponder?";

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
        done = Global.getSector().getCampaignUI().showConfirmDialog(message, "Yes", "No", new Script() {
            @Override
            public void run() {
                Global.getSector().addTransientScript(new EFSTransponder());
            }
        }, new Script() {
            @Override
            public void run() {
            }
        });
    }
}
