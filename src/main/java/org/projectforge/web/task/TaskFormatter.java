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

package org.projectforge.web.task;

import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.Hibernate;
import org.projectforge.task.TaskDO;
import org.projectforge.task.TaskNode;
import org.projectforge.task.TaskStatus;
import org.projectforge.task.TaskTree;
import org.projectforge.web.HtmlHelper;
import org.projectforge.web.common.OutputType;
import org.projectforge.web.core.AbstractFormatter;
import org.projectforge.web.core.LocalizerAndUrlBuilder;
import org.projectforge.web.core.PageContextLocalizerAndUrlBuilder;
import org.projectforge.web.wicket.WicketUtils;


public class TaskFormatter extends AbstractFormatter
{
  private TaskTree taskTree;

  private HtmlHelper htmlHelper;

  public void setTaskTree(TaskTree taskTree)
  {
    this.taskTree = taskTree;
  }

  public void setHtmlHelper(HtmlHelper htmlHelper)
  {
    this.htmlHelper = htmlHelper;
  }

  /**
   * enableLinks = false, lineThroughDeletedTasks = true
   * @param taskId
   * @see #getTaskPath(Integer, boolean)
   */
  public String getTaskPath(PageContext pageContext, Integer taskId)
  {
    return getTaskPath(pageContext, taskId, false, true);
  }

  /**
   * Gets the path of the task as String: ProjectForge -&gt; ... -&gt; database -&gt; backup strategy.
   * @param taskId
   * @param enableLinks If true, every task title is associated with a link to EditTask.
   */
  public String getTaskPath(PageContext pageContext, Integer taskId, boolean enableLinks, boolean lineThroughDeletedTasks)
  {
    return getTaskPath(pageContext, taskId, null, enableLinks, lineThroughDeletedTasks);
  }

  /**
   * Gets the path of the task as String: ProjectForge -&gt; ... -&gt; database -&gt; backup strategy.
   * @param taskId
   * @param enableLinks If true, every task title is associated with a link to EditTask.
   * @param lineThroughDeletedTasks If true, deleted task will be visualized by line through.
   * @param ancestorTaskId If not null, the path will shown between taskId and ancestorTaskId. If mainTaskId is not an ancestor of taskId,
   *                the whole path will be shown.
   */
  public String getTaskPath(PageContext pageContext, Integer taskId, Integer ancestorTaskId, boolean enableLinks,
      boolean lineThroughDeletedTasks)
  {
    return getTaskPath(new PageContextLocalizerAndUrlBuilder(pageContext), taskId, ancestorTaskId, enableLinks, lineThroughDeletedTasks);
  }

  /**
   * Gets the path of the task as String: ProjectForge -&gt; ... -&gt; database -&gt; backup strategy.
   * @param taskId
   * @param enableLinks If true, every task title is associated with a link to EditTask.
   * @param lineThroughDeletedTasks If true, deleted task will be visualized by line through.
   * @param ancestorTaskId If not null, the path will shown between taskId and ancestorTaskId. If mainTaskId is not an ancestor of taskId,
   *                the whole path will be shown.
   */
  public String getTaskPath(LocalizerAndUrlBuilder locUrlBuilder, Integer taskId, Integer ancestorTaskId, boolean enableLinks,
      boolean lineThroughDeletedTasks)
  {
    if (taskId == null || taskTree.getTaskNodeById(taskId) == null) {
      return null;
    }
    List<TaskNode> list = taskTree.getPath(taskId, ancestorTaskId);
    if (list.size() > 0) {
      StringBuffer buf = new StringBuffer();
      int i = 0;
      for (TaskNode node : list) {
        TaskDO task = node.getTask();
        if (i++ > 0) {
          buf.append(" -&gt; ");
        }
        appendFormattedTask(buf, locUrlBuilder, task, enableLinks, false, lineThroughDeletedTasks);
      }
      return buf.toString();
    } else if (ancestorTaskId != null) {
      return "";
    } else {
      return getI18nMessage("task.path.rootTask");
    }
  }

  /**
   * Formats path to root: "task1 -> task2 -> task3".
   * @param taskId
   * @param showCurrentTask if true also the given task by id will be added to the path, otherwise the path of the parent task will be
   *                shown.
   * @param escapeHtml
   */
  public String getTaskPath(Integer taskId, boolean showCurrentTask, OutputType outputType)
  {
    return getTaskPath(taskId, null, showCurrentTask, outputType);
  }

  /**
   * Formats path to ancestor task if given or to root: "task1 -> task2 -> task3".
   * @param taskId
   * @param ancestorTaskId
   * @param showCurrentTask if true also the given task by id will be added to the path, otherwise the path of the parent task will be
   *                shown.
   * @param escapeHtml
   */
  public String getTaskPath(Integer taskId, Integer ancestorTaskId, boolean showCurrentTask, OutputType outputType)
  {
    if (taskId == null) {
      return null;
    }
    TaskNode n = taskTree.getTaskNodeById(taskId);
    if (n == null) {
      return null;
    }
    if (showCurrentTask == false) {
      n = n.getParent();
      if (n == null) {
        return null;
      }
      taskId = n.getTaskId();
    }
    List<TaskNode> list = taskTree.getPath(taskId, ancestorTaskId);
    if (CollectionUtils.isEmpty(list) == true) {
      return "";
    }
    StringBuffer buf = new StringBuffer();
    int i = 0;
    for (TaskNode node : list) {
      TaskDO task = node.getTask();
      if (i++ > 0) {
        buf.append(" -> ");
      }
      buf.append(task.getTitle());
    }
    if (outputType == OutputType.HTML) {
      return StringEscapeUtils.escapeHtml(buf.toString());
    } else if (outputType == OutputType.XML) {
      return StringEscapeUtils.escapeXml(buf.toString());
    } else {
      return buf.toString();
    }
  }

  /**
   * Writes the html formatted task to the given StringBuffer.
   * @param buf
   * @param pageContext
   * @param task
   * @param enableLink If true, the task has a link to the EditTask.action.
   * @param showPathAsTooltip If true, an info icon with the whole task path as tooltip will be added.
   */
  public void appendFormattedTask(StringBuffer buf, PageContext pageContext, TaskDO task, boolean enableLink, boolean showPathAsTooltip,
      boolean lineThroughDeletedTask)
  {
    appendFormattedTask(buf, new PageContextLocalizerAndUrlBuilder(pageContext), task, enableLink, showPathAsTooltip,
        lineThroughDeletedTask);
  }

  /**
   * Writes the html formatted task to the given StringBuffer.
   * @param buf
   * @param pageContext
   * @param task
   * @param enableLink If true, the task has a link to the EditTask.action.
   * @param showPathAsTooltip If true, an info icon with the whole task path as tooltip will be added.
   */
  public void appendFormattedTask(StringBuffer buf, LocalizerAndUrlBuilder locUrlBuilder, TaskDO task, boolean enableLink,
      boolean showPathAsTooltip, boolean lineThroughDeletedTask)
  {
    Validate.notNull(buf);
    Validate.notNull(locUrlBuilder);
    Validate.notNull(task);
    if (showPathAsTooltip == true) {
      String taskPath = getTaskPath(locUrlBuilder, task.getId(), null, false, false);
      if (taskPath != null) {
        htmlHelper.appendImageTag(locUrlBuilder, buf, htmlHelper.getInfoImage(), taskPath);
      }
    }
    if (enableLink == true) {
      htmlHelper.appendAncorStartTag(locUrlBuilder, buf, WicketUtils.getBookmarkablePageUrl(TaskEditPage.class, "id", String.valueOf(task.getId())));
    }
    if (Hibernate.isInitialized(task) == false) {
      task = taskTree.getTaskById(task.getId());
    }
    if (task.isDeleted() == true) {
      if (lineThroughDeletedTask == true) {
        buf.append("<span");
        htmlHelper.attribute(buf, "style", "text-decoration: line-through;");
        buf.append(">");
        buf.append(HtmlHelper.escapeXml(task.getTitle()));
        buf.append("</span>");
      } else {
        buf.append(HtmlHelper.escapeXml(task.getTitle())).append(" (");
        buf.append(getI18nMessage("task.deleted"));
        buf.append(")");
      }
    } else {
      buf.append(HtmlHelper.escapeXml(task.getTitle()));
    }
    if (enableLink == true) {
      htmlHelper.appendAncorEndTag(buf);
    }
  }
  
  public String getFormattedTaskStatus(final TaskStatus status) {
    if (status == TaskStatus.N) {
      // Show 'not opened' as blank field:
      return "";
    }
    StringBuffer buf = new StringBuffer();
    buf.append("<span");
    htmlHelper.attribute(buf, "class", "taskStatus_" + status.getKey());
    buf.append(">");
    buf.append(getI18nMessage("task.status." + status.getKey()));
    buf.append("</span>");
    return buf.toString();
  }

  @Deprecated
  public String appendFormattedTaskStatus(PageContext pageContext, TaskStatus status)
  {
    return getFormattedTaskStatus(status);
  }
}
