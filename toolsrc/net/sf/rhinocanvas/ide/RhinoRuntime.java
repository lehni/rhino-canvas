package net.sf.rhinocanvas.ide;

import java.util.Hashtable;

import javax.swing.text.JTextComponent;


import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Main;

public class RhinoRuntime implements Runtime {

	Hashtable intervals = new Hashtable();
	private int intervalId;
	ConsoleTextArea console = new ConsoleTextArea(new String[0]);


	int runNumber;
	String currentUrl;
	
	
	public RhinoRuntime(){
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
				new Thread(new RhinoScheduler(RhinoRuntime.this, args[0], cx, ((Number) args[1]).intValue(), false)).start();
				return null;
			}
	        	
		}, 0);

		Main.getGlobal().defineProperty("setInterval", new Callable(){

			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					// todo: allow function parameter instead of string (!!!)
				RhinoScheduler e = new RhinoScheduler(RhinoRuntime.this, args[0], cx, ((Number) args[1]).intValue(), true);
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
				RhinoScheduler e = (RhinoScheduler) intervals.get(id);
				e.loop = false;
				intervals.remove(id);
				return null;
			}
		}, 0);

		console.setRows(24);
		console.setColumns(80);
	        
		new Thread(new Runnable(){
			public void run(){
				Main.main(new String[0]);
			}
		}).start();
	}

	public void setSource(String url){
		this.currentUrl = url;
		Main.getGlobal().defineProperty("documentBase", url, 0);
	}
	
	public Object exec(String expression) {
		runNumber++;
		return Main.shellContextFactory.call(new RhinoScriptRunner(this, expression, null));
	}

	public JTextComponent getConsole(){
		return console;
	}
	
	
	public int getLineNumber() {
		return -1;
	}


	public void stop() {
		runNumber++;
		intervals = new Hashtable();
	}

}
