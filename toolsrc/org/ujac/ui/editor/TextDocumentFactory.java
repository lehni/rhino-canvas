/*
 * Copyright (C) 2003 by Christian Lauer.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://sourceforge.net/projects/ujac
 */

package org.ujac.ui.editor;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: TextDocumentFactory<br>
 * Description: A factory, creating TextDocument instances for TextArea.
 * <br>Log: $Log: TextDocumentFactory.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class TextDocumentFactory {

  /** Holds the document type map. */
  private static final Map documentTypes = new HashMap();

  /**
   * No one is allowed to instanciate this class!!
   */
  private TextDocumentFactory() {
  }
	
  /**
   * Registers the given document type.
   * @param documentClass The document class.
   */
  public static final void registerDocumentType(Class documentClass) {
    try {
      TextDocument doc = (TextDocument) documentClass.newInstance();
      documentTypes.put(doc.getMimeType(), documentClass);
    } catch (InstantiationException ex) {
      ex.printStackTrace(); 
    } catch (IllegalAccessException ex) {
      ex.printStackTrace(); 
    }
  }

  /**
   * Unregisters the given document type.
   * @param mimeType The document type.
   */
  public static final void unregisterDocumentType(String mimeType) {
    documentTypes.remove(mimeType);
  }

  /**
   * Unregisters the given document type.
   * @param documentClass The document class.
   */
  public static final void unregisterDocumentType(Class documentClass) {
    try {
      TextDocument doc = (TextDocument) documentClass.newInstance();
      documentTypes.remove(doc.getMimeType());
    } catch (InstantiationException ex) {
      ex.printStackTrace(); 
    } catch (IllegalAccessException ex) {
      ex.printStackTrace(); 
    }
  }

  /**
   * Generates an instance for the given mimeType.
   * @param mimeType The document type.
   * @return The generated instance.
   */
  public static final TextDocument createDocument(String mimeType) {
    try {
      Class cls = (Class) documentTypes.get(mimeType);
      return (TextDocument) cls.newInstance();
    } catch (InstantiationException ex) {
      ex.printStackTrace(); 
    } catch (IllegalAccessException ex) {
      ex.printStackTrace(); 
    }
    return null;
  }
  
  static {
    // registering default document types
    registerDocumentType(PlainTextDocument.class);
    registerDocumentType(XmlTextDocument.class);
  }
}
