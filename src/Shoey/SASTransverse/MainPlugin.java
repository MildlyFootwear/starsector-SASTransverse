package Shoey.SASTransverse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import org.apache.log4j.Logger;

public class MainPlugin extends BaseModPlugin {

    public static CampaignFleetAPI pFleet;
    public static Logger logger = Global.getLogger(MainPlugin.class);

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        Global.getSector().addListener(new TransverseListener());
    }
}
