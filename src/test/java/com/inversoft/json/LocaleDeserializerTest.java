/*
 * Copyright (c) 2021-2023, Inversoft Inc., All Rights Reserved
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class LocaleDeserializerTest {
  ObjectMapper mapper = new ObjectMapper();

  @Test(dataProvider = "localeData")
  public void allLocalesFromBCP47Strings(Locale locale) throws Exception {
    Locale testLocaleObjectOutFromString = mapper.readValue("\"" + locale.toLanguageTag() + "\"", Locale.class);
    assertEquals(locale.toLanguageTag(), testLocaleObjectOutFromString.toLanguageTag());
    testAllDataStructures(testLocaleObjectOutFromString);
  }

  @Test(dataProvider = "localeData")
  public void allLocalesFromStrings(Locale locale) throws Exception {
    Locale testLocaleObjectOutFromString = mapper.readValue("\"" + locale.toString() + "\"", Locale.class);
    assertEquals(locale.toString(), testLocaleObjectOutFromString.toString());
    testAllDataStructures(testLocaleObjectOutFromString);
  }

  @DataProvider(name = "localeData")
  public Object[] localeData() {
    List<Locale> locales = new ArrayList<>();
    Collections.addAll(locales, Locale.getAvailableLocales());
    locales.removeIf((locale) -> locale.getLanguage().isEmpty() || locale.hasExtensions() || !locale.getScript().isEmpty());

    return locales.toArray();
  }

  @Test
  public void too_big() throws Exception {
    // This is just to prove that the Locale validation by Jackson, and by everyone sucks.
    // - This means you have to do additional validation elsewhere.

    // Create a 2MB string
    int length = 2 * 1024 * 1024;
    char[] array = new char[length];
    Arrays.fill(array, 'x');
    String bigString = new String(array);

    Locale actual = mapper.readValue("\"" + bigString + "\"", Locale.class);
    assertEquals(length, actual.toString().getBytes(StandardCharsets.UTF_8).length);
  }

  private void testAllDataStructures(Locale testLocaleObjectOutFromString) throws Exception {
    TestLocaleObject test1 = new TestLocaleObject();
    test1.locale = testLocaleObjectOutFromString;
    test1.localeArray = new Locale[]{testLocaleObjectOutFromString, Locale.CANADA_FRENCH};

    Map<Locale, String> testMap = new HashMap<>();
    testMap.put(testLocaleObjectOutFromString, "value1");
    testMap.put(Locale.CHINESE, "value2");
    test1.localeMap = testMap;

    Map<Locale, Object> testObjectMap = new HashMap<>();
    testObjectMap.put(Locale.GERMAN, testLocaleObjectOutFromString);
    testObjectMap.put(Locale.CHINESE, Locale.US);
    test1.localeObjectMap = testObjectMap;

    Map<String, Locale> testObjectMap2 = new HashMap<>();
    testObjectMap2.put("map1", testLocaleObjectOutFromString);
    testObjectMap2.put("map2", Locale.SIMPLIFIED_CHINESE);
    Map<Locale, Map<String, Locale>> testMapMap = new HashMap<>();
    testMapMap.put(testLocaleObjectOutFromString, testObjectMap2);
    testMapMap.put(Locale.CHINESE, testObjectMap2);
    test1.localeMapMap = testMapMap;

    Locale[] localeArray = new Locale[]{testLocaleObjectOutFromString, Locale.CANADA_FRENCH};
    Map<Locale, Locale[]> localeArrayMap = new HashMap<>();
    localeArrayMap.put(Locale.FRANCE, localeArray);
    localeArrayMap.put(testLocaleObjectOutFromString, localeArray);
    test1.localeArrayMap = localeArrayMap;

    String testObjectString = mapper.writeValueAsString(test1);

    TestLocaleObject testLocaleObjectOut = mapper.readValue(testObjectString, TestLocaleObject.class);

    assertEquals(testLocaleObjectOutFromString, testLocaleObjectOut.locale);
    assertEquals(mapper.writeValueAsString(new Locale[]{testLocaleObjectOutFromString, Locale.CANADA_FRENCH}), mapper.writeValueAsString(testLocaleObjectOut.localeArray));
    assertEquals(mapper.writeValueAsString(testMap), mapper.writeValueAsString(testLocaleObjectOut.localeMap));
    assertEquals(mapper.writeValueAsString(testObjectMap), mapper.writeValueAsString(testLocaleObjectOut.localeObjectMap));
    assertEquals(mapper.writeValueAsString(testMapMap), mapper.writeValueAsString(testLocaleObjectOut.localeMapMap));
    assertEquals(mapper.writeValueAsString(localeArrayMap), mapper.writeValueAsString(testLocaleObjectOut.localeArrayMap));
  }

  private static class TestLocaleObject {
    // Cover all different JSON formats.

    // "locale": "en_US"
    public Locale locale;

    // "localeArray": ["en_US"]
    public Locale[] localeArray;

    // "localeArrayMap": {"en_LR": ["en_LR","fr_CA"],"fr_FR": ["en_LR","fr_CA"]}
    public Map<Locale, Locale[]> localeArrayMap;

    // "localMap": {"en_US": "value"}
    public Map<Locale, String> localeMap;

    // "localeMapMap": {"en_US": {"map2": "zh_CN","map1": "en_LR"},"zh":{"map2":"zh_CN","map1":"en_LR"}}
    public Map<Locale, Map<String, Locale>> localeMapMap;

    // "localObjectMap": {"en_US": {}}
    public Map<Locale, Object> localeObjectMap;

    @JacksonConstructor
    public TestLocaleObject() {
    }
  }
}
