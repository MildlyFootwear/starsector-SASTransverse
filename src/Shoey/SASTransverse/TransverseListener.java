package Shoey.SASTransverse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.impl.campaign.abilities.FractureJumpAbility;
import org.apache.log4j.Logger;

import java.util.Objects;

import static Shoey.SASTransverse.MainPlugin.pFleet;

public class TransverseListener extends CampaignListenerTemplate {


    private static final Logger log = Logger.getLogger(TransverseListener.class);

    @Override
    public void reportPlayerActivatedAbility(AbilityPlugin ability, Object param) {
        if (ability.getId() != null) {
            if (ability.getId().equals("fracture_jump")) {
                pFleet = Global.getSector().getPlayerFleet();
                if (pFleet.isInHyperspace() && !Global.getSector().hasTransientScript(TransverseWatcher.class)) {
                    log.info("added watcher");
                    Global.getSector().addTransientScript(new TransverseWatcher());
                }
            }
        }
    }
}
