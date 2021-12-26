/*
 * Copyright (c) 2021, Inversoft Inc., All Rights Reserved
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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import com.inversoft.json.ToString;

public class ToStringTest {
  @Test
  public void toJSONStringTest() {
    Map<String, Object> test = new HashMap<>();
    List<String> list = new ArrayList<>(Arrays.asList("bar", "baz"));
    test.put("foo", list);

    String output = ToString.toJSONString(test);
    Assert.assertFalse(output.contains("\n"));

    // ensure configuration state is not affected buy calling toString
    ToString.toString(test);
    String output2 = ToString.toJSONString(test);
    Assert.assertEquals(output2, output);
    Assert.assertFalse(output2.contains("\n"));
  }

  @Test
  public void toStringTest() {
    Map<String, Object> test = new HashMap<>();
    List<String> list = new ArrayList<>(Arrays.asList("bar", "baz"));
    test.put("foo", list);

    String output = ToString.toString(test);
    Assert.assertTrue(output.contains("\n"));

    // ensure configuration state is not affected buy calling toJSONString
    ToString.toJSONString(test);
    String output2 = ToString.toString(test);
    Assert.assertEquals(output2, output);
    Assert.assertTrue(output2.contains("\n"));
  }
}