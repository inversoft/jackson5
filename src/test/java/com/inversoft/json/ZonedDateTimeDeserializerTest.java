/*
 * Copyright (c) 2026, Inversoft Inc., All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.inversoft.json;

import java.time.ZonedDateTime;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class ZonedDateTimeDeserializerTest {
  private ObjectMapper mapper;

  @BeforeMethod
  public void beforeMethod() {
    mapper = new ObjectMapper();
    mapper.registerModule(new JacksonModule());
    mapper.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());
  }

  @Test
  public void parse_error_boolean() throws JsonProcessingException {
    // Use case: A boolean (not string/int/float/long) value is passed, which is not valid

    // arrange

    // act
    try {
      mapper.readValue("true", ZonedDateTime.class);
      fail("Expected exception");
    } catch (JsonMappingException e) {
      assertEquals(e.getOriginalMessage(),
          "Unexpected token (VALUE_TRUE), expected VALUE_NUMBER_INT: Parseable to long");
    }
  }

  @Test
  public void parse_error_string() throws JsonProcessingException {
    // Use case: A full ISO date time value is passed, which is not valid

    // arrange

    // act
    try {
      mapper.readValue("\"1970-01-01T00:00:01.234Z\"", ZonedDateTime.class);
      fail("Expected exception");
    } catch (JsonMappingException e) {
      assertEquals(e.getOriginalMessage(),
          "Unexpected token (VALUE_STRING), expected VALUE_NUMBER_INT: Parseable to long");
    }
  }

  @Test
  public void parses_correctly_float() throws JsonProcessingException {
    // Use case: 1234.0 milliseconds past epoch, parses OK

    // arrange

    // act
    ZonedDateTime result = mapper.readValue("1234.0", ZonedDateTime.class);

    // assert
    assertEquals(result,
        ZonedDateTime.parse("1970-01-01T00:00:01.234Z"));
  }

  @Test
  public void parses_correctly_int() throws JsonProcessingException {
    // Use case: 1234 milliseconds past epoch, parses OK

    // arrange

    // act
    ZonedDateTime result = mapper.readValue("1234", ZonedDateTime.class);

    // assert
    assertEquals(result,
        ZonedDateTime.parse("1970-01-01T00:00:01.234Z"));
  }

  @Test
  public void parses_correctly_string() throws JsonProcessingException {
    // Use case: "1234" milliseconds past epoch, parses OK

    // arrange

    // act
    ZonedDateTime result = mapper.readValue("\"1234\"", ZonedDateTime.class);

    // assert
    assertEquals(result,
        ZonedDateTime.parse("1970-01-01T00:00:01.234Z"));
  }
}
