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
import javax.swing.text.BadLocationException;

/**
 * Name: XmlTextDocument<br>
 * Description: A class handling XML documents.
 * <br>Log: $Log: XmlTextDocument.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class XmlTextDocument extends TextDocument {

  /**
   * Gets the document's mime type
   * @return The document's mime type.
   */
  public String getMimeType() {
    return "text/xml";
  }

  /**
   * Performs the content assist command.
   * @param textArea The text area the command was invoked at.
   */
  public void contentAssist(TextArea textArea) {
    
  }

  /**
   * Marks the tokens.
   * @param token The current token.
   * @param line The current line.
   * @param lineIndex The index of the current line.
   * @return The new index.
   */
  public byte markTokens(byte token, Segment line, int lineIndex) {
    char[] array = line.array;
    int offset = line.offset;
    int lastOffset = offset;
    int length = line.count + offset;

    // Ugly hack to handle multi-line tags
    boolean sk1 = token == Token.KEYWORD1;

    for (int i = offset; i < length; i++) {
      int ip1 = i + 1;
      char c = array[i];
      switch (token) {
        case Token.NULL : // text
          switch (c) {
            case '<' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              if (SyntaxUtilities.regionMatches(false, line, ip1, "!--")) {
                i += 3;
                token = Token.COMMENT1;
              } else if (array[ip1] == '!') {
                i += 1;
                token = Token.COMMENT2;
              } else if (array[ip1] == '?') {
                i += 1;
                token = Token.KEYWORD3;
              } else
                token = Token.KEYWORD1;
              break;

            case '&' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              token = Token.LABEL;
              break;
          }
          break;

        case Token.KEYWORD1 : // tag
          switch (c) {
            case '>' :
              addToken(ip1 - lastOffset, token);
              lastOffset = ip1;
              token = Token.NULL;
              sk1 = false;
              break;

            case ' ' :
            case '\t' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              token = Token.KEYWORD2;
              sk1 = false;
              break;

            default :
              if (sk1) {
                token = Token.KEYWORD2;
                sk1 = false;
              }
              break;
          }
          break;

        case Token.KEYWORD2 : // attribute
          switch (c) {
            case '>' :
              addToken(ip1 - lastOffset, token);
              lastOffset = ip1;
              token = Token.NULL;
              break;

            case '/' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              token = Token.KEYWORD1;
              break;

            case '=' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              token = Token.OPERATOR;
          }
          break;

        case Token.OPERATOR : // equal for attribute
          switch (c) {
            case '\"' :
            case '\'' :
              addToken(i - lastOffset, token);
              lastOffset = i;
              if (c == '\"')
                token = Token.LITERAL1;
              else
                token = Token.LITERAL2;
              break;
          }
          break;

        case Token.LITERAL1 :
        case Token.LITERAL2 : // strings
          if ((token == Token.LITERAL1 && c == '\"') || (token == Token.LITERAL2 && c == '\'')) {
            addToken(ip1 - lastOffset, token);
            lastOffset = ip1;
            token = Token.KEYWORD1;
          }
          break;

        case Token.LABEL : // entity
          if (c == ';') {
            addToken(ip1 - lastOffset, token);
            lastOffset = ip1;
            token = Token.NULL;
            break;
          }
          break;

        case Token.COMMENT1 : // Inside a comment
          if (SyntaxUtilities.regionMatches(false, line, i, "-->")) {
            addToken((i + 3) - lastOffset, token);
            lastOffset = i + 3;
            token = Token.NULL;
          }
          break;

        case Token.COMMENT2 : // Inside a declaration
          if (SyntaxUtilities.regionMatches(false, line, i, ">")) {
            addToken(ip1 - lastOffset, token);
            lastOffset = ip1;
            token = Token.NULL;
          }
          break;

        case Token.KEYWORD3 : // Inside a processor instruction
          if (SyntaxUtilities.regionMatches(false, line, i, "?>")) {
            addToken((i + 2) - lastOffset, token);
            lastOffset = i + 2;
            token = Token.NULL;
          }
          break;

        default :
          throw new InternalError("Invalid state: " + token);
      }
    }

    switch (token) {
      case Token.LABEL :
        addToken(length - lastOffset, Token.INVALID);
        token = Token.NULL;
        break;

      default :
        addToken(length - lastOffset, token);
        break;
    }

    return token;
  }

  /**
   * Tries to get the encoding defined in the XML file. If not found,
   * null is returned.
   */
  public String getEncoding() {
    Segment seg = new Segment();
    int length = getLength();
    try {
      getText(0, length, seg);
    } catch (BadLocationException e) {
      return null;
    }

    StringBuffer encoding = new StringBuffer(10);

    // check if the document starts with '<?xml'
    if (!isWord("<?xml", seg.array, 0, seg.count)) {
      return null;
    }

    // skip whitespace
    int r = 5;
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    // now the word version should follow
    if (!isWord("version", seg.array, r, seg.count)) {
      return null;
    }

    r += "version".length();

    // skip whitespace
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    // now we should have '='
    if (!isWord("=", seg.array, r, seg.count)) {
      return null;
    }

    r++;

    // skip whitespace
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    char q = seg.array[r];
    if (q != '"' && q != '\'') {
      return null;
    }

    r++;

    // now we should have 1.0
    if (!isWord("1.0" + q, seg.array, r, seg.count)) {
      return null;
    }

    r += 4;

    // skip whitespace
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    // now we should have 'encoding'
    if (!isWord("encoding", seg.array, r, seg.count)) {
      return null;
    }

    r += "encoding".length();

    // skip whitespace
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    // now we should have '='
    if (!isWord("=", seg.array, r, seg.count)) {
      return null;
    }

    r++;

    // skip whitespace
    if ((r = skipWhiteSpace(seg.array, r, seg.count)) == -1) {
      return null;
    }

    q = seg.array[r];
    if (q != '"' && q != '\'') {
      return null;
    }

    r++;

    int m = r;
    while (m < seg.count && seg.array[m] != q) {
      if ((seg.array[m] >= 'a' && seg.array[m] <= 'z')
        || (seg.array[m] >= 'A' && seg.array[m] <= 'Z')
        || (seg.array[m] >= '0' && seg.array[m] <= '9')
        || seg.array[m] == '.'
        || seg.array[m] == '-'
        || seg.array[m] == '_') {
        encoding.append(seg.array[m]);
        m++;
      } else {
        return null;
      }
    }

    if (m < seg.count && seg.array[m] == q) {
      return encoding.toString();
    }

    return null;
  }

  private boolean isXmlSpace(char c) {
    if (c == ' ' || c == '\t') {
      return true;
    } else {
      return false;
    }
  }

  private boolean isWord(String word, char[] text, int offset, int count) {
    for (int i = 0; i < word.length(); i++) {
      if (offset + i >= count) {
        return false;
      }

      if (!(text[offset + i] == word.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private int skipWhiteSpace(char[] text, int offset, int count) {
    int i = offset;
    while (isXmlSpace(text[i])) {
      i++;
      if (i >= count) {
        return -1;
      }
    }
    return i;
  }
}
