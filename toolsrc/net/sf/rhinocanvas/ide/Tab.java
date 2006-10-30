/*
 * Created on 28/10/2006 by Stefan Haustein
 */
package net.sf.rhinocanvas.ide;

import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;

import org.ujac.ui.editor.TextArea;

public class Tab {

	String title;
	File file;
	TextArea editor;
	boolean changed;
	static int unnamedCount;
	
	Tab(File file){
		this.file = file;
		editor = new TextArea();
		editor.setDocument(new JsDocument());
		editor.setLineHighlightEnabled(true);
		
		if(file == null){
			title = "scratch-"+(++unnamedCount)+".js";
		}
		else{
			title = file.getName();
			
			try{
				FileReader r = new FileReader(file);
				StringWriter sw = new StringWriter();
				char[] buf = new char[16384];
				
				while(true){
					int count = r.read(buf);
					if(count <= 0) break;
					sw.write(buf, 0, count);
				}
				
				editor.setText(sw.toString());
			}
			catch(IOException e){
				throw new RuntimeException(e);
			}
			
		}
	}

	
}
