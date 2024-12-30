package Shoey.SASTransverse;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.abilities.FractureJumpAbility;

import static Shoey.SASTransverse.MainPlugin.logger;
import static Shoey.SASTransverse.MainPlugin.pFleet;

public class TransverseWatcher implements EveryFrameScript {

    boolean done = false;
    boolean show = true;

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
    }


    public static boolean transverseCheck(SectorEntityToken t)
    {
        int wantTranspond = 0;
        int dontTranspond = 0;
        for (SectorEntityToken e: t.getStarSystem().getAllEntities())
        {

            if (e.getFaction() == null || e.getMarket() == null)
                continue;

            FactionAPI factionAPI = e.getFaction();

            if (factionAPI.isNeutralFaction() || factionAPI.isPlayerFaction())
                continue;

            if (!factionAPI.getRelToPlayer().isHostile())
            {
                wantTranspond++;
            } else {
                dontTranspond++;
            }
        }

        if (wantTranspond > dontTranspond && !pFleet.getAbility("transponder").isActive())
        {
            return Global.getSector().getCampaignUI().showConfirmDialog("Destination has mostly non-hostile factions. Turn on transponder?", "Yes", "No", new Script() {
                @Override
                public void run() {
                    Global.getSector().addTransientScript(new EFSTransponder());
                }
            }, new Script() {
                @Override
                public void run() {
                }
            });
        } else {
            return true;
        }
    }

}
