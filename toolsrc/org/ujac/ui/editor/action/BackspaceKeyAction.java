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

package org.ujac.ui.editor.action;

import java.awt.event.ActionEvent;

import javax.swing.text.BadLocationException;

import org.ujac.ui.editor.TextArea;
import org.ujac.ui.editor.TextUtilities;

/**
 * Name: BackspaceKeyAction<br>
 * Description: A class handling the backspace key action (CTRL+C).
 * <br>Log: $Log: BackspaceKeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.2  2004/07/02 23:41:01  lauerc
 * <br>Log: Applied performance improvements.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class BackspaceKeyAction extends KeyAction {

  /**
   * Constructs a BackspaceKeyAction instance with specific arguments.
   * @param textArea The text area.
   */
  public BackspaceKeyAction(TextArea textArea) {
    super(textArea);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
   
    if (isCtrlPressed(e)) {
      int start = textArea.getSelectionStart();
      if (start != textArea.getSelectionEnd()) {
        textArea.insertText("");
      }

      int line = textArea.getCaretLine();
      int lineStart = textArea.getLineStartOffset(line);
      int caret = start - lineStart;

      String lineText = textArea.getLineText(textArea.getCaretLine());

      if (caret == 0) {
        if (lineStart == 0) {
          textArea.getToolkit().beep();
          return;
        }
        caret--;
      } else {
        String noWordSep = (String) textArea.getDocument().getProperty("noWordSep");
        caret = TextUtilities.findWordStart(lineText, caret, noWordSep);
      }

      try {
        textArea.getDocument().remove(caret + lineStart, start - (caret + lineStart), true);
      } catch (BadLocationException bl) {
        bl.printStackTrace();
      }
    } else {
      if (!textArea.isEditable()) {
        textArea.getToolkit().beep();
        return;
      }

      if (textArea.getSelectionStart() != textArea.getSelectionEnd()) {
        textArea.insertText("");
      } else {
        int caret = textArea.getCaretPosition();
        if (caret == 0) {
          textArea.getToolkit().beep();
          return;
        }
        try {
          textArea.getDocument().remove(caret - 1, 1, true);
        } catch (BadLocationException bl) {
          bl.printStackTrace();
        }
      }
    }
  }
}
