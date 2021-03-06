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

package org.projectforge.plugins.memo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.projectforge.core.AbstractBaseDO;
import org.projectforge.database.Constants;
import org.projectforge.user.PFUserDO;

import de.micromata.hibernate.history.Historizable;

/**
 * This data object is the Java representation of a data-base entry of a memo.<br/>
 * Changes of this object will not be added to the history of changes. After deleting a memo it will be deleted in the data-base (there is
 * no undo!).<br/>
 * If you want to use the history of changes and undo functionality please use DefaultBaseDO as super class instead of AbstractBaseDO.
 * DefaultBaseDO implements the required interface {@link Historizable}.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
@Entity
@Indexed
@Table(name = "T_PLUGIN_MEMO")
public class MemoDO extends AbstractBaseDO<Integer>
{
  private static final long serialVersionUID = -1755234078022019415L;

  @Field(index = Index.UN_TOKENIZED, store = Store.NO)
  private Integer id;

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String subject;

  @IndexedEmbedded
  private PFUserDO owner;

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String memo;

  @Override
  @Id
  @GeneratedValue
  @Column(name = "pk")
  public Integer getId()
  {
    return id;
  }

  @Override
  public void setId(Integer id)
  {
    this.id = (Integer) id;
  }

  @Column(length = Constants.LENGTH_TITLE)
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject
   * @return this for chaining.
   */
  public MemoDO setSubject(String subject)
  {
    this.subject = subject;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_fk")
  public PFUserDO getOwner()
  {
    return owner;
  }

  @Transient
  public Integer getOwnerId()
  {
    return owner != null ? owner.getId() : null;
  }

  /**
   * @param owner
   * @return this for chaining.
   */
  public MemoDO setOwner(final PFUserDO owner)
  {
    this.owner = owner;
    return this;
  }

  @Column(length = Constants.LENGTH_TEXT)
  public String getMemo()
  {
    return memo;
  }

  /**
   * @return this for chaining.
   */
  public MemoDO setMemo(final String memo)
  {
    this.memo = memo;
    return this;
  }
}
