/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2013 Kai Reinhard (k.reinhard@micromata.de)
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

package org.projectforge.xstream;

import java.util.TimeZone;

import junit.framework.Assert;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.projectforge.common.DateHelper;
import org.projectforge.user.PFUserContext;
import org.projectforge.user.PFUserDO;

public class JodaDateMidnightConverterTest
{
  @Test
  public void testConverter()
  {
    test(DateHelper.EUROPE_BERLIN);
    test(DateHelper.UTC);
  }

  private void test(final TimeZone timeZone) {
    final PFUserDO user = new PFUserDO();
    user.setTimeZone(timeZone);
    PFUserContext.setUser(user);
    final JodaDateMidnightConverter converter = new JodaDateMidnightConverter();
    final DateMidnight dateMidnight = (DateMidnight) converter.parse("1970-11-21");
    Assert.assertEquals(1970, dateMidnight.getYear());
    Assert.assertEquals(11, dateMidnight.getMonthOfYear());
    Assert.assertEquals(21, dateMidnight.getDayOfMonth());
    Assert.assertEquals("1970-11-21", converter.toString(dateMidnight));
  }
}
