/*
 * Copyright (C) 2003 by Christian Lauer.
 *
 * The implementation of this class is based upon a early implementation 
 * of the class package org.gjt.sp.jedit.Buffer from the jEdit project 
 * (www.jedit.org) which was originally written by Slava Pestov. 
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

import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

import org.ujac.ui.editor.action.RedoKeyAction;
import org.ujac.ui.editor.action.UndoKeyAction;


/**
 * Name: TextDocument<br>
 * Description: Base class for text documents.
 * <br>Log: $Log: TextDocument.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.3  2004/07/05 18:26:21  lauerc
 * <br>Log: Fixed bug in method getLineLength.
 * <br>Log:
 * <br>Log: Revision 1.2  2004/07/02 23:40:50  lauerc
 * <br>Log: Applied performance improvements.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/01/20 20:29:56  lauerc
 * <br>Log: Moved user interface components to sub project UJAC-UI.
 * <br>Log:
 * @author  $Author: haustein $
 * @version $Revision: 1.1 $
 */
public abstract class TextDocument extends PlainDocument {

  /**
   * The first token in the list. This should be used as the return
   * value from <code>markTokens()</code>.
   */
  protected Token firstToken;

  /**
   * The last token in the list. New tokens are added here.
   * This should be set to null before a new line is to be tokenized.
   */
  protected Token lastToken;

  /**
   * An array for storing information about lines. It is enlarged and
   * shrunk automatically by the <code>insertLines()</code> and
   * <code>deleteLines()</code> methods.
   */
  protected LineInfo[] lineInfo;

  /** The length of the longest line. */
  protected int documentWidth = 0;
  /** The last tokenized line. */
  protected int lastLine = -1;

  /** True if the next line should be painted. */
  protected boolean nextLineRequested;

  /** The undo manager. */
  private UndoManager undoManager;
  /** The undo listener. */
  private UndoHandler undoHandler;
  /** The flag indicating whether a replace is currently being performed or not. */
  private boolean performingReplace = false;
  /** The replace edit. */
  private CompoundEdit replaceEdit = null;

  /** The undo action. */
  protected UndoKeyAction undoAction;
  /** The redo action. */
  protected RedoKeyAction redoAction;

  /**
   * Constructs a TextDocument instance with no specific attributes.
   */
  public TextDocument() {
    this.undoManager = new UndoManager();
    this.undoHandler = new UndoHandler();
    this.undoAction = new UndoKeyAction(this);
    this.redoAction = new RedoKeyAction(this);
    insertLines(0, getHeight());
    getDocumentProperties().put(PlainDocument.tabSizeAttribute, new Integer(4));
  }

  /**
   * Gets the document's mime type
   * @return The document's mime type.
   */
  public abstract String getMimeType();

  /**
   * An abstract method that splits a line up into tokens. It
   * should parse the line, and call <code>addToken()</code> to
   * add syntax tokens to the token list. Then, it should return
   * the initial token type for the next line.<p>
   *
   * For example if the current line contains the start of a 
   * multiline comment that doesn't end on that line, this method
   * should return the comment token type so that it continues on
   * the next line.
   *
   * @param token The initial token type for this line
   * @param line The line to be tokenized
   * @param lineIndex The index of the line in the document,
   * starting at 0
   * @return The initial token type for the next line
   */
  protected abstract byte markTokens(byte token, Segment line, int lineIndex);

  /**
   * Connects to undo manager.
   */
  public void startUndo() {
    undoManager.discardAllEdits();
    addUndoableEditListener(undoHandler);
  }

  /**
   * Disconnects from undo manager.
   */
  public void stopUndo() {
    removeUndoableEditListener(undoHandler);
  }

  /**
   * Performs an undo.
   */
  public void undo() {
    if (undoManager.canUndo()) {
      undoManager.undo();
    }
  }

  /**
   * Performs an redo.
   */
  public void redo() {
    if (undoManager.canRedo()) {
      undoManager.redo();
    }
  }

  /**
   * Gets the undo action.
   */
  public UndoKeyAction getUndoAction() {
    return undoAction;
  }

  /**
   * Gets the redo action.
   */
  public RedoKeyAction getRedoAction() {
    return redoAction;
  }

  /**
   * @see javax.swing.text.PlainDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
   */
  public void insertString(int offs, String str, AttributeSet a, boolean calculateWidth) throws BadLocationException {
    synchronized (this) {
      super.insertString(offs, str, a);
    }
    if (calculateWidth) {
      int textLength = str.length();
      Element root = getDefaultRootElement();
      int lineIdx = root.getElementIndex(offs);
      int endIdx = offs + str.length();
      Element lineElement = root.getElement(lineIdx);
      int lineLength = getLineLength(lineElement);
      this.documentWidth = Math.max(documentWidth, lineLength);
      while (lineElement.getEndOffset() < endIdx) {
        lineElement = root.getElement(lineIdx);
        lineLength = getLineLength(lineElement);
        this.documentWidth = Math.max(documentWidth, lineLength);
        ++lineIdx;
      }
    }
  }

  /**
   * Computes the length of the given line.
   * @param lineElement The line to determine the length for.
   * @return The length of the line.
   * @exception BadLocationException In case the requested text location was invalid.
   */
  int getLineLength(Element lineElement) throws BadLocationException {
    String line = this.getText(lineElement.getStartOffset(), lineElement.getEndOffset() - lineElement.getStartOffset() - 1);
    int lineLength = line.length();
    int absoluteLineLength = lineLength;
    int tabWidth = getTabSize();
    for (int i = 0; i < lineLength; i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        absoluteLineLength += tabWidth - 1;
      }
    }
    return absoluteLineLength;
  }
  
  /**
   * @see javax.swing.text.AbstractDocument#remove(int, int)
   */
  public void remove(int offs, int len, boolean calculateWidth) throws BadLocationException {
    synchronized (this) {
      super.remove(offs, len);
    }
    if (calculateWidth) {
      calculateDocumentWidth(offs, len, null);
    }
  }

  /**
   * @see javax.swing.text.AbstractDocument#replace(int, int, java.lang.String, javax.swing.text.AttributeSet)
   */
  public void replace(int offset, int length, String text, AttributeSet attrs, boolean calculateWidth) throws BadLocationException {
    synchronized (this) {
      this.performingReplace = true;
     
      if (length == 0 && (text == null || text.length() == 0)) {
        return;
      }

      writeLock();
      try {
        if (length > 0) {
          remove(offset, length, false);
        }
        if (text != null && text.length() > 0) {
          insertString(offset, text, attrs, false);
        }
      } finally {
        writeUnlock();
      }
      if (calculateWidth) {
        calculateDocumentWidth(offset, length, text);
      }

      this.performingReplace = false;
    }
  }

  /**
   * Calculates the new document width after the performed document manipulation. 
   * @param offset The positiin at which the manipulation has been performed.
   * @param numMarked The number of characters, marked before the manipulation. 
   * @param insertedText The inserted text, this one is null if no insert has been performed.
   */
  private void calculateDocumentWidth(int offset, int numMarked, String insertedText) {
    int tabWidth = getTabSize();
    int width = 0;
    try {
      Element root = getDefaultRootElement();
      int numLines = getHeight();
      for (int i = 0; i < numLines; i++) {
        Element lineElement = root.getElement(i);
        int lineLength = getLineLength(lineElement);
        width = Math.max(width, lineLength);
      }
    } catch (BadLocationException ex) {
      ex.printStackTrace();
    }
    this.documentWidth = width;
  }
  
  /**
   * Reparses the document, by passing all lines to the token
   * marker. This should be called after the document is first
   * loaded.
   */
  public void tokenizeLines() {
    tokenizeLines(0, getDefaultRootElement().getElementCount());
  }

  /**
   * Reparses the document, by passing the specified lines to the
   * token marker. This should be called after a large quantity of
   * text is first inserted.
   * @param start The first line to parse
   * @param len The number of lines, after the first one to parse
   */
  public void tokenizeLines(int start, int len) {
    Segment lineSegment = new Segment();
    Element map = getDefaultRootElement();

    len += start;

    try {
      for (int i = start; i < len; i++) {
        Element lineElement = map.getElement(i);
        int lineStart = lineElement.getStartOffset();
        getText(lineStart, lineElement.getEndOffset() - lineStart - 1, lineSegment);
        markTokens(lineSegment, i);
      }
    } catch (BadLocationException bl) {
      bl.printStackTrace();
    }
  }

  /**
   * We overwrite this method to update the token marker
   * state immediately so that any event listeners get a
   * consistent token marker.
   */
  protected void fireInsertUpdate(DocumentEvent evt) {
    DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
    if (ch != null) {
      insertLines(ch.getIndex() + 1, ch.getChildrenAdded().length - ch.getChildrenRemoved().length);
    }

    super.fireInsertUpdate(evt);
  }

  /**
   * We overwrite this method to update the token marker
   * state immediately so that any event listeners get a
   * consistent token marker.
   */
  protected void fireRemoveUpdate(DocumentEvent evt) {
    DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
    if (ch != null) {
      deleteLines(ch.getIndex() + 1, ch.getChildrenRemoved().length - ch.getChildrenAdded().length);
    }

    super.fireRemoveUpdate(evt);
  }

  /**
   * Gets the tabulator size.
   * @return The number of spaces, represented by a single tab.
   */
  public int getTabSize() {
    return ((Integer) getProperty(PlainDocument.tabSizeAttribute)).intValue();
  }

  /**
   * Gets the number of lines.
   * @return The number of document lines.
   */  
  public int getHeight() {
    return getDefaultRootElement().getElementCount();
  }

  /**
   * Gets the width of the widest line.
   * @return The width of the widest line.
   */  
  public int getWidth() {
    return documentWidth;
  }

  /**
   * Gets the line with the given index.
   * @param lineIdx The index of the line to retrieve.
   * @return The according line.
   * @exception BadLocationException In case the requested text location was invalid.
   */
  public char[] getLine(int lineIdx) throws BadLocationException {
    Element root = getDefaultRootElement();
    LeafElement line = (LeafElement) root.getElement(lineIdx);
    int startOffset = line.getStartOffset();
    int endOffset = line.getEndOffset();
    int lineLength = endOffset - startOffset - 1;
    Segment segment = new Segment();
    getText(startOffset, lineLength, segment);
    if (segment.offset > 0) {
      char[] lineChars = new char[lineLength];
      System.arraycopy(segment.array, segment.offset, 
                       lineChars, 0, lineLength);
      return lineChars; 
    }
    return segment.array;
  }

  /**
   * A wrapper for the lower-level <code>markTokensImpl</code> method
   * that is called to split a line up into tokens.
   * @param line The line
   * @param lineIndex The line number
   */
  public Token markTokens(Segment line, int lineIndex) {
    if (lineIndex >= getHeight()) {
      throw new IllegalArgumentException("Tokenizing invalid line: " + lineIndex);
    }

    lastToken = null;

    LineInfo info = lineInfo[lineIndex];
    LineInfo prev;
    if (lineIndex == 0) {
      prev = null;
    } else {
      prev = lineInfo[lineIndex - 1];
    }

    byte oldToken = info.token;
    byte token = markTokens(prev == null ? Token.NULL : prev.token, line, lineIndex);

    info.token = token;

    /*
     * This is a foul hack. It stops nextLineRequested
     * from being cleared if the same line is marked twice.
     *
     * Why is this necessary? It's all JEditTextArea's fault.
     * When something is inserted into the text, firing a
     * document event, the insertUpdate() method shifts the
     * caret (if necessary) by the amount inserted.
     *
     * All caret movement is handled by the select() method,
     * which eventually pipes the new position to scrollTo()
     * and calls repaint().
     *
     * Note that at this point in time, the new line hasn't
     * yet been painted; the caret is moved first.
     *
     * scrollTo() calls offsetToX(), which tokenizes the line
     * unless it is being called on the last line painted
     * (in which case it uses the text area's painter cached
     * token list). What scrollTo() does next is irrelevant.
     *
     * After scrollTo() has done it's job, repaint() is
     * called, and eventually we end up in paintLine(), whose
     * job is to paint the changed line. It, too, calls
     * markTokens().
     *
     * The problem was that if the line started a multiline
     * token, the first markTokens() (done in offsetToX())
     * would set nextLineRequested (because the line end
     * token had changed) but the second would clear it
     * (because the line was the same that time) and therefore
     * paintLine() would never know that it needed to repaint
     * subsequent lines.
     *
     * This bug took me ages to track down, that's why I wrote
     * all the relevant info down so that others wouldn't
     * duplicate it.
     */
    if (!(lastLine == lineIndex && nextLineRequested)) {
      nextLineRequested = (oldToken != token);
    }

    lastLine = lineIndex;

    addToken(0, Token.END);

    return firstToken;
  }

  /**
   * Informs the token marker that lines have been inserted into
   * the document. This inserts a gap in the <code>lineInfo</code>
   * array.
   * @param index The first line number
   * @param lines The number of lines 
   */
  public void insertLines(int index, int lines) {
    if (lines <= 0) {
      return;
    }
    //length += lines;
    ensureCapacity(getHeight());
    int len = index + lines;
    System.arraycopy(lineInfo, index, lineInfo, len, lineInfo.length - len);

    for (int i = index + lines - 1; i >= index; i--) {
      lineInfo[i] = new LineInfo();
    }
  }

  /**
   * Informs the token marker that line have been deleted from
   * the document. This removes the lines in question from the
   * <code>lineInfo</code> array.
   * @param index The first line number
   * @param lines The number of lines
   */
  public void deleteLines(int index, int lines) {
    if (lines <= 0) {
      return;
    }
    int len = index + lines;
    //length -= lines;
    System.arraycopy(lineInfo, len, lineInfo, index, lineInfo.length - len);
  }

  /**
   * Returns true if the next line should be repainted. This
   * will return true after a line has been tokenized that starts
   * a multiline token that continues onto the next line.
   */
  public boolean isNextLineRequested() {
    return nextLineRequested;
  }
  
  /**
   * Ensures that the <code>lineInfo</code> array can contain the
   * specified index. This enlarges it if necessary. No action is
   * taken if the array is large enough already.<p>
   *
   * It should be unnecessary to call this under normal
   * circumstances; <code>insertLine()</code> should take care of
   * enlarging the line info array automatically.
   *
   * @param index The array index
   */
  protected void ensureCapacity(int index) {
    if (lineInfo == null) {
      lineInfo = new LineInfo[index + 1];
    } else if (lineInfo.length <= index) {
      LineInfo[] lineInfoN = new LineInfo[(index + 1) * 2];
      System.arraycopy(lineInfo, 0, lineInfoN, 0, lineInfo.length);
      lineInfo = lineInfoN;
    }
  }

  /**
   * Adds a token to the token list.
   * @param length The length of the token
   * @param id The id of the token
   */
  protected void addToken(int length, byte id) {
    if (id >= Token.INTERNAL_FIRST && id <= Token.INTERNAL_LAST) {
      throw new InternalError("Invalid id: " + id);
    }

    if (length == 0 && id != Token.END) {
      return;
    }

    if (firstToken == null) {
      firstToken = new Token(length, id);
      lastToken = firstToken;
    } else if (lastToken == null) {
      lastToken = firstToken;
      firstToken.length = length;
      firstToken.id = id;
    } else if (lastToken.next == null) {
      lastToken.next = new Token(length, id);
      lastToken = lastToken.next;
    } else {
      lastToken = lastToken.next;
      lastToken.length = length;
      lastToken.id = id;
    }
  }

  /**
   * Inner class for storing information about tokenized lines.
   */
  public class LineInfo {
    
    /** The id of the last token of the line. */
    public byte token;

    /**
     * This is for use by the token marker implementations
     * themselves. It can be used to store anything that
     * is an object and that needs to exist on a per-line
     * basis.
     */
    public Object obj;

    /**
     * Creates a new LineInfo object with token = Token.NULL
     * and obj = null.
     */
    public LineInfo() {
    }

    /**
     * Creates a new LineInfo object with the specified
     * parameters.
     */
    public LineInfo(byte token, Object obj) {
      this.token = token;
      this.obj = obj;
    }
  }

  /**
   * Properly handles undo events regarding replace actions.
   */
  private class UndoHandler implements UndoableEditListener {

    /**
     * @see javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing.event.UndoableEditEvent)
     */
    public void undoableEditHappened(UndoableEditEvent e) {
      DefaultDocumentEvent edit = (DefaultDocumentEvent) e.getEdit(); 
      if (performingReplace) {
        if (replaceEdit == null) {
          if (edit.getType() != DefaultDocumentEvent.EventType.REMOVE) {
            // has to be a remove edit.
            undoManager.addEdit(edit); 
            return;
          }
          // creating compound edit for replace action
          replaceEdit = new CompoundEdit();
          // adding remove edit
          replaceEdit.addEdit(edit);
        } else {
          if (edit.getType() != DefaultDocumentEvent.EventType.INSERT) {
            // has to be a remove edit.
            undoManager.addEdit(edit); 
            return;
          }
          // adding insert edit
          replaceEdit.addEdit(edit);
          // closing compound edit
          replaceEdit.end();
          // adding replace edit to real undo manager.
          undoManager.addEdit(replaceEdit);
          // resetting replace edit
          replaceEdit = null;
        }
      } else {
        // passing the edit to the real undo manager.
        undoManager.addEdit(edit); 
      }
    }
  }

  /**
   * Performs the content assist command.
   * @param textArea The text area the command was invoked at.
   */
  public void contentAssist(TextArea textArea) {
  }
}
