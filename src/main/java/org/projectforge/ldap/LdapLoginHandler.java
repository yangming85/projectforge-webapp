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

package org.projectforge.ldap;

import java.util.List;

import org.projectforge.access.AccessChecker;
import org.projectforge.core.ConfigXml;
import org.projectforge.registry.Registry;
import org.projectforge.user.LoginDefaultHandler;
import org.projectforge.user.LoginHandler;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserDao;
import org.projectforge.user.UserRights;

public abstract class LdapLoginHandler implements LoginHandler
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LdapSlaveLoginHandler.class);

  UserDao userDao;

  AccessChecker accessChecker;

  LdapConnector ldapConnector;

  LdapGroupDao ldapGroupDao;

  LdapUserDao ldapUserDao;

  LdapConfig ldapConfig;

  protected LoginDefaultHandler loginDefaultHandler;

  /**
   * @see org.projectforge.user.LoginHandler#initialize()
   */
  @Override
  public void initialize()
  {
    this.ldapConfig = ConfigXml.getInstance().getLdapConfig();
    if (ldapConfig == null || ldapConfig.getServer() == null) {
      log.warn("No LDAP configured in config.xml, so any login won't be possible!");
    }
    ldapConnector = new LdapConnector(ldapConfig);
    ldapGroupDao = new LdapGroupDao();
    ldapGroupDao.ldapConnector = ldapConnector;
    ldapUserDao = new LdapUserDao();
    ldapUserDao.ldapConnector = ldapConnector;
    final Registry registry = Registry.instance();
    userDao = (UserDao) registry.getDao(UserDao.class);
    accessChecker = UserRights.getAccessChecker();
    loginDefaultHandler = new LoginDefaultHandler();
    loginDefaultHandler.initialize();
  }

  /**
   * Calls {@link LoginDefaultHandler#checkStayLogin(PFUserDO)}.
   * @see org.projectforge.user.LoginHandler#checkStayLogin(org.projectforge.user.PFUserDO)
   */
  @Override
  public boolean checkStayLogin(final PFUserDO user)
  {
    return loginDefaultHandler.checkStayLogin(user);
  }

  public boolean isAdminUser(final PFUserDO user)
  {
    return accessChecker.isUserMemberOfAdminGroup(user);
  }

  protected List<LdapPerson> getAllLdapUsers()
  {
    final String organizationalUnits = ldapConfig.getUserBase();
    final List<LdapPerson> ldapUsers = ldapUserDao.findAll(organizationalUnits);
    return ldapUsers;
  }

  protected List<LdapGroup> getAllLdapGroups()
  {
    final String organizationalUnits = ldapConfig.getGroupBase();
    final List<LdapGroup> ldapGroups = ldapGroupDao.findAll(organizationalUnits);
    return ldapGroups;
  }
}