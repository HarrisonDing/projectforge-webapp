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

package org.projectforge.web.wicket;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.projectforge.core.AbstractBaseDO;
import org.projectforge.core.BaseDao;
import org.projectforge.core.IManualIndex;
import org.projectforge.core.UserException;
import org.springframework.dao.DataIntegrityViolationException;

public class EditPageSupport<O extends AbstractBaseDO< ? >, D extends BaseDao<O>> implements Serializable
{
  private static final long serialVersionUID = 5504452697069803264L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditPageSupport.class);

  private final IEditPage<O, D> editPage;

  private final D baseDao;

  private final O data;

  private boolean updateAndNext;

  public EditPageSupport(final IEditPage<O, D> editPage, final D baseDao, final O data)
  {
    this.editPage = editPage;
    this.baseDao = baseDao;
    this.data = data;
  }

  /**
   * User has clicked the save button for storing a new item.
   */
  public void create()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("create in " + editPage.getClass() + ": " + data);
    }
    synchronized (data) {
      if (editPage.isAlreadySubmitted() == true) {
        log.info("Double click detection in " + editPage.getClass() + " create method. Do nothing.");
      } else {
        editPage.setAlreadySubmitted(true);
        if (data.getId() != null && data instanceof IManualIndex == false) {
          // User has used the back button?
          log.info("User has used the back button in "
              + editPage.getClass()
              + " after inserting a new object? Try to load the object from the data base and show edit page again.");
          final O dbObj = baseDao.getById(data.getId());
          if (dbObj == null) {
            // Error while trying to insert Object and user has used the back button?
            log.info("User has used the back button "
                + editPage.getClass()
                + " after inserting a new object and a failure occured (because object with id not found in the data base)? Deleting the id and show the edit page again.");
            editPage.clearIds();
            return;
          }
          data.copyValuesFrom(dbObj);
          return;
        }
        WebPage page = editPage.onSaveOrUpdate();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        try {
          baseDao.save(data);
        } catch (final DataIntegrityViolationException ex) {
          log.error(ex.getMessage(), ex);
          throw new UserException("exception.constraintViolation");
        }
        page = editPage.afterSaveOrUpdate();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        page = editPage.afterSave();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
      }
      editPage.setResponsePage();
    }
  }

  /**
   * User has clicked the update button for updating an existing item.
   */
  public void update()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("update in " + editPage.getClass() + ": " + data);
    }
    synchronized (data) {
      if (editPage.isAlreadySubmitted() == true) {
        log.info("Double click detection in " + editPage.getClass() + " update method. Do nothing.");
      } else {
        editPage.setAlreadySubmitted(true);
        WebPage page = editPage.onSaveOrUpdate();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        boolean modified = false;
        try {
          modified = baseDao.update(data);
        } catch (final DataIntegrityViolationException ex) {
          log.error(ex.getMessage(), ex);
          throw new UserException("exception.constraintViolation");
        }
        page = editPage.afterSaveOrUpdate();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        page = editPage.afterUpdate(modified);
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
      }
    }
    editPage.setResponsePage();
  }

  /**
   * User has clicked the update-and-next button for updating an existing item.
   */
  public void updateAndNext()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("update in " + editPage.getClass() + ": " + data);
    }
    update();
    updateAndNext = true;
    editPage.setResponsePage();
  }

  public boolean isUpdateAndNext()
  {
    return updateAndNext;
  }

  public void setUpdateAndNext(final boolean updateAndNext)
  {
    this.updateAndNext = updateAndNext;
  }

  public void undelete()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("undelete in " + editPage.getClass() + ": " + data);
    }
    synchronized (data) {
      if (editPage.isAlreadySubmitted() == true) {
        log.info("Double click detection in " + editPage.getClass() + " undelete method. Do nothing.");
      } else {
        editPage.setAlreadySubmitted(true);
        final WebPage page = editPage.onUndelete();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        baseDao.undelete(data);
      }
    }
    editPage.afterUndelete();
    editPage.setResponsePage();
  }

  public void markAsDeleted()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("Mark object as deleted in " + editPage.getClass() + ": " + data);
    }
    synchronized (data) {
      if (editPage.isAlreadySubmitted() == true) {
        log.info("Double click detection in " + editPage.getClass() + " markAsDeleted method. Do nothing.");
      } else {
        editPage.setAlreadySubmitted(true);
        final WebPage page = editPage.onDelete();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        baseDao.markAsDeleted(data);
        editPage.afterDelete();
        editPage.setResponsePage();
      }
    }
  }

  public void delete()
  {
    if (log.isDebugEnabled() == true) {
      log.debug("delete in " + editPage.getClass() + ": " + data);
    }
    synchronized (data) {
      if (editPage.isAlreadySubmitted() == true) {
        log.info("Double click detection in " + editPage.getClass() + " delete method. Do nothing.");
      } else {
        editPage.setAlreadySubmitted(true);
        final WebPage page = editPage.onDelete();
        if (page != null) {
          editPage.setResponsePageAndHighlightedRow(page);
          return;
        }
        baseDao.delete(data);
        editPage.afterDelete();
        editPage.setResponsePage();
      }
    }
  }
}
