package com.aesophor.vigilante.component.character;

import com.aesophor.vigilante.map.Portal;
import com.badlogic.ashley.core.Component;

public class PortalTargetComponent implements Component {

    private Portal inRangePortal;

    public PortalTargetComponent() {

    }


    public Portal getInRangePortal() {
        return inRangePortal;
    }

    public void setInRangePortal(Portal inRangePortal) {
        this.inRangePortal = inRangePortal;
    }

}