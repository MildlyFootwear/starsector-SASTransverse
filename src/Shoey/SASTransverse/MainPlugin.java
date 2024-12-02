package Shoey.SASTransverse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import org.apache.log4j.Logger;

public class MainPlugin extends BaseModPlugin {

    public static CampaignFleetAPI pFleet;
    public static Logger logger = Global.getLogger(MainPlugin.class);

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        for (CampaignEventListener l : Global.getSector().getAllListeners())
        {
            if (l.getClass() == TransverseListener.class)
            {
                Global.getSector().removeListener(l);
                logger.info("Removed unneeded listener "+l);
            }
        }
        Global.getSector().getListenerManager().addListener(new GateHandler(), true);
        Global.getSector().addTransientListener(new TransverseListener());
        Global.getSector().addTransientScript(new TransverseFrameChecker());
    }
}
