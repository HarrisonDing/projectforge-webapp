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

package org.projectforge.web.core;

import org.projectforge.core.SearchFilter;
import org.projectforge.xml.stream.XmlObject;

@XmlObject(alias = "searchPageFilter")
public class SearchPageFilter extends SearchFilter
{
  private static final long serialVersionUID = 2056162179686892853L;

  private Integer lastDays;

  private String area;

  public Integer getLastDays()
  {
    return lastDays;
  }

  public void setLastDays(Integer lastDays)
  {
    this.lastDays = lastDays;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }
  
  public void setMaxRows(Integer maxRows)
  {
    super.setMaxRows(maxRows != null ? maxRows : 3);
  }
}
