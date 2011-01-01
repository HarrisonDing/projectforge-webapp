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

package org.projectforge.web.wicket.components;

import org.apache.wicket.Response;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.projectforge.web.wicket.PresizedImage;

/**
 * An image as link with an href and with a tooltip.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public abstract class ImageLinkPanel extends Panel
{
  private static final long serialVersionUID = 1333929048394636569L;

  private Link<String> link;

  @SuppressWarnings("serial")
  private ImageLinkPanel(final String id)
  {
    super(id);
    link = new Link<String>("link") {
      public void onClick()
      {
        ImageLinkPanel.this.onClick();
      };
    };
    add(link);
  }

  public ImageLinkPanel(final String id, final Response response, final String relativeImagePath)
  {
    this(id);
    link.add(new PresizedImage("image", getResponse(), relativeImagePath));
  }

  public ImageLinkPanel(final String id, final Response response, final String relativeImagePath, final String tooltip)
  {
    this(id);
    link.add(new TooltipImage("image", getResponse(), relativeImagePath, tooltip));
  }

  public ImageLinkPanel(final String id, final Response response, final String relativeImagePath, final IModel<String> tooltip)
  {
    this(id);
    link.add(new TooltipImage("image", getResponse(), relativeImagePath, tooltip));
  }

  public abstract void onClick();

}
