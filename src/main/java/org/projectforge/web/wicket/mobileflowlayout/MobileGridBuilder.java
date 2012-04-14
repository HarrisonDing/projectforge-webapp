/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2012 Kai Reinhard (k.reinhard@micromata.com)
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

package org.projectforge.web.wicket.mobileflowlayout;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.projectforge.web.wicket.flowlayout.AbstractGridBuilder;
import org.projectforge.web.wicket.flowlayout.DivPanel;

/**
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class MobileGridBuilder extends AbstractGridBuilder<MobileFieldsetPanel>
{
  private static final long serialVersionUID = 134863232462613937L;

  public MobileGridBuilder(final RepeatingView parent)
  {
    this.parentRepeatingView = parent;
  }

  public MobileGridBuilder(final DivPanel parent)
  {
    this.parentDivPanel = parent;
  }

  /**
   * @see org.projectforge.web.wicket.flowlayout.GridBuilderInterface#newFieldset(java.lang.String)
   */
  @Override
  public MobileFieldsetPanel newFieldset(final String label)
  {
    return null;// new MobileFieldsetPanel(current, label);
  }

  /**
   * @see org.projectforge.web.wicket.flowlayout.GridBuilderInterface#newFieldset(java.lang.String, boolean)
   */
  @Override
  public MobileFieldsetPanel newFieldset(final String label, final boolean multipleChildren)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.projectforge.web.wicket.flowlayout.GridBuilderInterface#newFieldset(java.lang.String, java.lang.String)
   */
  @Override
  public MobileFieldsetPanel newFieldset(final String labelText, final String labelDescription)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.projectforge.web.wicket.flowlayout.GridBuilderInterface#newFieldset(java.lang.String, java.lang.String, boolean)
   */
  @Override
  public MobileFieldsetPanel newFieldset(final String labelText, final String labelDescription, final boolean multipleChildren)
  {
    throw new UnsupportedOperationException();
  }
}
