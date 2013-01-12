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

package org.projectforge.web.wicket.flowlayout;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.projectforge.web.wicket.WicketUtils;

/**
 * Represents an icon. Supports Ajax onclick behavior if {@link #enableAjaxOnClick()} is called.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class IconPanel extends Panel
{
  private static final long serialVersionUID = 3317775585548133768L;

  private final WebMarkupContainer div;

  public IconPanel(final String id, final IconType type)
  {
    this(id, type, (String) null);
  }

  public IconPanel(final String id, final IconType type, final String tooltip)
  {
    super(id);
    div = new WebMarkupContainer("div");
    add(div);
    appendAttribute("class", type.getClassAttrValue());
    if (tooltip != null) {
      WicketUtils.addTooltip(div, tooltip);
    }
  }

  public IconPanel(final String id, final IconType type, final IModel<String> tooltip)
  {
    super(id);
    div = new WebMarkupContainer("div");
    add(div);
    appendAttribute("class", type.getClassAttrValue());
    if (tooltip != null) {
      WicketUtils.addTooltip(div, tooltip);
    }
  }

  /**
   * Enable Ajax onclick event. If clicked by the user {@link #onClick()} is called.
   */
  @SuppressWarnings("serial")
  public IconPanel enableAjaxOnClick()
  {
    appendAttribute("style", "cursor: pointer;");
    final AjaxEventBehavior behavior = new AjaxEventBehavior("onClick") {
      @Override
      protected void onEvent(final AjaxRequestTarget target)
      {
        IconPanel.this.onClick();
      }
    };
    div.add(behavior);
    return this;
  }

  /**
   * Appends attribute onclick and changes the cursor to pointer.
   * @return
   */
  public IconPanel setOnClick(final String onclick)
  {
    appendAttribute("style", "cursor: pointer;");
    appendAttribute("onclick", onclick);
    return this;
  }

  /**
   * Appends attribute onclick and changes the cursor to pointer. onclick results in location.href.
   * @param location url to go on click.
   * @param newWindow If true then a new browser with the given url is opened.
   * @return
   */
  public IconPanel setOnClickLocation(final String location, final boolean newWindow)
  {
    appendAttribute("style", "cursor: pointer;");
    if (newWindow == true) {
      appendAttribute("onclick", "window.open('" + location + "'); return false;");
    } else {
      appendAttribute("onclick", "location.href='" + location + "';");
    }
    return this;
  }

  /**
   * @see org.apache.wicket.Component#setMarkupId(java.lang.String)
   */
  @Override
  public Component setMarkupId(final String markupId)
  {
    div.setOutputMarkupId(true);
    return div.setMarkupId(markupId);
  }

  /**
   * @return the div
   */
  public WebMarkupContainer getDiv()
  {
    return div;
  }

  /**
   * 
   * @param attributeName
   * @param value
   * @return this for chaining.
   * @see AttributeModifier#append(String, java.io.Serializable)
   */
  public IconPanel appendAttribute(final String attributeName, final Serializable value)
  {
    div.add(AttributeModifier.append(attributeName, value));
    return this;
  }

  /**
   * Don't forget to call {@link #enableAjaxOnClick()}.
   */
  public void onClick()
  {
  }
}
