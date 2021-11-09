/*
 * Copyright (c) 2015-2016, Inversoft Inc., All Rights Reserved
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Prime's Jackson module. Binds the Locale serializer/deserializer.
 *
 * @author Brian Pontarelli
 */
public class JacksonModule extends SimpleModule {
  private static final Version VERSION = VersionUtil.parseVersion("0.22", "org.primeframework.mvc", "prime-jackson-module");

  public JacksonModule() {
    super(VERSION);

    // Deserializers
    addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
    addDeserializer(LocalDate.class, new LocalDateDeserializer());
    addDeserializer(ZoneId.class, new ZoneIdDeserializer());

    // Serializers
    addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
    addSerializer(LocalDate.class, new LocalDateSerializer());
    addSerializer(ZoneId.class, new ZoneIdSerializer());
  }
}
