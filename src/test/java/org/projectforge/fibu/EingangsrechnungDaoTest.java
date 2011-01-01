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

package org.projectforge.fibu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.sql.Date;

import org.junit.Test;
import org.projectforge.access.AccessException;
import org.projectforge.fibu.EingangsrechnungDO;
import org.projectforge.fibu.EingangsrechnungDao;
import org.projectforge.fibu.EingangsrechnungsPositionDO;
import org.projectforge.fibu.RechnungFilter;
import org.projectforge.test.TestBase;


public class EingangsrechnungDaoTest extends TestBase
{
  private EingangsrechnungDao eingangsrechnungDao;

  @Test
  public void checkAccess()
  {
    logon(TEST_FINANCE_USER);
    EingangsrechnungDO eingangsrechnung = new EingangsrechnungDO();
    eingangsrechnung.setDatum(new Date(System.currentTimeMillis()));
    eingangsrechnung.addPosition(new EingangsrechnungsPositionDO());
    Serializable id = eingangsrechnungDao.save(eingangsrechnung);
    eingangsrechnung = eingangsrechnungDao.getById(id);

    logon(TEST_CONTROLLING_USER);
    eingangsrechnungDao.getById(id);
    checkNoWriteAccess(id, eingangsrechnung, "Controlling");

    logon(TEST_USER);
    checkNoAccess(id, eingangsrechnung, "Other");

    logon(TEST_PROJECT_MANAGER_USER);
    checkNoAccess(id, eingangsrechnung, "Project manager");

    logon(TEST_ADMIN_USER);
    checkNoAccess(id, eingangsrechnung, "Admin ");
  }

  private void checkNoAccess(Serializable id, EingangsrechnungDO eingangsrechnung, String who)
  {
    try {
      RechnungFilter filter = new RechnungFilter();
      eingangsrechnungDao.getList(filter);
      fail("AccessException expected: " + who + " users should not have select list access to invoices.");
    } catch (AccessException ex) {
      // OK
    }
    try {
      eingangsrechnungDao.getById(id);
      fail("AccessException expected: " + who + " users should not have select access to invoices.");
    } catch (AccessException ex) {
      // OK
    }
    checkNoHistoryAccess(id, eingangsrechnung, who);
    checkNoWriteAccess(id, eingangsrechnung, who);
  }

  private void checkNoHistoryAccess(Serializable id, EingangsrechnungDO eingangsrechnung, String who)
  {
    assertEquals(who + " users should not have select access to history of invoices.", eingangsrechnungDao.hasHistoryAccess(false), false);
    try {
      eingangsrechnungDao.hasHistoryAccess(true);
      fail("AccessException expected: " + who + " users should not have select access to history of invoices.");
    } catch (AccessException ex) {
      // OK
    }
    assertEquals(who + " users should not have select access to history of invoices.", eingangsrechnungDao.hasHistoryAccess(
        eingangsrechnung, false), false);
    try {
      eingangsrechnungDao.hasHistoryAccess(eingangsrechnung, true);
      fail("AccessException expected: " + who + " users should not have select access to history of invoices.");
    } catch (AccessException ex) {
      // OK
    }
  }

  private void checkNoWriteAccess(Serializable id, EingangsrechnungDO eingangsrechnung, String who)
  {
    try {
      EingangsrechnungDO re = new EingangsrechnungDO();
      re.setDatum(new Date(System.currentTimeMillis()));
      eingangsrechnungDao.save(re);
      fail("AccessException expected: " + who + " users should not have save access to invoices.");
    } catch (AccessException ex) {
      // OK
    }
    try {
      eingangsrechnung.setBemerkung(who);
      eingangsrechnungDao.update(eingangsrechnung);
      fail("AccessException expected: " + who + " users should not have update access to invoices.");
    } catch (AccessException ex) {
      // OK
    }
  }

  public void setEingangsrechnungDao(EingangsrechnungDao eingangsrechnungDao)
  {
    this.eingangsrechnungDao = eingangsrechnungDao;
  }
}
