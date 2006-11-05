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

import javax.swing.text.Segment;

/**
 * Name: PlainTextDocument<br>
 * Description: A class handling plain documents.
 * <br>Log: $Log: PlainTextDocument.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class PlainTextDocument extends TextDocument {

  /**
   * Gets the document's mime type
   * @return The document's mime type.
   */
  public String getMimeType() {
    return "text/plain";
  }

  /**
   * Marks the tokens.
   * @param token The current token.
   * @param line The current line.
   * @param lineIndex The index of the current line.
   * @return The new index.
   */
  protected byte markTokens(byte token, Segment line, int lineIndex) {
    int offset = line.offset;
    int lastOffset = offset;
    int length = line.count + offset;
    addToken(length - lastOffset, token);
    return token;
  }

}
