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

package org.projectforge.web.orga;

import org.projectforge.orga.ContractFilter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ContractFilter")
public class ContractListFilter extends ContractFilter
{
  private static final long serialVersionUID = -7381514878697257874L;

  protected int year;

  @Override
  public void reset()
  {
    super.reset();
    this.searchString = "";
  }

  /**
   * Year of contracts to filter. "<= 0" means showing all years.
   * @return
   */
  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }
}
