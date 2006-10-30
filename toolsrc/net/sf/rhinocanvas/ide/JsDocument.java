/*
 * Copyright (C) 2006 by Strefan Haustein.
 *
 */

package net.sf.rhinocanvas.ide;

import javax.swing.text.Segment;
import org.ujac.ui.editor.SyntaxUtilities;
import org.ujac.ui.editor.TextArea;
import org.ujac.ui.editor.TextDocument;
import org.ujac.ui.editor.Token;

/**
 * Javascript Syntax highlighting hack
 */
public class JsDocument extends TextDocument {

  static final String[] KEYWORDS = {
	  "function", "this", "new", "var", "if", "for", "do", "while", "else"
  };
	
  /**
   * Gets the document's mime type
   * @return The document's mime type.
   */
  public String getMimeType() {
    return "application/x-javascript";
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
    
    token = token == Token.COMMENT1 ? token : Token.INTERNAL_FIRST;

    for (int i = offset; i < length; i++) {
      int ip1 = i + 1;
      char c = array[i];
      sw:
      switch (token) {
      
      
        case Token.INTERNAL_FIRST : // text
        	for(int k = 0; k < KEYWORDS.length; k++){
        		int len = KEYWORDS[k].length();
        		if (SyntaxUtilities.regionMatches(false, line, i, KEYWORDS[k])
        				&& (i+len >= array.length || !Character.isJavaIdentifierPart(array[i+len])))
        		 {
        			addToken(i - lastOffset, Token.NULL);
                    lastOffset = i;
        			i += KEYWORDS[k].length();
//        			switch(k){
//        			case 0: 
//        			case 1: token = Token.KEYWORD3; break;
//        			case 2:
//        			case 3: token = Token.KEYWORD2; break;
//        			default:
        				token = Token.KEYWORD1;
//        			}
        			addToken(i - lastOffset, token);
                    lastOffset = i;
                    token = Token.NULL;
        			break sw;
        		}
        	}
        	
        case Token.NULL:
          switch (c) {
            case '/' :
              addToken(i - lastOffset, Token.NULL);
              lastOffset = i;
              if (array[ip1] == '/') {
                i += 1;
                token = Token.COMMENT2;
                break;
              } else if (array[ip1] == '*') {
                  i += 1;
                  token = Token.COMMENT1;
                  break;
              }

            case '(':
            case ')':
            case '<':
            case '>':
            case '{':
            case '}':
            case '[':
            case ']':
            case '&':
            case '!':
            case '|':
            case '+':
            case '-':
            case '?':
            case ':':
            case '=':
            case '*':
            case ',':
            case ';':
              addToken(ip1 - lastOffset, Token.NULL);
             // lastOffset = i;
//              addToken(1, Token.NULL);
              lastOffset = ip1;
              token = Token.INTERNAL_FIRST;
              break;
              
            case '\'':
            case '\"':
            	 addToken(i - lastOffset, Token.NULL);
                 lastOffset = i;
                 token = c == '\'' ? Token.LITERAL2 : Token.LITERAL1;
                 break;
                
            case ' ':
            case '\t':
            	token = Token.INTERNAL_FIRST;
                 
            default:
            	
          }
          break;
          

        case Token.LITERAL1 :
        case Token.LITERAL2 : // strings
          if ((token == Token.LITERAL1 && c == '\"') || (token == Token.LITERAL2 && c == '\'')) {
            addToken(ip1 - lastOffset, token);
            lastOffset = ip1;
            token = Token.INTERNAL_FIRST;
          }
          break;

        case Token.COMMENT1 : // Inside a comment
          if (SyntaxUtilities.regionMatches(false, line, i, "*/")) {
            addToken((i + 2) - lastOffset, token);
            lastOffset = i + 2;
            token = Token.INTERNAL_FIRST;
          }
          break;

        case Token.COMMENT2 : // Inside a declaration
          if (c == '\n') {
            addToken(ip1 - lastOffset, token);
            lastOffset = ip1;
            token = Token.INTERNAL_FIRST;
          }
          break;

      }
    }


        addToken(length - lastOffset, token == Token.INTERNAL_FIRST ? Token.NULL : token);



    
    return token == Token.COMMENT1 ? token : Token.NULL;
  }


}
