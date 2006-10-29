/*
 * Created on 28/10/2006 by Stefan Haustein
 */
package net.sf.rhinocanvas.ide;

import java.awt.Component;
import java.io.File;
import java.net.URI;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;

public class Tab {

	String title;
	File file;
	JEditorPane editor;
	static int unnamedCount;
	
	Tab(File file){
		this.file = file;
		if(file == null){
			title = "new-"+(++unnamedCount)+".js";
			editor = new JEditorPane();
		}
		else{
			title = file.getName();
			
			try {
				String fileURI = new URI("file", null, file.getAbsolutePath(), null).toString();
				System.out.println("opening: "+fileURI);
					
				editor = new JEditorPane(fileURI);
			} catch (Exception e) {
					
				throw new RuntimeException(e);
			} 
		}
	}

	
}
