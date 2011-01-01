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

package org.projectforge.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.projectforge.access.AccessException;
import org.projectforge.address.AddressDO;
import org.projectforge.address.AddressDao;
import org.projectforge.book.BookDO;
import org.projectforge.book.BookDao;
import org.projectforge.task.TaskDO;
import org.projectforge.task.TaskDao;
import org.projectforge.test.TestBase;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserGroupCache;


public class InitDatabaseDaoWithTestDataTest extends TestBase
{
  private InitDatabaseDao initDatabaseDao;

  private AddressDao addressDao;

  private TaskDao taskDao;

  private BookDao bookDao;

  private UserGroupCache userGroupCache;

  public void setInitDatabaseDao(InitDatabaseDao initDatabaseDao)
  {
    this.initDatabaseDao = initDatabaseDao;
  }

  public void setAddressDao(AddressDao addressDao)
  {
    this.addressDao = addressDao;
  }

  public void setTaskDao(TaskDao taskDao)
  {
    this.taskDao = taskDao;
  }

  public void setBookDao(BookDao bookDao)
  {
    this.bookDao = bookDao;
  }

  public void setUserGroupCache(UserGroupCache userGroupCache)
  {
    this.userGroupCache = userGroupCache;
  }

  @BeforeClass
  public static void setUp() throws Exception
  {
    init(false);
  }

  @Test
  public void initializeEmptyDatabase()
  {
    final String encryptedPassword = userDao.encryptPassword(InitDatabaseDao.DEFAULT_ADMIN_PASSWORD);
    userGroupCache.setExpired(); // Force reload (because it's may be expired due to previous tests).
    assertTrue(initDatabaseDao.isEmpty());
    initDatabaseDao.initializeEmptyDatabaseWithTestData(encryptedPassword, null);
    PFUserDO user = userDao.authenticateUser("admin", userDao.encryptPassword("manage"));
    assertNotNull(user);
    assertEquals("admin", user.getUsername());
    Collection<Integer> col = userGroupCache.getUserGroups(user);
    assertEquals(5, col.size());
    assertTrue(userGroupCache.isUserMemberOfAdminGroup(user.getId()));
    assertTrue(userGroupCache.isUserMemberOfFinanceGroup(user.getId()));

    List<AddressDO> addressList = addressDao.internalLoadAll();
    assertEquals(1, addressList.size());
    
    List<BookDO> bookList = bookDao.internalLoadAll();
    assertEquals(2, bookList.size());
    
    List<TaskDO> taskList = taskDao.internalLoadAll();
    assertEquals(6, taskList.size());
    
    boolean exception = false;
    try {
      initDatabaseDao.initializeEmptyDatabase(encryptedPassword, null);
      fail("AccessException expected.");
    } catch (AccessException ex) {
      exception = true;
      // Everything fine.
    }
    assertTrue(exception);

    exception = false;
    try {
      initDatabaseDao.initializeEmptyDatabaseWithTestData(encryptedPassword, null);
      fail("AccessException expected.");
    } catch (AccessException ex) {
      exception = true;
      // Everything fine.
    }
    assertTrue(exception);
  }
}
