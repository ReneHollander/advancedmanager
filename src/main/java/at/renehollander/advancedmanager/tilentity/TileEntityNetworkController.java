package at.renehollander.advancedmanager.tilentity;

import at.renehollander.advancedmanager.grid.impl.TileEntityNode;

public class TileEntityNetworkController extends TileEntityNode {

    @Override
    public void destroy() {
        if (this.getConnectedGrid() != null) {
            this.getConnectedGrid().removeNode(this);
        }
    }

}