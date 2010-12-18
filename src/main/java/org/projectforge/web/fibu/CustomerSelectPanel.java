/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2010 Kai Reinhard (k.reinhard@me.com)
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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.projectforge.common.NumberHelper;
import org.projectforge.fibu.KundeDO;
import org.projectforge.fibu.KundeFavorite;
import org.projectforge.fibu.KundeFormatter;
import org.projectforge.user.UserPrefArea;
import org.projectforge.web.wicket.AbstractForm;
import org.projectforge.web.wicket.AbstractSelectPanel;
import org.projectforge.web.wicket.WebConstants;
import org.projectforge.web.wicket.components.FavoritesChoicePanel;
import org.projectforge.web.wicket.components.MaxLengthTextField;
import org.projectforge.web.wicket.components.TooltipImage;


/**
 * This panel show the actual kunde and buttons for select/unselect kunde.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class CustomerSelectPanel extends AbstractSelectPanel<KundeDO>
{
  private static final long serialVersionUID = 5452693296383142460L;

  @SpringBean(name = "kundeFormatter")
  private KundeFormatter kundeFormatter;

  private PropertyModel<String> kundeText;

  private TextField<String> kundeTextField;

  /**
   * @param id
   * @param model
   * @param kundeText If no Kunde is given then a free text field representing a Kunde can be used.
   * @param caller
   * @param selectProperty
   */
  public CustomerSelectPanel(final String id, final IModel<KundeDO> model, final PropertyModel<String> kundeText,
      final ISelectCallerPage caller, final String selectProperty)
  {
    super(id, model, caller, selectProperty);
    this.kundeText = kundeText;
  }

  @SuppressWarnings("serial")
  public CustomerSelectPanel init()
  {
    super.init();
    if (kundeText != null) {
      kundeTextField = new MaxLengthTextField("kundeText", kundeText) {
        @Override
        public boolean isVisible()
        {
          return (CustomerSelectPanel.this.getModelObject() == null || NumberHelper
              .greaterZero(CustomerSelectPanel.this.getModelObject().getId()) == false);
        }
      };
      add(kundeTextField);
    } else {
      add(AbstractForm.createInvisibleDummyComponent("kundeText"));
    }
    final Label kundeAsStringLabel = new Label("kundeAsString", new Model<String>() {

      @Override
      public String getObject()
      {
        final KundeDO kunde = getModelObject();
        return kundeFormatter.format(kunde, false);
      }
    });
    add(kundeAsStringLabel);
    final SubmitLink selectButton = new SubmitLink("select") {
      public void onSubmit()
      {
        setResponsePage(new CustomerListPage(caller, selectProperty));
      };
    };
    selectButton.setDefaultFormProcessing(false);
    add(selectButton);
    selectButton.add(new TooltipImage("selectHelp", getResponse(), WebConstants.IMAGE_KUNDE_SELECT, getString("fibu.tooltip.selectKunde")));
    final SubmitLink unselectButton = new SubmitLink("unselect") {
      @Override
      public void onSubmit()
      {
        caller.unselect(selectProperty);
      }

      @Override
      public boolean isVisible()
      {
        return CustomerSelectPanel.this.getModelObject() != null;
      }
    };
    unselectButton.setDefaultFormProcessing(false);
    add(unselectButton);
    unselectButton.add(new TooltipImage("unselectHelp", getResponse(), WebConstants.IMAGE_KUNDE_UNSELECT,
        getString("fibu.tooltip.unselectKunde")));
    // DropDownChoice favorites
    final FavoritesChoicePanel<KundeDO, KundeFavorite> favoritesPanel = new FavoritesChoicePanel<KundeDO, KundeFavorite>("favorites",
        UserPrefArea.KUNDE_FAVORITE, tabIndex, "half select") {
      @Override
      protected void select(final KundeFavorite favorite)
      {
        if (favorite.getKunde() != null) {
          CustomerSelectPanel.this.selectKunde(favorite.getKunde());
        }
      }

      @Override
      protected KundeDO getCurrentObject()
      {
        return CustomerSelectPanel.this.getModelObject();
      }

      @Override
      protected KundeFavorite newFavoriteInstance(final KundeDO currentObject)
      {
        final KundeFavorite favorite = new KundeFavorite();
        favorite.setKunde(currentObject);
        return favorite;
      }
    };
    add(favoritesPanel);
    favoritesPanel.init();
    if (showFavorites == false) {
      favoritesPanel.setVisible(false);
    }
    return this;
  }

  /**
   * Will be called if the user has chosen an entry of the kunde favorites drop down choice.
   * @param kunde
   */
  protected void selectKunde(final KundeDO kunde)
  {
    setModelObject(kunde);
    caller.select(selectProperty, kunde.getId());
  }

  /**
   * @return The user's raw input of kunde text if given, otherwise null.
   */
  public String getKundeTextInput()
  {
    if (kundeTextField != null) {
      return kundeTextField.getRawInput();
    }
    return null;
  }

  @Override
  protected void convertInput()
  {
    setConvertedInput(getModelObject());
  }
}
