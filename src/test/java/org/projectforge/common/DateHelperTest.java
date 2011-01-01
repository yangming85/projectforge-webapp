/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2011 Kai Reinhard (k.reinhard@me.com)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

package org.projectforge.common;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.projectforge.core.Configuration;
import org.projectforge.test.TestBase;


public class DateHelperTest extends TestBase
{
  @Test
  public void testTimeZone() throws ParseException
  {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    df.setTimeZone(DateHelper.EUROPE_BERLIN);
    Date mezDate = df.parse("2008-03-14 17:25");
    long mezMillis = mezDate.getTime();
    df.setTimeZone(DateHelper.UTC);
    Date utcDate = df.parse("2008-03-14 16:25");
    long utcMillis = utcDate.getTime();
    assertEquals(mezMillis, utcMillis);
  }

  @Test
  public void formatIsoDate()
  {
    assertEquals("1970-11-21", DateHelper.formatIsoDate(createDate(1970, Calendar.NOVEMBER, 21, 16, 0, 0, 0)));
    assertEquals("1970-11-21", DateHelper.formatIsoDate(createDate(1970, Calendar.NOVEMBER, 21, 16, 35, 27, 968)));
  }

  @Test
  public void formatIsoTimestamp()
  {
    assertEquals("1970-11-21 17:00:00.0", DateHelper.formatIsoTimestamp(createDate(1970, Calendar.NOVEMBER, 21, 17, 0, 0, 0)));
    assertEquals("1970-11-21 17:05:07.123", DateHelper.formatIsoTimestamp(createDate(1970, Calendar.NOVEMBER, 21, 17, 5, 7, 123)));
  }

  @Test
  public void getDuration()
  {
    DateHolder dateHolder = new DateHolder(DatePrecision.MINUTE, Locale.GERMAN);
    dateHolder.setDate(1970, Calendar.NOVEMBER, 21, 4, 50, 0);
    Date startTime = dateHolder.getDate();
    dateHolder.setDate(1970, Calendar.NOVEMBER, 21, 6, 59, 0);
    Date stopTime = dateHolder.getDate();
    assertEquals(129, DateHelper.getDuration(startTime, stopTime));
    assertEquals(0, DateHelper.getDuration(stopTime, startTime));
    assertEquals(0, DateHelper.getDuration(null, stopTime));
    assertEquals(0, DateHelper.getDuration(startTime, null));
    assertEquals(0, DateHelper.getDuration(null, null));
    assertEquals(0, DateHelper.getDuration(startTime, startTime));
  }

  @Test
  public void formatMonth()
  {
    assertEquals("2009-01", DateHelper.formatMonth(2009, 0));
    assertEquals("2009-01", DateHelper.formatMonth(2009, Calendar.JANUARY));
    assertEquals("2009-03", DateHelper.formatMonth(2009, Calendar.MARCH));
    assertEquals("2009-09", DateHelper.formatMonth(2009, Calendar.SEPTEMBER));
    assertEquals("2009-10", DateHelper.formatMonth(2009, Calendar.OCTOBER));
    assertEquals("2009-12", DateHelper.formatMonth(2009, Calendar.DECEMBER));
  }

  private Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond)
  {
    Calendar cal = Calendar.getInstance(Configuration.getInstance().getDefaultTimeZone());
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, second);
    cal.set(Calendar.MILLISECOND, millisecond);
    return cal.getTime();
  }
}
