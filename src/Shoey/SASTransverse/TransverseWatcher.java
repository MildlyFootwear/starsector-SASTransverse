package Shoey.SASTransverse;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.abilities.FractureJumpAbility;

import static Shoey.SASTransverse.MainPlugin.logger;
import static Shoey.SASTransverse.MainPlugin.pFleet;

public class TransverseWatcher implements EveryFrameScript {

    boolean done = false;
    boolean show = true;
    public boolean ponder = false;
    public SectorEntityToken tt;

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        if ((!pFleet.isInHyperspace()) || (!Global.getSector().isPaused() && !pFleet.getAbility("fracture_jump").isInProgress())) {
            logger.info("Removing watcher");
            done = true;
        } else if (pFleet.getAbility("fracture_jump").isInProgress())
        {
            pFleet.goSlowOneFrame(true);
            if (show) {
                FractureJumpAbility pT = (FractureJumpAbility) pFleet.getAbility("fracture_jump");
                show = !transverseCheck(pT.getNearestWell(FractureJumpAbility.NASCENT_JUMP_DIST).getTarget());
            }
        }
        if (ponder)
        {
            if (!pFleet.isTransponderOn()) {
                pFleet.getAbility("transponder").pressButton();
            } else {
                ponder = false;
            }
        }
    }


    boolean transverseCheck(SectorEntityToken t)
    {
        tt = t;
        int wantTranspond = 0;
        int dontTranspond = 0;
        for (PlanetAPI planetAPI : t.getStarSystem().getPlanets())
        {

            if (planetAPI.getFaction() == null)
                continue;

            FactionAPI factionAPI = planetAPI.getFaction();

            if (factionAPI.isNeutralFaction() || factionAPI.isPlayerFaction())
                continue;

            if (factionAPI.getRelToPlayer().getRel() > -0.25f)
            {
                wantTranspond++;
            } else {
                dontTranspond++;
            }
        }

        if (wantTranspond > dontTranspond && !pFleet.getAbility("transponder").isActive())
        {
            return Global.getSector().getCampaignUI().showConfirmDialog(t.getStarSystem().getBaseName() + " has mostly non-hostile factions. Turn on transponder?", "Yes", "No", new Script() {
                @Override
                public void run() {
                    Global.getSector().getCampaignUI().addMessage("Jumping to " + tt.getName() + ".");
                    ponder = true;
                }
            }, new Script() {
                @Override
                public void run() {
                    Global.getSector().getCampaignUI().addMessage("Jumping to " + tt.getName() + ".");
                }
            });
        } else {
            Global.getSector().getCampaignUI().addMessage("Jumping to " + t.getName() + ".");
            return true;
        }
    }

}
