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
 * Name: GotoLineKeyAction<br>
 * Description: A class handling the 'goto line' key action (CTRL+L).
 * <br>Log: $Log: GotoLineKeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.2  2004/07/03 08:43:33  lauerc
 * <br>Log: Removed unnecessary imports.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/07/03 00:58:27  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class GotoLineKeyAction extends KeyAction {

  /**
   * Constructs a GotoLineKeyAction instance with specific arguments.
   * @param textArea The text area.
   */
  public GotoLineKeyAction(TextArea textArea) {
    super(textArea);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    textArea.showGotoLineDialog();
  }

}
