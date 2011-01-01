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

/**
 * 
 */
package org.projectforge.web.debug;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.lang.ClassUtils;
import org.projectforge.web.wicket.WicketApplication;


/**
 * In production environment this checker does nothing.
 * @author wolle
 * @see WicketApplication#isDevelopmentModus()
 * 
 */
public class SessionSerializableChecker implements HttpSessionAttributeListener
{
  /** The logger */
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SessionSerializableChecker.class);

  /**
   * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
   */
  public void attributeAdded(HttpSessionBindingEvent evt)
  {
    if (WicketApplication.isDevelopmentModus() == true) {
      check(evt.getSession(), evt.getName(), evt.getValue());
    }
  }

  /**
   * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
   */
  public void attributeRemoved(HttpSessionBindingEvent evt)
  {
  }

  /**
   * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
   */
  public void attributeReplaced(HttpSessionBindingEvent evt)
  {
    if (WicketApplication.isDevelopmentModus() == true) {
      check(evt.getSession(), evt.getName(), evt.getValue());
    }
  }

  private void check(HttpSession session, String name, Object value)
  {
    if (log.isInfoEnabled()) {
      try {
        if (log.isDebugEnabled()) {
          log
              .debug("Storing "
                  + ClassUtils.getShortClassName(value, "null")
                  + " under the name "
                  + name
                  + " in session "
                  + session.getId());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(value);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ois.readObject();
      } catch (Exception ex) {
        log.warn("Trying to store non-serializable value " + value + " under the name " + name + " in session " + session.getId(), ex);
      }
    }
  }
}
