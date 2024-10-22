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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DateUtilsTest {
  private static final Locale NIGERIA_LOCALE = new Locale("en", "NG");
  private static final Locale CAMEROON_LOCALE = new Locale("fr", "CM");

  private static final TimeZone NIGERIA_TIME_ZONE = TimeZone.getTimeZone("Africa/Lagos");
  private static final TimeZone CAMEROON_TIME_ZONE = TimeZone.getTimeZone("Africa/Douala");
  private static final TimeZone TORONTO_TIME_ZONE = TimeZone.getTimeZone("America/Toronto");
  private static final TimeZone LONDON_TIME_ZONE = TimeZone.getTimeZone("Europe/London");
  private static final TimeZone ITALY_TIME_ZONE = TimeZone.getTimeZone("Europe/Rome");

  @BeforeClass
  public static void oneTimeSetUp() {
    StaticStateManipulator.get().reset();
  }

  @Test
  public void validifyDateValue_withDateInput_returnsFormattedDate() {
    // Set up TimeZone and DateUtils
    TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]);
    DateUtils dateUtil = new DateUtils(Locale.US, tz);
    String value = dateUtil.validifyDateValue("3/4/2015");

    String expected = "2015-03-04T";
    assertEquals(expected, value.substring(0,expected.length()));
  }

  @Test
  public void validifyDateValue_withNowInput_returnsFormattedCurrentDate() {
    DateUtils dateUtil = new DateUtils(CAMEROON_LOCALE, CAMEROON_TIME_ZONE);
    String value = dateUtil.validifyDateValue("now");
    assertNotNull(value);

    DateTime now = new DateTime();
    String expectedFormattedDate = getTimeString(now);

    // This is to take the slight delay when checking for output into consideration
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void validifyDateValue_withfutureTimeInput_returnsTimeInTheFuture() {
    DateUtils dateUtil = new DateUtils(Locale.CANADA, TORONTO_TIME_ZONE);
    String value = dateUtil.validifyDateValue("now + 10m");
    assertNotNull(value);

    DateTime nowPlus10Minutes = new DateTime().plusMinutes(10);
    String expectedFormattedDate = getTimeString(nowPlus10Minutes);
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void validifyDateValue_withPastTimeInput_returnsTimeInThePast() {
    DateUtils dateUtil = new DateUtils(NIGERIA_LOCALE, NIGERIA_TIME_ZONE);
    String value = dateUtil.validifyDateValue("now - 3h");
    assertNotNull(value);

    DateTime nowMinus3Hours = new DateTime().minusHours(3);
    String expectedFormattedDate = getTimeString(nowMinus3Hours);
    int periodIndex = expectedFormattedDate.indexOf('.');
    assertTrue(value.startsWith(expectedFormattedDate.substring(0, periodIndex + 1)));
  }

  @Test
  public void validifyDateValue_withUnsupportedUnitInput_returnsNull() {
    DateUtils dateUtil = new DateUtils(Locale.UK, LONDON_TIME_ZONE);
    String value = dateUtil.validifyDateValue("now - 3y");
    assertNull(value);
  }

  @Test
  public void validifyDateValue_withInvalidTimeFormat_returnsNull() {
    DateUtils dateUtil = new DateUtils(Locale.ITALY, ITALY_TIME_ZONE);
    String value = dateUtil.validifyDateValue("invalid-date");
    assertNull(value);
  }

  private String getTimeString(DateTime time){
    // convert to a nanosecond-extended iso8601-style UTC date yyyy-mm-ddTHH:MM:SS.sssssssss
    String partialPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    Calendar calendar = GregorianCalendar.getInstance(new SimpleTimeZone(0, "UT"));

    SimpleDateFormat fmt = new SimpleDateFormat(partialPattern, Locale.ROOT);
    fmt.setCalendar(calendar);
    Date d = new Date(time.getMillis());
    return fmt.format(d) + "000000";
  }
  @AfterClass
  public static void oneTimeTearDown() {
    StaticStateManipulator.get().reset();
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
