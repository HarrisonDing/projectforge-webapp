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

import java.util.TimeZone;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.projectforge.core.Configuration;
import org.projectforge.user.PFUserContext;
import org.projectforge.user.PFUserDO;
import org.projectforge.web.UserAgentDevice;

public class MySession extends WebSession
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MySession.class);

  private static final long serialVersionUID = -1783696379234637066L;

  private PFUserDO user;

  private String userAgent;

  private UserAgentDevice userAgentDevice = UserAgentDevice.UNKNOWN;

  private boolean mobileUserAgent;

  private boolean ignoreMobileUserAgent;

  public MySession(final Request request)
  {
    super(request);
    setLocale(PFUserContext.getLocale(request.getLocale()));
    final ClientInfo info = getClientInfo();
    if (info instanceof WebClientInfo) {
      ((WebClientInfo) info).getProperties().setTimeZone(PFUserContext.getTimeZone());
      userAgent = ((WebClientInfo) info).getUserAgent();
      userAgentDevice = UserAgentDevice.getUserAgentDevice(userAgent);
      mobileUserAgent = userAgentDevice.isMobile();
    } else {
      log.error("Oups, ClientInfo is not from type WebClientInfo: " + info);
    }
    setUser(PFUserContext.getUser());
  }

  public static MySession get()
  {
    return (MySession) Session.get();
  }

  public synchronized PFUserDO getUser()
  {
    return user;
  }

  public synchronized void setUser(PFUserDO user)
  {
    this.user = user;
    dirty();
  }

  public synchronized boolean isAuthenticated()
  {
    return (user != null);
  }

  public synchronized TimeZone getTimeZone()
  {
    return user != null ? user.getTimeZoneObject() : Configuration.getInstance().getDefaultTimeZone();
  }

  public String getUserAgent()
  {
    return userAgent;
  }
  
  /**
   * @return true, if the user agent device is an iPad, iPhone or iPod.
   */
  public boolean isIOSDevice() {
    return this.userAgentDevice != null && this.userAgentDevice.isIn(UserAgentDevice.IPAD, UserAgentDevice.IPHONE, UserAgentDevice.IPOD);
  }

  /**
   * @return true, if the user agent is a mobile agent and ignoreMobileUserAgent isn't set, otherwise false.
   */
  public boolean isMobileUserAgent()
  {
    if (ignoreMobileUserAgent == true) {
      return false;
    }
    return mobileUserAgent;
  }

  /**
   * The user wants to ignore the mobile agent and wants to get the PC version (normal web version).
   * @return
   */
  public boolean isIgnoreMobileUserAgent()
  {
    return ignoreMobileUserAgent;
  }

  public void setIgnoreMobileUserAgent(final boolean ignoreMobileUserAgent)
  {
    this.ignoreMobileUserAgent = ignoreMobileUserAgent;
  }

  public void login(final PFUserDO user)
  {
    if (user == null) {
      log.warn("Oups, no user given to log in.");
      return;
    }
    this.user = user;
    log.debug("User logged in: " + user.getShortDisplayName());
    PFUserContext.setUser(user);
  }

  public void logout()
  {
    if (user != null) {
      log.info("User logged out: " + user.getShortDisplayName());
      user = null;
    }
    PFUserContext.setUser(null);
    super.clear();
    super.invalidate();
  }

  public void put(final String name, final Object value)
  {
    super.setAttribute(name, value);
  }

  public Object get(final String name)
  {
    return super.getAttribute(name);
  }
}
