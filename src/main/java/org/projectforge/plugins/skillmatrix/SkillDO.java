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

package org.projectforge.plugins.skillmatrix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.projectforge.core.DefaultBaseDO;
import org.projectforge.core.UserPrefParameter;
import org.projectforge.database.Constants;

/**
 * A skill usable for a skill-matrix.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
@Entity
@Indexed
@Table(name = "T_PLUGIN_SKILL")
public class SkillDO extends DefaultBaseDO
{
  private static final long serialVersionUID = 6102127905651011282L;

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String title;

  // Null if this skill is a top level skill.
  @IndexedEmbedded(depth = 1)
  private SkillDO parent;

  @UserPrefParameter(i18nKey = "description", multiline = true)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String description;

  @UserPrefParameter(i18nKey = "comment", multiline = true)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String comment;

  private boolean rateable = true;

  @Column(length = 100)
  public String getTitle()
  {
    return title;
  }

  /**
   * @param title
   * @return this for chaining.
   */
  public SkillDO setTitle(final String title)
  {
    this.title = title;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_fk")
  public SkillDO getParent()
  {
    return parent;
  }

  @Transient
  public Integer getParentId()
  {
    return parent != null ? parent.getId() : null;
  }

  /**
   * @param parent
   * @return this for chaining.
   */
  public SkillDO setParent(final SkillDO parent)
  {
    this.parent = parent;
    return this;
  }

  @Column(length = Constants.LENGTH_TEXT)
  public String getComment()
  {
    return comment;
  }

  /**
   * @return this for chaining.
   */
  public SkillDO setComment(final String comment)
  {
    this.comment = comment;
    return this;
  }

  /**
   * This value should be false for skills which should be used as categories or sub categories for which a rating isn't useful. But for
   * some categories is it useful to define them as rateable (e. g. for Programming languages -> Java -> J2EE the skill Java should be
   * rateable).
   */
  @Column
  public boolean isRateable()
  {
    return rateable;
  }

  /**
   * @return this for chaining.
   */
  public SkillDO setRateable(final boolean rateable)
  {
    this.rateable = rateable;
    return this;
  }

  @Column(length = Constants.LENGTH_TEXT)
  public String getDescription()
  {
    return description;
  }

  /**
   * @return this for chaining.
   */
  public SkillDO setDescription(final String description)
  {
    this.description = description;
    return this;
  }

}
