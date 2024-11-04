package Shoey.SASTransverse;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.NascentGravityWellAPI;
import com.fs.starfarer.api.impl.campaign.abilities.FractureJumpAbility;

public class TransverseFrameChecker implements EveryFrameScript {

    float messageTime = 0;
    float burnTime = 0;
    boolean oncePerStop = false;

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        CampaignFleetAPI pFleet = Global.getSector().getPlayerFleet();
        if (pFleet.isInHyperspace() && pFleet.hasAbility("fracture_jump"))
        {
            if (pFleet.getCurrBurnLevel() == 0)
            {
                messageTime += amount;
                burnTime = 0;
                CampaignUIAPI cUI = Global.getSector().getCampaignUI();
                if (!oncePerStop && messageTime > 0.5 && !cUI.isShowingDialog() && !cUI.isShowingMenu())
                {
                    messageTime = 0;
                    oncePerStop = true;
                    NascentGravityWellAPI ngw = ((FractureJumpAbility) pFleet.getAbility("fracture_jump")).getNearestWell(FractureJumpAbility.NASCENT_JUMP_DIST);
                    if (ngw != null) {
                        Global.getSector().getCampaignUI().addMessage("Stopped in well for " + ngw.getTarget().getName() + ".");
                    }
                }
            } else {
                burnTime += amount;
                if (burnTime > 1) {
                    oncePerStop = false;
                }
            }
        }

    }
}
