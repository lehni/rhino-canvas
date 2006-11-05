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
 * Name: PageUpKeyAction<br>
 * Description: A class handling the page up key action (PAGE_UP).
 * <br>Log: $Log: PageUpKeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class PageUpKeyAction extends KeyAction {

  /**
   * Constructs a PageUpKeyAction instance with no specific arguments.
   * @param textArea The text area.
   */
  public PageUpKeyAction(TextArea textArea) {
    super(textArea);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    int oldCaretPosition = textArea.getCaretPosition();
    int caretLine = textArea.getCaretLine();
    int caretColumn = oldCaretPosition - textArea.getLineStartOffset(caretLine);
    int visibleLines = textArea.getVisibleLines();
    caretLine = Math.max(caretLine - visibleLines, 0);
    int newCaretPos = Math.min(textArea.getLineStartOffset(caretLine) + caretColumn,
                               textArea.getLineEndOffset(caretLine));
      
    if (isShiftPressed(e)) {
      textArea.select(textArea.getMarkPosition(), newCaretPos);
    } else {
      textArea.setCaretPosition(newCaretPos);
    }
  }

}
