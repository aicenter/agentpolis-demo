/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import cz.cvut.fel.aic.agentpolis.demo.tripUtil.StartTargetNodePair;
import java.io.IOException;

/**
 *
 * @author fido
 */
class StartTargetNodePairDeserializer extends KeyDeserializer {

    public StartTargetNodePairDeserializer() {
    }

    @Override
    public Object deserializeKey(String string, DeserializationContext dc) throws IOException, JsonProcessingException {
        String[] parts = string.split("-");
        return new StartTargetNodePair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

}
