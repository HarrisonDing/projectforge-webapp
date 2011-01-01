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

package org.projectforge.address;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.projectforge.common.LabelValueBean;
import org.projectforge.common.StringHelper;
import org.projectforge.core.DefaultBaseDO;
import org.projectforge.core.HibernateSearchPhoneNumberBridge;
import org.projectforge.task.TaskDO;

/**
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
@Entity
@Indexed
@Table(name = "T_ADDRESS")
public class AddressDO extends DefaultBaseDO
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddressDO.class);

  private static final long serialVersionUID = 974064367925158463L;

  private TaskDO task;

  private ContactStatus contactStatus = ContactStatus.ACTIVE;

  private AddressStatus addressStatus = AddressStatus.UPTODATE;

  private boolean imageBroschure;

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String name; // 255 not null

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String firstName; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private FormOfAddress form;

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String title; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String positionText; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String organization; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String division; // 255

  @FieldBridge(impl = HibernateSearchPhoneNumberBridge.class)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String businessPhone; // 255

  @FieldBridge(impl = HibernateSearchPhoneNumberBridge.class)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String mobilePhone; // 255

  @FieldBridge(impl = HibernateSearchPhoneNumberBridge.class)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String fax; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String addressText; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String zipCode; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String city; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String country; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String state; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String postalAddressText; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String postalZipCode; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String postalCity; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String postalCountry; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String postalState; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String email; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String website; // 255

  @FieldBridge(impl = HibernateSearchPhoneNumberBridge.class)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privatePhone; // 255

  @FieldBridge(impl = HibernateSearchPhoneNumberBridge.class)
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateMobilePhone; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateAddressText; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateZipCode; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateCity; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateCountry; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateState; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String privateEmail; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String publicKey; // 5000

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String fingerprint; // 255

  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String comment; // 5000;

  @Field(index = Index.UN_TOKENIZED)
  @DateBridge(resolution = Resolution.DAY)
  private Date birthday;

  // @FieldBridge(impl = HibernateSearchInstantMessagingBridge.class)
  // @Field(index = Index.TOKENIZED, store = Store.NO)
  // TODO: Prepared for hibernate search.
  private List<LabelValueBean<InstantMessagingType, String>> instantMessaging;

  @Enumerated(EnumType.STRING)
  @Column(name = "contact_status", length = 20, nullable = false)
  public ContactStatus getContactStatus()
  {
    return contactStatus;
  }

  public void setContactStatus(ContactStatus contactStatus)
  {
    this.contactStatus = contactStatus;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "address_status", length = 20, nullable = false)
  public AddressStatus getAddressStatus()
  {
    return addressStatus;
  }

  public void setAddressStatus(AddressStatus addressStatus)
  {
    this.addressStatus = addressStatus;
  }

  @Column(name = "image_broschure", nullable = false)
  public boolean isImageBroschure()
  {
    return imageBroschure;
  }

  public void setImageBroschure(boolean imageBroschure)
  {
    this.imageBroschure = imageBroschure;
  }

  @Column(name = "business_phone", length = 255)
  public String getBusinessPhone()
  {
    return businessPhone;
  }

  public void setBusinessPhone(String businessPhone)
  {
    this.businessPhone = businessPhone;
  }

  @Column(name = "mobile_phone", length = 255)
  public String getMobilePhone()
  {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone)
  {
    this.mobilePhone = mobilePhone;
  }

  @Column(length = 255)
  public String getFax()
  {
    return fax;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  @Column(length = 255)
  public String getAddressText()
  {
    return addressText;
  }

  public void setAddressText(String addressText)
  {
    this.addressText = addressText;
  }

  @Column(name = "zip_code", length = 255)
  public String getZipCode()
  {
    return zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  @Column(length = 255)
  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  @Column(length = 255)
  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  @Column(length = 255)
  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  @Column(length = 255, name = "postal_addresstext")
  public String getPostalAddressText()
  {
    return postalAddressText;
  }

  public void setPostalAddressText(String postalAddressText)
  {
    this.postalAddressText = postalAddressText;
  }

  @Column(name = "postal_zip_code", length = 255)
  public String getPostalZipCode()
  {
    return postalZipCode;
  }

  public void setPostalZipCode(String postalZipCode)
  {
    this.postalZipCode = postalZipCode;
  }

  @Column(length = 255, name = "postal_city")
  public String getPostalCity()
  {
    return postalCity;
  }

  public void setPostalCity(String postalCity)
  {
    this.postalCity = postalCity;
  }

  @Column(name = "postal_country", length = 255)
  public String getPostalCountry()
  {
    return postalCountry;
  }

  public void setPostalCountry(String postalCountry)
  {
    this.postalCountry = postalCountry;
  }

  @Column(name = "postal_state", length = 255)
  public String getPostalState()
  {
    return postalState;
  }

  public void setPostalState(String postalState)
  {
    this.postalState = postalState;
  }

  @Column
  public Date getBirthday()
  {
    return birthday;
  }

  public void setBirthday(Date birthday)
  {
    this.birthday = birthday;
  }

  @Column(name = "comment", length = 5000)
  public String getComment()
  {
    return comment;
  }

  public AddressDO setComment(String comment)
  {
    this.comment = comment;
    return this;
  }

  @Column(length = 255)
  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  @Column(length = 255)
  public String getWebsite()
  {
    return website;
  }

  public void setWebsite(String website)
  {
    this.website = website;
  }

  @Column(length = 255)
  public String getFingerprint()
  {
    return fingerprint;
  }

  public void setFingerprint(String fingerprint)
  {
    this.fingerprint = fingerprint;
  }

  @Column(name = "first_name", length = 255)
  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  @Transient
  public String getFullName()
  {
    return StringHelper.listToString(", ", name, firstName);
  }

  @Column(length = 255)
  public String getName()
  {
    return name;
  }

  public AddressDO setName(String name)
  {
    this.name = name;
    return this;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "form", length = 10)
  public FormOfAddress getForm()
  {
    return form;
  }

  public void setForm(FormOfAddress form)
  {
    this.form = form;
  }

  @Column(length = 255)
  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization(String organization)
  {
    this.organization = organization;
  }

  @Column(length = 255)
  public String getDivision()
  {
    return division;
  }

  public void setDivision(String division)
  {
    this.division = division;
  }

  @Column(length = 255)
  public String getPositionText()
  {
    return positionText;
  }

  public void setPositionText(String positionText)
  {
    this.positionText = positionText;
  }

  @Column(name = "private_phone", length = 255)
  public String getPrivatePhone()
  {
    return privatePhone;
  }

  public void setPrivatePhone(String privatePhone)
  {
    this.privatePhone = privatePhone;
  }

  @Column(name = "private_mobile_phone", length = 255)
  public String getPrivateMobilePhone()
  {
    return privateMobilePhone;
  }

  public void setPrivateMobilePhone(String mobilePhone)
  {
    this.privateMobilePhone = mobilePhone;
  }

  @Column(length = 255, name = "private_addresstext")
  public String getPrivateAddressText()
  {
    return privateAddressText;
  }

  public void setPrivateAddressText(String privateAddressText)
  {
    this.privateAddressText = privateAddressText;
  }

  @Column(name = "private_zip_code", length = 255)
  public String getPrivateZipCode()
  {
    return privateZipCode;
  }

  public void setPrivateZipCode(String zipCode)
  {
    this.privateZipCode = zipCode;
  }

  @Column(length = 255, name = "private_city")
  public String getPrivateCity()
  {
    return privateCity;
  }

  public void setPrivateCity(String city)
  {
    this.privateCity = city;
  }

  @Column(name = "private_country", length = 255)
  public String getPrivateCountry()
  {
    return privateCountry;
  }

  public void setPrivateCountry(String privateCountry)
  {
    this.privateCountry = privateCountry;
  }

  @Column(name = "private_state", length = 255)
  public String getPrivateState()
  {
    return privateState;
  }

  public void setPrivateState(String privateState)
  {
    this.privateState = privateState;
  }

  @Column(length = 255, name = "private_email")
  public String getPrivateEmail()
  {
    return privateEmail;
  }

  public void setPrivateEmail(String email)
  {
    this.privateEmail = email;
  }

  @Column(name = "public_key", length = 5000)
  public String getPublicKey()
  {
    return publicKey;
  }

  public void setPublicKey(String publicKey)
  {
    this.publicKey = publicKey;
  }

  /**
   * Not used as object due to performance reasons.
   * @return
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  public TaskDO getTask()
  {
    return task;
  }

  public void setTask(TaskDO task)
  {
    this.task = task;
  }

  @Transient
  public Integer getTaskId()
  {
    if (this.task == null)
      return null;
    return task.getId();
  }

  @Column(length = 255)
  public String getTitle()
  {
    return title;
  }

  public AddressDO setTitle(String title)
  {
    this.title = title;
    return this;
  }

  /**
   * @return address text of mailing address (in order: postal, default or private address).
   * @see #hasPostalAddress()
   * @see #hasDefaultAddress()
   */
  @Transient
  public String getMailingAddressText()
  {
    if (hasPostalAddress() == true) {
      return getPostalAddressText();
    } else if (hasDefaultAddress() == true) {
      return getAddressText();
    } else {
      return getPrivateAddressText();
    }
  }

  /**
   * @return zip code of mailing address (in order: postal, default or private address).
   * @see #hasPostalAddress()
   * @see #hasDefaultAddress()
   */
  @Transient
  public String getMailingZipCode()
  {
    if (hasPostalAddress() == true) {
      return getPostalZipCode();
    } else if (hasDefaultAddress() == true) {
      return getZipCode();
    } else {
      return getPrivateZipCode();
    }
  }

  /**
   * @return city of mailing address (in order: postal, default or private address).
   * @see #hasPostalAddress()
   * @see #hasDefaultAddress()
   */
  @Transient
  public String getMailingCity()
  {
    if (hasPostalAddress() == true) {
      return getPostalCity();
    } else if (hasDefaultAddress() == true) {
      return getCity();
    } else {
      return getPrivateCity();
    }
  }

  /**
   * @return country of mailing address (in order: postal, default or private address).
   * @see #hasPostalAddress()
   * @see #hasDefaultAddress()
   */
  @Transient
  public String getMailingCountry()
  {
    if (hasPostalAddress() == true) {
      return getPostalCountry();
    } else if (hasDefaultAddress() == true) {
      return getCountry();
    } else {
      return getPrivateCountry();
    }
  }

  /**
   * @return state of mailing address (in order: postal, default or private address).
   * @see #hasPostalAddress()
   * @see #hasDefaultAddress()
   */
  @Transient
  public String getMailingState()
  {
    if (hasPostalAddress() == true) {
      return getPostalState();
    } else if (hasDefaultAddress() == true) {
      return getState();
    } else {
      return getPrivateState();
    }
  }

  /**
   * @return true, if postal addressText, zip code, city or country is given.
   */
  @Transient
  public boolean hasPostalAddress()
  {
    return StringHelper.isNotBlank(getPostalAddressText(), getPostalZipCode(), getPostalCity(), getPostalCountry());
  }

  /**
   * @return true, if default addressText, zip code, city or country is given.
   */
  @Transient
  public boolean hasDefaultAddress()
  {
    return StringHelper.isNotBlank(getAddressText(), getZipCode(), getCity(), getCountry());
  }

  /**
   * @return true, if private addressText, zip code, city or country is given.
   */
  @Transient
  public boolean hasPrivateAddress()
  {
    return StringHelper.isNotBlank(getPrivateAddressText(), getPrivateZipCode(), getPrivateCity(), getPrivateCountry());
  }

  /**
   * List of instant messaging contacts in the form of a property file: {skype=hugo.mustermann\naim=12345dse}. Only for data base access,
   * use getter an setter of instant messaging instead.
   * @return
   */
  // @Column(name = "instant_messaging", length = 4000)
  @Transient
  // TODO: Prepared for data base persistence.
  public String getInstantMessaging4DB()
  {
    return getInstantMessagingAsString(instantMessaging);
  }

  public void setInstantMessaging4DB(String properties)
  {
    if (StringUtils.isBlank(properties) == true) {
      this.instantMessaging = null;
    } else {
      StringTokenizer tokenizer = new StringTokenizer(properties, "\n");
      while (tokenizer.hasMoreTokens() == true) {
        String line = tokenizer.nextToken();
        if (StringUtils.isBlank(line) == true) {
          continue;
        }
        int idx = line.indexOf('=');
        if (idx <= 0) {
          log.error("Wrong instant messaging entry format in data base: " + line);
          continue;
        }
        String label = line.substring(0, idx);
        String value = "";
        if (idx < line.length()) {
          label = line.substring(idx);
        }
        InstantMessagingType type = null;
        try {
          type = InstantMessagingType.get(label);
        } catch (Exception ex) {
          log.error("Ignoring unknown InstantMessagingType: " + label, ex);
          continue;
        }
        setInstantMessaging(type, value);
      }
    }
  }

  /**
   * Instant messaging settings as property file.
   * @return
   */
  @Transient
  public List<LabelValueBean<InstantMessagingType, String>> getInstantMessaging()
  {
    return instantMessaging;
  }

  public void setInstantMessaging(InstantMessagingType type, String value)
  {
    if (this.instantMessaging == null) {
      this.instantMessaging = new ArrayList<LabelValueBean<InstantMessagingType, String>>();
    } else {
      for (LabelValueBean<InstantMessagingType, String> entry : this.instantMessaging) {
        if (entry.getLabel() == type) {
          // Entry found;
          if (StringUtils.isBlank(value) == true) {
            // Remove this entry:
            this.instantMessaging.remove(entry);
          } else {
            // Modify existing entry:
            entry.setValue(value);
          }
          return;
        }
      }
    }
    this.instantMessaging.add(new LabelValueBean<InstantMessagingType, String>(type, value));
  }

  /**
   * Used for representation in the data base and for hibernate search (lucene).
   */
  static String getInstantMessagingAsString(List<LabelValueBean<InstantMessagingType, String>> list)
  {
    if (list == null || list.size() == 0) {
      return null;
    }
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for (LabelValueBean<InstantMessagingType, String> lv : list) {
      if (StringUtils.isBlank(lv.getValue()) == true) {
        continue; // Do not write empty entries.
      }
      if (first == true)
        first = false;
      else buf.append("\n");
      buf.append(lv.getLabel()).append("=").append(lv.getValue());
    }
    if (first == true) {
      return null; // No entry was written.
    }
    return buf.toString();
  }
}
