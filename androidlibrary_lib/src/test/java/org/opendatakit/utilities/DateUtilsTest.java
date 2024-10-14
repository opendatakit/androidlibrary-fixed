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

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.opendatakit.logging.WebLogger;
import org.opendatakit.logging.desktop.WebLoggerDesktopFactoryImpl;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DateUtilsTest {

  @BeforeClass
  public static void oneTimeSetUp() {
    StaticStateManipulator.get().reset();
    WebLogger.setFactory(new WebLoggerDesktopFactoryImpl());
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
  public void testNowInput() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);

    String value = util.validifyDateValue("now");
    assertNotNull(value);

    DateTime now = new DateTime();
    String expectedFormattedDate = util.formatDateTimeForDb(now);

    // This is to take the slight delay when checking for output into consideration
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void testTimeAddition() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);

    String value = util.validifyDateValue("now + 10m");
    assertNotNull(value);

    DateTime nowPlus10Minutes = new DateTime().plusMinutes(10);
    String expectedFormattedDate = util.formatDateTimeForDb(nowPlus10Minutes);
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void testTimeSubtraction() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);

    String value = util.validifyDateValue("now - 3h");
    assertNotNull(value);

    DateTime nowMinus3Hours = new DateTime().minusHours(3);
    String expectedFormattedDate = util.formatDateTimeForDb(nowMinus3Hours);
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void testInvalidTime() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);

    String value = util.validifyDateValue("now - 3y");
    assertNull(value);
  }

  @Test
  public void testInvalidTimeFormat() {
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils util = new DateUtils(Locale.US, tz);

    String value = util.validifyDateValue("invalid-date");
    assertNull(value);
  }

}
