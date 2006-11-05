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

import org.ujac.ui.editor.TextArea;

/**
 * Name: UpKeyAction<br>
 * Description: A class handling the up key action (UP).
 * <br>Log: $Log: UpKeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class UpKeyAction extends KeyAction {

  /**
   * Constructs a UpKeyAction instance with no specific arguments.
   * @param textArea The text area.
   */
  public UpKeyAction(TextArea textArea) {
    super(textArea);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    int caret = textArea.getCaretPosition();
    int line = textArea.getCaretLine();

    if (line == 0) {
      textArea.getToolkit().beep();
      return;
    }

    int magic = textArea.getMagicCaretPosition();
    if (magic == -1) {
      magic = textArea.offsetToX(line, caret - textArea.getLineStartOffset(line));
    }

    caret = textArea.getLineStartOffset(line - 1) + textArea.xToOffset(line - 1, magic);
    if (isShiftPressed(e)) {
      textArea.select(textArea.getMarkPosition(), caret);
    } else {
      textArea.setCaretPosition(caret);
    }
    textArea.setMagicCaretPosition(magic);
  }

}
