/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.tripUtil;


/**
 *
 * @author fido
 */
public class StartTargetNodePair {

    private final int startNodeId;

    private final int targetNodeId;

    public int getStartNodeId() {
        return startNodeId;
    }

    public int getTargetNodeId() {
        return targetNodeId;
    }

    public StartTargetNodePair(int startNodeId, int targetNodeId) {
        this.startNodeId = startNodeId;
        this.targetNodeId = targetNodeId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.startNodeId;
        hash = 59 * hash + this.targetNodeId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StartTargetNodePair other = (StartTargetNodePair) obj;
        if (this.startNodeId != other.startNodeId) {
            return false;
        }
        if (this.targetNodeId != other.targetNodeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return startNodeId + "-" + targetNodeId;
    }

}
