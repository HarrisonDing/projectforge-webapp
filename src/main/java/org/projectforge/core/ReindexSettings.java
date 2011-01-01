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

package org.projectforge.core;

import java.util.Date;

import org.projectforge.common.ReflectionToString;


public class ReindexSettings
{
  private Date fromDate;

  private Integer lastNEntries;

  public ReindexSettings()
  {
  }

  public ReindexSettings(final Date fromDate, final Integer lastNEntries)
  {
    this.fromDate = fromDate;
    this.lastNEntries = lastNEntries;
  }
  
  public Date getFromDate()
  {
    return fromDate;
  }
  
  public Integer getLastNEntries()
  {
    return lastNEntries;
  }
  
  @Override
  public String toString()
  {
    final ReflectionToString tos = new ReflectionToString(this);
    return tos.toString();
  }
}
