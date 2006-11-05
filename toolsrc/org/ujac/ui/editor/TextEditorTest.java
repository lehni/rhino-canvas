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

import java.awt.Insets;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Name: TextEditorTest<br>
 * Description: A test bed for the TextArea component.
 * <br>Log: $Log: TextEditorTest.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class TextEditorTest extends JFrame {

  TextArea editor = null;
  //SimpleTextEditor editor = null;
  JScrollPane editorScrollPane = null;
  JLabel positionLabel = null;
  JLabel positionText = null;

  class FieldFocusListener implements FocusListener {

    /* (non-Javadoc)
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
      //System.out.println("focusGained: " + e.getComponent().getName());
    }

    /* (non-Javadoc)
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {
      //System.out.println("focusLost: " + e.getComponent().getName());
    }

  }

  public TextEditorTest() {
    Container contentPane = this.getContentPane();
    contentPane.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    this.editor = new TextArea("text/xml");
    //this.editor.setFont(new Font("Helvetica", Font.PLAIN, 18));
    editorScrollPane = new JScrollPane();
    this.editorScrollPane.setViewportView(editor);
    this.editor.setName("text-editor");
    this.editor.addFocusListener(new FieldFocusListener());
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    contentPane.add(editorScrollPane, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    this.positionLabel = new JLabel("Pos:");
    this.positionLabel.setName("positionLabel");
    contentPane.add(positionLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(0, 2, 0, 2);
    gbc.anchor = GridBagConstraints.WEST;
    this.positionText = new JLabel();
    this.positionText.setName("positionText");
    contentPane.add(positionText, gbc);

    editor.addCaretPositionListener(new CaretPositionListener() {
      public void positionUpdate(CaretPositionEvent evt) {
        positionText.setText((evt.getRow() + 1) + "," + (evt.getColumn() + 1));
      }
    });

    this.setBounds(100, 100, 300, 150);

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        System.exit(0);
      }
    });
  }
  
  private void loadFile(String fileName) throws IOException {
    StringBuffer text = new StringBuffer();
    BufferedReader textReader = new BufferedReader(new FileReader(fileName));
    String line = null;
    while ((line = textReader.readLine()) != null) {
      text.append(line);
      text.append("\n");
    }
    editor.setText(text.toString());
  }

  public static void main(String[] args) {
    TextEditorTest testWindow = new TextEditorTest();
    try {
      if (args.length > 0) {
        testWindow.loadFile(args[0]);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    testWindow.show();
  }
}
