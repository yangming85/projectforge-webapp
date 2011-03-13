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

package org.projectforge.web.wicket.layout;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.projectforge.web.wicket.components.DatePanel;

/**
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
public class ContainerLPanel extends AbstractLPanel
{
  public static final String WICKET_ID = "container";

  private static final long serialVersionUID = 4851316682458152524L;

  private WebMarkupContainer labelTag;

  private WebMarkupContainer container;

  /**
   * @see AbstractFormRenderer#createDateFieldPanel(String, LayoutLength, DatePanel)
   */
  ContainerLPanel(final String id, final WebMarkupContainer container, final PanelContext ctx)
  {
    super(id, ctx);
    add(labelTag = new WebMarkupContainer("label"));
    labelTag.add(this.container = container);
  }

  public ContainerLPanel replaceWithContainer(final WebMarkupContainer newContainer)
  {
    if (container != null) {
      labelTag.remove(container);
    }
    labelTag.add(this.container = newContainer);
    return this;
  }

  @Override
  protected Component getClassModifierComponent()
  {
    return labelTag;
  }

  @Override
  public Component getWrappedComponent()
  {
    return labelTag;
  }
}
