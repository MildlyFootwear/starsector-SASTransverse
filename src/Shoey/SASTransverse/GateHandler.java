package Shoey.SASTransverse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.GateTransitListener;

import static Shoey.SASTransverse.MainPlugin.logger;
import static Shoey.SASTransverse.MainPlugin.pFleet;

public class GateHandler implements GateTransitListener {

    public void transponderCheck(SectorEntityToken t)
    {
        int wantTranspond = 0;
        int dontTranspond = 0;
        for (SectorEntityToken e: t.getStarSystem().getAllEntities())
        {

            if (e.getFaction() == null || e.getMarket() == null)
                continue;

            FactionAPI factionAPI = e.getMarket().getFaction();

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
            Global.getSector().addTransientScript(new EFSPromptTransponder());
        }
    }

    @Override
    public void reportFleetTransitingGate(CampaignFleetAPI fleet, SectorEntityToken gateFrom, SectorEntityToken gateTo) {
        if (fleet.isPlayerFleet() && gateTo != null)
        {
            pFleet = fleet;
            logger.info("Player jumping.");
            transponderCheck(gateTo);
        }
    }
}
