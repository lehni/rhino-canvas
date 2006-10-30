/*
 * Created on 28/10/2006 by Stefan Haustein
 */
package net.sf.rhinocanvas.ide;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.ContextFactory.Listener;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.ConsoleTextArea;

import org.mozilla.javascript.tools.shell.Main;
import org.ujac.ui.editor.CaretPositionEvent;
import org.ujac.ui.editor.CaretPositionListener;


public class IDE  {
	
	static int runNumber;

	class ReflectiveAction extends AbstractAction{

		Method method;
		
		ReflectiveAction(String label, String methodName){
			super(label);
			
			
			try {
				method = IDE.class.getMethod(methodName, null);
			} catch (Exception e) {
				
				throw new RuntimeException(e);
			} 
		}
		
		public void actionPerformed(ActionEvent ae) {
			try {
				method.invoke(IDE.this, null);
			} catch (Exception e) {
				
				throw new RuntimeException(e);
			} 
		}
		Action setMnemonic(int m){
			putValue(Action.MNEMONIC_KEY, new Integer(m));
			return this;
		}
	}
	
	File propertyFile = new File(System.getProperty("user.home"), ".rhino-canvas-ide.ini");
	Vector tabs = new Vector();
	JFrame frame = new JFrame("Rhino Canvas 'IDE' :)");
	JTabbedPane tabPane = new JTabbedPane();
	JFileChooser fileChooser = new JFileChooser();
	JLabel status = new JLabel();
	ConsoleTextArea console = new ConsoleTextArea(new String[0]);
	
	JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabPane, new JScrollPane(console));
	Properties properties = new Properties();

	

	class Executor implements ContextAction, Runnable {
		String scriptText;
		Context context;
		int timeOut;
		int run;
		
		Executor(String scriptText){
			this.scriptText = scriptText;
		}
		
		Executor(String scriptText, Context context, int timeout){
			this(scriptText);
			this.context = context;
			this.timeOut = timeout;		
			this.run = runNumber;
		}
		
		public void run(){
			
			try {
				Thread.sleep(timeOut);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				throw new RuntimeException(e);
			}
			if(run == runNumber){
				run(Context.enter());
			}
		}
		
		public Object run(Context cx) {
			
			 Script script = cx.compileString(scriptText ,
                    "<command>", 1, null);
			 
			 Object result = Main.evaluateScript(script, cx, Main.getGlobal());
			 PrintStream ps = Main.getGlobal().getErr();
			 
			 if (result != Context.getUndefinedValue()) {
		            try {
		            	ps.println();
		                ps.println(Context.toString(result));
		                ps.print("js> ");
		                ps.flush();
		            } catch (RhinoException rex) {
		                ToolErrorReporter.reportException(
		                    cx.getErrorReporter(), rex);
		            }
		        }
			 
			 
			 return result;
		 }
	}
	

	
	void exec(String scriptText){
	
		Main.shellContextFactory.call(new Executor(scriptText));
        
	}
	
	
	IDE(){
		try {
			properties.load(new FileInputStream(propertyFile));
			
			String cwd = properties.getProperty("lru-1");
			if(cwd != null){
				fileChooser.setCurrentDirectory(new File(cwd).getParentFile());
			}
		} 
		catch(FileNotFoundException e){
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
		
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu runMenu = new JMenu("Run");
		JMenu helpMenu = new JMenu("Help");
		
		
		menu.add(fileMenu);
		
		fileMenu.setMnemonic('F');
		runMenu.setMnemonic('R');
		
		fileMenu.add(new ReflectiveAction("New", "actionNew").setMnemonic(KeyEvent.VK_N));
		fileMenu.add(new ReflectiveAction("Open File...", "actionOpen"));
		fileMenu.addSeparator();
		fileMenu.add(new ReflectiveAction("Close", "actionClose").setMnemonic(KeyEvent.VK_C));
		fileMenu.addSeparator();
		fileMenu.add(new ReflectiveAction("Save", "actionSave").setMnemonic(KeyEvent.VK_S));
		fileMenu.add(new ReflectiveAction("Save as...", "actionSaveAs").setMnemonic(KeyEvent.VK_A));
		fileMenu.addSeparator();

		
		for(int i = 1;; i++){
			String path = properties.getProperty("lru-"+i);
			if(path == null) {
				if(i > 1){
					fileMenu.addSeparator();
				}
				break;
			}
			File f = new File(path);
			Action a = new AbstractAction(""+i+". "+f.getName()){

				public void actionPerformed(ActionEvent e) {
					addTab(new File((String) getValue(SHORT_DESCRIPTION)));
				}
				
			};
			
			a.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_0+i));
			a.putValue(Action.SHORT_DESCRIPTION, path);
			fileMenu.add(a);
		}
		
		fileMenu.add(new ReflectiveAction("Exit", "actionExit").setMnemonic(KeyEvent.VK_X));
		
		menu.add(runMenu);
		runMenu.add(new ReflectiveAction("Run", "actionRun").setMnemonic(KeyEvent.VK_R));
		runMenu.add(new ReflectiveAction("Terminate", "actionTerminate").setMnemonic(KeyEvent.VK_T));
		
		frame.setJMenuBar(menu);
		
		
		
		addTab(null);
//		addTab(new File("/home/haustein/eclipse/rhino-canvas/samples/simple.js"));
		
		
        console.setRows(24);
        console.setColumns(80);
		Container content = frame.getContentPane();
		content.add(split);
//		JPanel statusPanel = new JPanel(new FlowLayout());
//		statusPanel.add(status);
		content.add(BorderLayout.SOUTH, status);
		frame.pack();
		
		split.setDividerLocation(0.67);
		fileChooser.setFileFilter(new FileFilter(){

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".js");
			}

			public String getDescription() {
				return "Javascript file";
			}
			
		});
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void 	windowClosing(WindowEvent e) {
				actionExit();
			}
		});
		frame.setVisible(true);
		
		Main.getGlobal().init(Main.shellContextFactory);
		
		 Main.setIn(console.getIn());
		 Main.setOut(console.getOut());
		 Main.setErr(console.getErr());
	        
		 exec("importPackage(Packages.net.sf.rhinocanvas.js)");
		 
//		 exec("defineClass('net.sf.rhinocanvas.Canvas'); "+
//		 "defineClass('net.sf.rhinocanvas.Image'); "+
//		 "defineClass('net.sf.rhinocanvas.CanvasRenderingContext2D');");
//	        
	        Main.getGlobal().defineProperty("setTimeout", new Callable(){

				public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					new Thread(new Executor((String) args[0], cx, ((Number) args[1]).intValue())).start();
					return null;
				}
	        	
	        }, 0);
	        Main.main(new String[0]);
	}
	
	private void addTab(File file) {
		Tab tab = new Tab(file);
		tabs.add(tab);
		tabPane.add(tab.title, new JScrollPane(tab.editor));
		tabPane.setSelectedIndex(tabs.size()-1);
		
		
		tab.editor.addCaretPositionListener(new CaretPositionListener() {
		      public void positionUpdate(CaretPositionEvent evt) {
		    	  status.setText("pos: "+(evt.getRow() + 1) + "," + (evt.getColumn() + 1));
		      }
		    });
//		tab.editor.addCaretListener(new CaretListener(){
//
//			public void caretUpdate(CaretEvent ce) {
//				JEditorPane e = getCurrentTab().editor;
////				e.getEditorKit().
//				status.setText(""+e.getCaret().getMagicCaretPosition());
//			}
//			
//		});
	}


	public void actionOpen(){
		fileChooser.setDialogTitle("Select a file to load");
        int returnVal = fileChooser.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
         //   CWD = new File(fileChooser.getSelectedFile().getParent());
         //   return result;
            
            String path = file.getAbsolutePath();
            
            int mx = 5;
            for(int i = 1; i <= mx; i++){
            	String lruI = properties.getProperty("lru-"+i);
            	if(lruI == null || path.equals(lruI)){
            		mx = i;
            		break;
            	}
            }
            
            for(int i = mx; i > 1; i--){
            	properties.setProperty("lru-"+i, properties.getProperty("lru-"+(i-1)));
            }
          
            properties.setProperty("lru-1", path);
            
            addTab(file);//.getName(), new JScrollPane(editor));
        }
	}
	
	Tab getCurrentTab(){
		int index = tabPane.getSelectedIndex();
		return (Tab) (index == -1 
			? null
            : tabs.elementAt(index));
	}
	
	public void actionClose(){
		int index = tabPane.getSelectedIndex();
		if(index == -1) return;
		
		tabs.remove(index);
		tabPane.remove(index);
	}
	
	public void actionSave(){
		Tab tab = getCurrentTab();
		if(tab == null) return;
		
		if(tab.file == null){
			actionSaveAs();
		}
		else{
			try{
			Writer w = new FileWriter(tab.file);
			w.write(tab.editor.getText());
			w.close();
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
	
	public void actionSaveAs(){
		Tab tab = getCurrentTab();
		if(tab == null) return;
		
		fileChooser.setDialogTitle("Save file");
        int returnVal = fileChooser.showSaveDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	
            File file = fileChooser.getSelectedFile();
            
            tab.file = file;
            tab.title = file.getName();
            tabPane.setTitleAt(tabPane.getSelectedIndex(), tab.title);
            actionSave();
        }
	}

	
	public void actionNew(){
		addTab(null);
	}
	
	
	public void actionExit(){
		try{
			properties.store(new FileOutputStream(propertyFile), ""+new Date());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public void actionRun(){
		runNumber++;
		Tab tab = getCurrentTab();
		if(tab != null){
			exec(tab.editor.getText());
		}
	}
	
	public void actionTerminate(){
		runNumber++;
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new IDE();
	}




}
