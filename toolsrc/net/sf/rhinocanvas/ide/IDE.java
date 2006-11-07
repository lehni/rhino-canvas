/*
 * Created on 28/10/2006 by Stefan Haustein
 */
package net.sf.rhinocanvas.ide;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.ToolErrorReporter;

import org.mozilla.javascript.tools.shell.Main;
import org.ujac.ui.editor.CaretPositionEvent;
import org.ujac.ui.editor.CaretPositionListener;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class IDE extends JFrame {
	
	static int runNumber;

	
	class ReflectiveAction extends AbstractAction{

		Method method;
		
		ReflectiveAction(String label, String methodName){
			super(label);
			
			try {
				method = (IDE.class).getMethod(methodName);
			} catch (Exception e) {
				
				throw new RuntimeException(e);
			} 			
		}
		
		
		
		
		
		public void actionPerformed(ActionEvent ae) {
			try {
				method.invoke(IDE.this);
			
			} catch (Exception e) {
				
				throw new RuntimeException(e);
			} 
		}
		
		ReflectiveAction setMnemonic(int m){
			putValue(Action.MNEMONIC_KEY, new Integer(m));
			return this;
		}
		
		ReflectiveAction setAccelerator(String a){
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke((macOS ? "meta " : "control ") + a));
			return this;
		}
	}

	
	boolean consoleFocussed;
	boolean macOS = System.getProperty("mrj.version") != null;
	File propertyFile = new File(System.getProperty("user.home"), ".rhino-canvas-ide.ini");
	Vector tabs = new Vector();
	//JFrame frame = new JFrame("Rhino Canvas IDE");
	JTabbedPane tabPane = new JTabbedPane();
	JFileChooser fileChooser = new JFileChooser();
	JLabel status = new JLabel();
	ConsoleTextArea console = new ConsoleTextArea(new String[0]);
	
	JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabPane, new JScrollPane(console));
	Properties properties = new Properties();

	Hashtable intervals = new Hashtable();
	private int intervalId;

	class Executor implements ContextAction, Runnable {

		String scriptText;
		Context context;
		int time;
		int run;
		boolean loop;
		
		Executor(String scriptText){
			this.scriptText = scriptText;
		}
		
		Executor(String scriptText, Context context, int time, boolean loop){
			this(scriptText);
			this.context = context;
			this.time = time;		
			this.run = runNumber;
			this.loop = loop;
		}
		
		public void run(){
			do{
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
				// Auto-generated catch block
					throw new RuntimeException(e);
				}
				
				if(run != runNumber){
					break;
				}
				
				if(EventQueue.isDispatchThread()){
					run(Context.enter());
				}
				else{
					try {
						EventQueue.invokeAndWait(new Executor(scriptText, context, 0, false));
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
			}
			while(loop);
		}
		
		
		public Object run(Context cx) {
			
			 Script script = cx.compileString(scriptText ,
                    getCurrentTab().title, 1, null);
			 
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
	
	
	public IDE(){
		super("Rhino Canvas IDE");
		String cwd = null;
		try {
			properties.load(new FileInputStream(propertyFile));
			
			cwd = properties.getProperty("lru-1");
			if(cwd != null){
				fileChooser.setCurrentDirectory(new File(cwd).getParentFile());
			}
		} 
		catch(FileNotFoundException e){
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(macOS){
			new MacHandler(this);
		}
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu runMenu = new JMenu("Run");
		
		menuBar.add(fileMenu);
		
		fileMenu.setMnemonic('F');
		runMenu.setMnemonic('R');
		
		fileMenu.add(new ReflectiveAction("New", "actionNew")
			.setMnemonic(KeyEvent.VK_N));
		fileMenu.add(new ReflectiveAction("Open File...", "actionOpen"));
		
		if(cwd != null){
			JMenu recent = new JMenu("Open Recent");
			fileMenu.add(recent);		
			
			for(int i = 1;; i++){
				String path = properties.getProperty("lru-"+i);
				if(path == null) {
					break;
				}
				File f = new File(path);
				Action a = new AbstractAction(""+i+". "+f.getName()){

					public void actionPerformed(ActionEvent e) {
						openFile(new File((String) getValue(SHORT_DESCRIPTION)));
					}
				};
				
				a.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_0+i));
				a.putValue(Action.SHORT_DESCRIPTION, path);
				recent.add(a);
			}
		}
		
		fileMenu.addSeparator();
		fileMenu.add(new ReflectiveAction("Close", "actionClose").setMnemonic(KeyEvent.VK_C));
		fileMenu.addSeparator();
		fileMenu.add(new ReflectiveAction("Save", "actionSave")
			.setMnemonic(KeyEvent.VK_S).setAccelerator("S"));
		fileMenu.add(new ReflectiveAction("Save as...", "actionSaveAs").setMnemonic(KeyEvent.VK_A));
		
		if(!macOS){
			fileMenu.add(new ReflectiveAction("Quit", "actionExit").setMnemonic('Q'));
		}
		
		menuBar.add(runMenu);
		runMenu.add(new ReflectiveAction("Run", "actionRun").setMnemonic(KeyEvent.VK_R));
		runMenu.add(new ReflectiveAction("Terminate", "actionTerminate").setMnemonic(KeyEvent.VK_T));
		
		JMenu menu = new JMenu("Edit");
		menu.setMnemonic('E');
		menuBar.add(menu);
		menu.add(new ReflectiveAction("Cut", "actionCut").setAccelerator("X"));		
		menu.add(new ReflectiveAction("Copy", "actionCopy").setAccelerator("C"));		
		menu.add(new ReflectiveAction("Paste", "actionPaste").setAccelerator("V"));
		menu.addSeparator();
		menu.add(new ReflectiveAction("Find / Replace...", "actionFind").setAccelerator("F"));
		menu.add(new ReflectiveAction("Goto Line...", "actionGoto").setAccelerator("L"));
//		menu.add(new ReflectiveAction("Find next", "actionFindNext").setAccelerator("K"));
		
		if(!macOS){
			menu = new JMenu("Help");
			menu.setMnemonic('H');
			menuBar.add(menu);
			menu.add(new ReflectiveAction("About RhinoCanvas", "actionAbout"));
		}
		
		setJMenuBar(menuBar);
		
		addTab(null);
//		addTab(new File("/home/haustein/eclipse/rhino-canvas/samples/simple.js"));
		
		
        console.setRows(24);
        console.setColumns(80);
        
        console.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				consoleFocussed = true;
			}

			public void focusLost(FocusEvent arg0) {
				consoleFocussed = false;
			}
        	
        });
        
		Container content = getContentPane();
		content.add(split);
//		JPanel statusPanel = new JPanel(new FlowLayout());
//		statusPanel.add(status);
		content.add(BorderLayout.SOUTH, status);
		pack();
		
		split.setDividerLocation(0.67);
		fileChooser.setFileFilter(new FileFilter(){

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".js");
			}

			public String getDescription() {
				return "Javascript file";
			}
			
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void 	windowClosing(WindowEvent e) {
				actionExit();
			}
		});
		setVisible(true);
		
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
				// todo: allow function parameter instead of string (!!!)

				public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					new Thread(new Executor((String) args[0], cx, ((Number) args[1]).intValue(), false)).start();
					return null;
				}
	        	
	        }, 0);

	        Main.getGlobal().defineProperty("setInterval", new Callable(){

				public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					// todo: allow function parameter instead of string (!!!)
					Executor e = new Executor((String) args[0], cx, ((Number) args[1]).intValue(), true);
					Integer id = new Integer(intervalId++);
					intervals.put(id, e);
					new Thread(e).start();
					return id;
				}
	        	
	        }, 0);

	        Main.getGlobal().defineProperty("clearInterval", new Callable(){

				public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					// todo: allow function parameter instead of string (!!!)
					Integer id = new Integer(((Number) args[0]).intValue());
					Executor e = (Executor) intervals.get(id);
					e.loop = false;
					intervals.remove(id);
					return null;
				}
	        	
	        }, 0);

	        
	        
	        
	        
	        
	        Main.main(new String[0]);
	}
	
	protected void openFile(File file) {

        String path = file.getAbsolutePath();
        if(!file.exists()){
        	JOptionPane.showMessageDialog(this, "File "+file.getAbsolutePath()+" does not exist.");
        	return;
        }
        
        for(int i = 0; i < tabs.size(); i++){
        	Tab ti = (Tab) tabs.get(i);
        	if(ti.file != null && ti.file.getAbsolutePath().equals(path)){
        		tabPane.setSelectedIndex(i);
        		return;
        	}
        }
        
        try{
        	addTab(file);//.getName(), new JScrollPane(editor));
        }
        catch (Exception e){
        	JOptionPane.showMessageDialog(this, "Error accessing file "+file.getAbsolutePath()+": "+e);
        	return;
        }
        
        
        addLRU(path);

		
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
		
		tab.editor.getDocument().addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent arg0) {
				changed();
			}

			public void insertUpdate(DocumentEvent arg0) {
				changed();
			}

			public void removeUpdate(DocumentEvent arg0) {
				changed();
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


	protected void changed() {
		Tab tab = getCurrentTab();
		
		if(!tab.changed){
			tab.changed = true;
			int index = tabPane.getSelectedIndex();
			tabPane.setTitleAt(index, tabPane.getTitleAt(index)+"*");
		}
	}


	public void actionOpen(){
		fileChooser.setDialogTitle("Select a file to load");
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            openFile(fileChooser.getSelectedFile());
         //   CWD = new File(fileChooser.getSelectedFile().getParent());
         //   return result;
            
        }
	}
	
	private boolean checkOpen(String path) {
		
		return false;
	}



	void addLRU(String path){
        int mx = 9;
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
	}
	
	Tab getCurrentTab(){
		int index = tabPane.getSelectedIndex();
		return (Tab) (index == -1 
			? null
            : tabs.elementAt(index));
	}
	
	public boolean actionClose(){

		Tab tab = getCurrentTab();
		if(tab == null){
			return true;
		}

		if(tab.changed){
			switch(JOptionPane.showConfirmDialog(this, tab.title+" has unsaved changes. Save changes?", "Close", JOptionPane.YES_NO_CANCEL_OPTION)){
			case JOptionPane.YES_OPTION:
				if(actionSave()) break; //otherwise fall-through
			case JOptionPane.CANCEL_OPTION:
				return false;
			} 
		}
		
		int index = tabPane.getSelectedIndex();
		tabs.remove(index);
		tabPane.remove(index);
		
		if(tabs.size() == 0){
			addTab(null);
		} 

		return true;
	}
	
	public boolean actionSave(){
		Tab tab = getCurrentTab();
		if(tab == null) return false;
		
		if(tab.file == null){
			return actionSaveAs();
		}

		try{
			Writer w = new FileWriter(tab.file);
			w.write(tab.editor.getText());
			w.close();
			
			int index = tabPane.getSelectedIndex();
			String title = tabPane.getTitleAt(index);
			if(title.endsWith("*")){
				tabPane.setTitleAt(index, title.substring(0, title.length()-1));
			}
			tab.changed = false;
			
			
			addLRU(tab.file.getAbsolutePath());
			
			return true;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public boolean actionSaveAs(){
		Tab tab = getCurrentTab();
		if(tab == null) return false;
		
		fileChooser.setDialogTitle("Save file");
        int returnVal = fileChooser.showSaveDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
        	return false;
        }
        	
        File file = fileChooser.getSelectedFile();
            
        tab.file = file;
        tab.title = file.getName();
        tabPane.setTitleAt(tabPane.getSelectedIndex(), tab.title);
        actionSave();
        
        return true;
	}

	
	public void actionNew(){
		addTab(null);
	}
	
	
	public boolean actionExit(){
		try{
			properties.store(new FileOutputStream(propertyFile), ""+new Date());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		while(tabs.size() > 1 || getCurrentTab().changed){
			if(!actionClose()) return false;
		}
		
		System.exit(0);
		return true;
	}
	
	
	
	public void actionRun(){
		runNumber++;
		Tab tab = getCurrentTab();
		if(tab != null){
			if(tab.file != null){
				Main.getGlobal().defineProperty("documentBase", tab.file.toURI().toString(), 0);
			}
			exec(tab.editor.getText());
		}
	}
	
	
	public void actionCut(){
		if(consoleFocussed){
			console.cut();
		}
		else {
			getCurrentTab().editor.cut();
		}
	}

	public void actionCopy(){
		if(consoleFocussed){
			console.copy();
		}
		else {
			getCurrentTab().editor.copy();
		}
	}
	
	public void actionPaste(){
		System.out.println("Paste");
		if(consoleFocussed){
			console.paste();
		}
		else {
			getCurrentTab().editor.paste();
		}
	}

	public void actionFind(){	
		getCurrentTab().editor.showFindDialog();
	}

	public void actionGoto(){	
		getCurrentTab().editor.showGotoLineDialog();
	}

	
	public void actionTerminate(){
		runNumber++;
		
		intervals = new Hashtable();
	}
	
	public void actionAbout(){
		
		//116 116
		//01805 012021
		
		//JPanel panel = new JPanel(new BorderLayout());
		Icon icon;
		try {
			icon = new ImageIcon(ImageIO.read(IDE.class.getResourceAsStream("rhino-small.png")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		
//		panel.add(BorderLayout.WEST, icon);
	
		JLabel label = new JLabel("<html><p><b>Rhino Canvas IDE</b><br>"+
				"http://rhino-canvas.sf.net<br>"+
				"Source code licensed under the GNU Public License (GPL)<br>"+
				"(c) 2006 Stefan Haustein<br>"+
				"<br>"+
				"<b>Components</b><br>"+
				"Rhino Javascript Interpreter (c) 1997-2006 Mozilla Foundation<br>"+
				"Batik AWT Extensions (c) 2005 Apache Foundation<br>"+
				"UJAC-UI Editor Component (c) 2003,2004 Slava Pestov, Christian Lauer etal.<br>"+
				"CSS4J (c) 2006 Stefan Haustein<br>", icon, JLabel.LEFT);
		
		JOptionPane.showMessageDialog(this, label,
//				
				"About RhinoCanvasIDE", JOptionPane.PLAIN_MESSAGE);
		
		
	}
	
	
	public void processKeyEvent(KeyEvent ke){
		System.out.println("KE:"+ke);
		super.processKeyEvent(ke);
		
		
	}

}