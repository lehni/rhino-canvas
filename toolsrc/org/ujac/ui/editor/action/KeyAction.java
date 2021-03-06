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
import java.awt.event.InputEvent;

import org.ujac.ui.editor.TextArea;


/**
 * Name: KeyAction<br>
 * Description: The base class for key event handling actions.
 * <br>Log: $Log: KeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public abstract class KeyAction extends EditorAction {

  /**
   * Constructs a KeyAction instance with no specific arguments.
   * @param textArea
   */
  public KeyAction(TextArea textArea) {
    super(textArea);
  }
	
  /**
   * Checks whether the shift key is pressed or not.
   * @param evt The action event.
   * @return true, if shift key is pressed, else false.
   */
  boolean isShiftPressed(ActionEvent evt) {
    return (evt.getModifiers() & InputEvent.SHIFT_MASK) > 0;
  }

  /**
   * Checks whether the alt key is pressed or not.
   * @param evt The action event.
   * @return true, if alt key is pressed, else false.
   */
  boolean isAltPressed(ActionEvent evt) {
    return (evt.getModifiers() & InputEvent.ALT_MASK) > 0;
  }

  /**
   * Checks whether the ctrl key is pressed or not.
   * @param evt The action event.
   * @return true, if ctrl key is pressed, else false.
   */
  boolean isCtrlPressed(ActionEvent evt) {
    return (evt.getModifiers() & InputEvent.CTRL_MASK) > 0;
  }
}
