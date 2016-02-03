/*
 * Copyright (c) 2015-2016, Inversoft Inc., All Rights Reserved
 */
package org.primeframework.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZoneId;

/**
 * @author Seth Musselman
 */
public class ZoneIdSerializer extends JsonSerializer<ZoneId> {
  @Override
  public void serialize(ZoneId value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeObject(value.toString());
  }
}
