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

package org.projectforge.web.user;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.projectforge.user.GroupDO;
import org.projectforge.user.GroupDao;
import org.projectforge.web.wicket.AbstractBasePage;
import org.projectforge.web.wicket.AbstractEditPage;
import org.projectforge.web.wicket.EditPage;

@EditPage(defaultReturnPage = GroupListPage.class)
public class GroupEditPage extends AbstractEditPage<GroupDO, GroupEditForm, GroupDao>
{
  /**
   * Parameter for pre-defining group name (e. g. used by a wizard for creating new groups).
   */
  public static final String PARAM_GROUP_NAME = "groupName";

  private static final long serialVersionUID = 4636922408954211544L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GroupEditPage.class);

  @SpringBean(name = "groupDao")
  private GroupDao groupDao;

  public GroupEditPage(final PageParameters parameters)
  {
    super(parameters, "group");
    super.init();
    final String groupName = parameters.getString(PARAM_GROUP_NAME);
    if (StringUtils.isNotEmpty(groupName) == true) {
      getData().setName(groupName);
    }
  }

  @Override
  public AbstractBasePage onSaveOrUpdate()
  {
    final Set<Integer> assignedUserIds = new HashSet<Integer>();
    for (Integer userId : form.users.getAssignedValues()) {
      assignedUserIds.add(userId);
    }
    groupDao.setAssignedUsers(getData(), assignedUserIds);
    return super.onSaveOrUpdate();
  }

  @Override
  protected GroupDao getBaseDao()
  {
    return groupDao;
  }

  @Override
  protected GroupEditForm newEditForm(AbstractEditPage< ? , ? , ? > parentPage, GroupDO data)
  {
    return new GroupEditForm(this, data);
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
