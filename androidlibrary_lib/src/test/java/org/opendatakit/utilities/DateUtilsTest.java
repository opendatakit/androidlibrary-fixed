/*
 * Copyright (C) 2015 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.utilities;

import static org.junit.Assert.assertEquals;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.opendatakit.logging.WebLogger;
import org.opendatakit.logging.desktop.WebLoggerDesktopFactoryImpl;


import java.util.Locale;
import java.util.TimeZone;

@RunWith(JUnit4.class)
public class DateUtilsTest {

  private DateUtils dateUtils;
  @BeforeClass
  public static void oneTimeSetUp() {
    StaticStateManipulator.get().reset();
    WebLogger.setFactory(new WebLoggerDesktopFactoryImpl());
  }
  @Before
  public void setUp() {
    TimeZone timeZone = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    dateUtils = new DateUtils(Locale.US, timeZone);
  }

  @Test
  public void testDateInterpretation() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);
    
    String value = util.validifyDateValue("3/4/2015");
    
    String expected = "2015-03-04T";
    assertEquals(expected, value.substring(0,expected.length()));
  }

  @Test
  public void testValidInstantInput(){
    String input = "3/4/2015";
    String expectedDateTime = "2015-03-04T00:00:00.000000000";
    assertEquals(expectedDateTime, dateUtils.validifyDateValue(input));
  }

  @Test
  public void testValidIntervalInput(){
    String input = "today";  // Supported input format
    DateTime expectedStart = new DateTime().withTimeAtStartOfDay();
    String expectedOutput = dateUtils.formatDateTimeForDb(expectedStart);
    assertEquals(expectedOutput, dateUtils.validifyDateValue(input));
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    String input = null;
    dateUtils.validifyDateValue(input);
  }


}
