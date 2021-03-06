/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2014 Kai Reinhard (k.reinhard@micromata.de)
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

package org.projectforge.plugins.poll.attendee;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.projectforge.core.BaseDao;
import org.projectforge.core.QueryFilter;
import org.projectforge.plugins.poll.PollDO;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserRightId;

/**
 * @author M. Lauterbach (m.lauterbach@micromata.de)
 * 
 */
public class PollAttendeeDao extends BaseDao<PollAttendeeDO>
{
  public static final UserRightId USER_RIGHT_ID = new UserRightId("PLUGIN_POLL_ATTENDEE", "plugin31", "plugins.poll");

  /**
   * @param clazz
   */
  protected PollAttendeeDao()
  {
    super(PollAttendeeDO.class);
    userRightId = USER_RIGHT_ID;
  }

  /**
   * @see org.projectforge.core.BaseDao#newInstance()
   */
  @Override
  public PollAttendeeDO newInstance()
  {
    return new PollAttendeeDO();
  }

  /**
   * get list of attendees by poll id, without deleted.
   * 
   * @param poll
   * @return
   */
  public List<PollAttendeeDO> getListByPoll(final PollDO poll)
  {
    final QueryFilter qFilter = new QueryFilter();
    if(poll != null && poll.getId() != null) {
      qFilter.add(Restrictions.and(Restrictions.eq("poll", poll), Restrictions.eq("deleted", false)));
    }
    return getList(qFilter);
  }

  /**
   * Verify if key is able to access poll data.
   * 
   * @param user
   * @param secureKey
   * @param poll - poll to check
   * @return
   */
  public boolean verifyUserOrKey(final PFUserDO user, final String secureKey, final PollDO poll)
  {
    final QueryFilter qFilter = new QueryFilter();
    if (user != null) {
      qFilter.add(Restrictions.and(Restrictions.and(Restrictions.eq("poll", poll), Restrictions.eq("user", user)),
          Restrictions.eq("deleted", false)));
    } else {
      qFilter.add(Restrictions.and(Restrictions.and(Restrictions.eq("poll", poll), Restrictions.eq("secureKey", secureKey)),
          Restrictions.eq("deleted", false)));
    }
    return !getList(qFilter).isEmpty();
  }
}
