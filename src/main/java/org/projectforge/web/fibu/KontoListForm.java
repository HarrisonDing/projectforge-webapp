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

package org.projectforge.web.fibu;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;
import org.projectforge.core.BaseSearchFilter;
import org.projectforge.web.wicket.AbstractListForm;


public class KontoListForm extends AbstractListForm<BaseSearchFilter, KontoListPage>
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(KontoListForm.class);

  private static final long serialVersionUID = -5969136444233092172L;

  @Override
  protected void init()
  {
    super.init();
    filterContainer.add(new CheckBox("deletedCheckBox", new PropertyModel<Boolean>(getSearchFilter(), "deleted")));
  }

  public KontoListForm(KontoListPage parentPage)
  {
    super(parentPage);
  }

  @Override
  protected BaseSearchFilter newSearchFilterInstance()
  {
    return new BaseSearchFilter();
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
