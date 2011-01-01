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

package org.projectforge.web.wicket;

public abstract class AbstractSecuredForm<F, P extends AbstractSecuredBasePage> extends AbstractForm<F, P>
{
  private static final long serialVersionUID = 5034574268522349613L;

  public AbstractSecuredForm(P parentPage)
  {
    super(parentPage);
  }

  /**
   * @param key
   * @see AbstractSecuredPage#getUserPrefEntry(String)
   */
  public Object getUserPrefEntry(final String key)
  {
    return parentPage.getUserPrefEntry(key);
  }

  /**
   * @param expectedType
   * @param key
   * @see AbstractSecuredPage#getUserPrefEntry(Class, String)
   */
  public Object getUserPrefEntry(final Class< ? > expectedType, final String key)
  {
    return parentPage.getUserPrefEntry(expectedType, key);
  }

  /**
   * @param key
   * @param value
   * @param persistent
   * @see AbstractSecuredPage#putUserPrefEntry(String, Object, boolean)
   */
  public void putUserPrefEntry(String key, Object value, boolean persistent)
  {
    parentPage.putUserPrefEntry(key, value, persistent);
  }
}
