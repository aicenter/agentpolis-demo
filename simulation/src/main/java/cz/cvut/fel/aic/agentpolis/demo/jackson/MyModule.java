/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.cvut.fel.aic.agentpolis.demo.tripUtil.StartTargetNodePair;

/**
 *
 * @author fido
 */
public class MyModule extends SimpleModule {

    public MyModule() {
        addKeyDeserializer(StartTargetNodePair.class, new StartTargetNodePairDeserializer());
        addKeySerializer(StartTargetNodePair.class, new StartTargetNodePairSerializer());
    }

}
