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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import de.micromata.hibernate.spring.HibernateXmlConverter;

/**
 * 
 * @author Kai Reinhard (k.reinhard@micromata.de)
 *
 */
public class XmlDump
{
  private static final Logger log = Logger.getLogger(XmlDump.class);

  private static final String XML_DUMP_FILENAME = System.getProperty("user.home") + "/tmp/database-dump.xml.gz";

  private HibernateTemplate hibernate;

  protected TransactionTemplate tx;

  public HibernateTemplate getHibernate()
  {
    Validate.notNull(hibernate);
    return hibernate;
  }

  public void setHibernate(HibernateTemplate hibernate)
  {
    this.hibernate = hibernate;
    tx = new TransactionTemplate(new HibernateTransactionManager(hibernate.getSessionFactory()));
  }

  public TransactionTemplate getTx()
  {
    Validate.notNull(tx);
    return tx;
  }

  public void setTx(TransactionTemplate tx)
  {
    this.tx = tx;
  }

  public void restoreDatabase()
  {
    try {
      restoreDatabase(new InputStreamReader(new FileInputStream(XML_DUMP_FILENAME), "utf-8"));
    } catch (UnsupportedEncodingException ex) {
      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
  }

  public void restoreDatabase(Reader reader)
  {
    HibernateXmlConverter converter = new HibernateXmlConverter();
    converter.setHibernate(hibernate);
    try {
      converter.fillDatabaseFromXml(reader);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    } finally {
      IOUtils.closeQuietly(reader);
    }
  }

  public void restoreDatabaseFromClasspathResource(String path, String encoding)
  {
    ClassPathResource cpres = new ClassPathResource(path);
    Reader reader;
    try {
      InputStream in;
      if (path.endsWith(".gz") == true) {
        in = new GZIPInputStream(cpres.getInputStream());
      } else {
        in = cpres.getInputStream();
      }
      reader = new InputStreamReader(in, encoding);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
    restoreDatabase(reader);
  }

  public void dumpDatabase()
  {
    dumpDatabase(XML_DUMP_FILENAME, "utf-8");
  }

  /**
   * 
   * @param filename virtual filename: If the filename suffix is "gz" then the dump will be compressed.
   * @param out
   */
  public void dumpDatabase(String filename, OutputStream out)
  {
    HibernateXmlConverter converter = new HibernateXmlConverter();
    converter.setHibernate(hibernate);
    Writer writer = null;
    GZIPOutputStream gzipOut = null;
    try {
      if (filename.endsWith(".gz") == true) {
        gzipOut = new GZIPOutputStream(out);
        writer = new OutputStreamWriter(gzipOut, "utf-8");
      } else {
        writer = new OutputStreamWriter(out, "utf-8");
      }
      converter.dumpDatabaseToXml(writer, false); // history=false, preserveIds=true
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      IOUtils.closeQuietly(gzipOut);
      IOUtils.closeQuietly(writer);
    }
  }

  public void dumpDatabase(String path, String encoding)
  {
    OutputStream out = null;
    try {
      out = new FileOutputStream(path);
      dumpDatabase(path, out);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
}
