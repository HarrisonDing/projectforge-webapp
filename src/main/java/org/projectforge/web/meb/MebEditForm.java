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

package org.projectforge.web.meb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.projectforge.core.Configuration;
import org.projectforge.jira.JiraConfig;
import org.projectforge.jira.JiraIssueType;
import org.projectforge.jira.JiraProject;
import org.projectforge.meb.MebEntryDO;
import org.projectforge.meb.MebEntryStatus;
import org.projectforge.orga.PostType;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserGroupCache;
import org.projectforge.user.UserPrefArea;
import org.projectforge.web.URLHelper;
import org.projectforge.web.calendar.DateTimeFormatter;
import org.projectforge.web.user.UserSelectPanel;
import org.projectforge.web.wicket.AbstractEditForm;
import org.projectforge.web.wicket.components.FavoritesChoicePanel;
import org.projectforge.web.wicket.components.LabelValueChoiceRenderer;
import org.projectforge.web.wicket.components.MaxLengthTextArea;
import org.projectforge.web.wicket.components.SingleButtonPanel;

public class MebEditForm extends AbstractEditForm<MebEntryDO, MebEditPage>
{
  private static final long serialVersionUID = -1447905028243511191L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MebEditForm.class);

  private static final String USER_PREF_KEY_JIRA_PROJECT = "meb.edit.recentJiraProject";

  private static final String USER_PREF_KEY_JIRA_ISSUE_TYPE = "meb.edit.recentJiraIssueType";

  @SpringBean(name = "userGroupCache")
  private UserGroupCache userGroupCache;

  private Integer jiraIssueType;

  private FavoritesChoicePanel<JiraProject, JiraProject> jiraProjectChoice;

  private JiraConfig jiraConfig = Configuration.getInstance().getJiraConfig();

  public MebEditForm(MebEditPage parentPage, MebEntryDO data)
  {
    super(parentPage, data);
    this.colspan = 2;
  }

  @SuppressWarnings("serial")
  @Override
  protected void init()
  {
    super.init();
    add(new Label("date", DateTimeFormatter.instance().getFormattedDateTime(data.getDate())));
    add(new Label("sender", data.getSender()));
    PFUserDO owner = data.getOwner();
    if (Hibernate.isInitialized(owner) == false) {
      owner = userGroupCache.getUser(owner.getId());
      data.setOwner(owner);
    }
    final UserSelectPanel userSelectPanel = new UserSelectPanel("owner", new PropertyModel<PFUserDO>(data, "owner"), parentPage, "ownerId");
    userSelectPanel.setRequired(true);
    add(userSelectPanel);
    userSelectPanel.init();
    add(new MaxLengthTextArea("message", new PropertyModel<String>(data, "message")));
    // DropDownChoice status
    final LabelValueChoiceRenderer<PostType> statusChoiceRenderer = new LabelValueChoiceRenderer<PostType>(this, MebEntryStatus.values());
    @SuppressWarnings("unchecked")
    final DropDownChoice statusChoice = new DropDownChoice("status", new PropertyModel(data, "status"), statusChoiceRenderer.getValues(),
        statusChoiceRenderer);
    statusChoice.setNullValid(false);
    statusChoice.setRequired(true);
    add(statusChoice);
    final SingleButtonPanel createTimesheetButton = new SingleButtonPanel("createTimesheet", new Button("button", new Model<String>(
        getString("timesheet.title.add"))) {
      @Override
      public final void onSubmit()
      {
        parentPage.createTimesheet();
      }
    });
    add(createTimesheetButton);
    // DropDownChoice favorites
    jiraProjectChoice = new FavoritesChoicePanel<JiraProject, JiraProject>("jiraProject", UserPrefArea.JIRA_PROJECT) {
      @Override
      protected void select(final JiraProject favorite)
      {
        if (StringUtils.isNotEmpty(this.selected) == true) {
          parentPage.putUserPrefEntry(USER_PREF_KEY_JIRA_PROJECT, this.selected, true);
        }
      }

      @Override
      protected JiraProject getCurrentObject()
      {
        return null;
      }

      @Override
      protected JiraProject newFavoriteInstance(final JiraProject currentObject)
      {
        return new JiraProject();
      }
    };
    jiraProjectChoice.setClearSelectionAfterSelection(false).setNullKey("jira.chooseProject");
    add(jiraProjectChoice);
    final DropDownChoice<String> choice = jiraProjectChoice.init();
    choice.setNullValid(false);
    List<JiraIssueType> issueTypes;
    if (jiraConfig != null && jiraConfig.getIssueTypes() != null) {
      issueTypes = jiraConfig.getIssueTypes();
    } else {
      issueTypes = new ArrayList<JiraIssueType>();
    }
    // DropDownChoice issueType
    final LabelValueChoiceRenderer<JiraIssueType> typeChoiceRenderer = new LabelValueChoiceRenderer<JiraIssueType>(this, issueTypes);
    @SuppressWarnings("unchecked")
    final DropDownChoice typeChoice = new DropDownChoice("issueType", new PropertyModel(this, "jiraIssueType"), typeChoiceRenderer
        .getValues(), typeChoiceRenderer) {
      @Override
      protected boolean wantOnSelectionChangedNotifications()
      {
        return true;
      }

      @Override
      protected void onSelectionChanged(final Object newSelection)
      {
        if (newSelection != null && newSelection instanceof Integer) {
          parentPage.putUserPrefEntry(USER_PREF_KEY_JIRA_ISSUE_TYPE, newSelection, true);
          // refresh();
        }
      }
    };
    final Integer recentJiraIssueType = (Integer) parentPage.getUserPrefEntry(Integer.class, USER_PREF_KEY_JIRA_ISSUE_TYPE);
    if (recentJiraIssueType != null) {
      this.jiraIssueType = recentJiraIssueType;
    }
    typeChoice.setNullValid(false);
    add(typeChoice);

    final AjaxButton createJiraIssueButton = new AjaxButton("createJiraIssue", new Model<String>(getString("meb.actions.createJIRAIssue"))) {
      public void onSubmit(final AjaxRequestTarget target, final Form< ? > form)
      {
        // ...create result page, get the url path to it...
        target.appendJavascript("window.open('" + buildCreateJiraIssueUrl() + "','newWindow');");
      }
    };
    add(createJiraIssueButton);
    if (jiraConfig == null || StringUtils.isEmpty(jiraConfig.getCreateIssueUrl()) == true) {
      jiraProjectChoice.setVisible(false);
      typeChoice.setVisible(false);
      // jiraCreateIssueLink.setVisible(false);
      createJiraIssueButton.setVisible(false);
    } else {
      final String recentJiraProjectFavorite = (String) parentPage.getUserPrefEntry(String.class, USER_PREF_KEY_JIRA_PROJECT);
      if (recentJiraProjectFavorite != null) {
        jiraProjectChoice.setSelected(recentJiraProjectFavorite);
      }
    }
  }

  private String buildCreateJiraIssueUrl()
  {
    if (jiraConfig == null || jiraConfig.getCreateIssueUrl() == null) {
      return "JIRA not configured.";
    }
    final JiraProject jiraProject = jiraProjectChoice.getCurrentFavorite();
    return jiraConfig.getCreateIssueUrl()
        + "?pid="
        + (jiraProject != null ? jiraProject.getPid() : null)
        + "&issuetype="
        + (jiraIssueType != null ? jiraIssueType : 3)
        + "&priority=4&reporter="
        + URLHelper.encode(getUser().getJiraUsernameOrUsername())
        + "&description="
        + URLHelper.encode(getData().getMessage());
  }

  public Integer getJiraIssueType()
  {
    return jiraIssueType;
  }

  public void setJiraIssueType(Integer jiraIssueType)
  {
    this.jiraIssueType = jiraIssueType;
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
