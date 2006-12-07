package net.sf.rhinocanvas.rt;

import java.io.PrintWriter;

import java.util.Hashtable;



import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

public class RhinoRuntime implements ScriptRuntime {

	Hashtable intervals = new Hashtable();
	private int intervalId;

	public int runNumber;
	String currentUrl;

	Scriptable scope;
	PrintWriter writer;
	
	public Scriptable getScope(){
		return scope;
	}
	
	public RhinoRuntime(){
		Context context = Context.enter();
		try {
			// Initialize the standard objects (Object, Function, etc.)
			// This must be done before scripts can be executed. Returns
			// a scope object that we use in later calls.
			
			scope = new ImporterTopLevel(context);
		 
			// Collect the arguments into a single string.
//			String s = "";
//		  	            for (int i=0; i < args.length; i++) {
//		                  s += args[i];
//		              }
//		  
//		              // Now evaluate the string we've colected.
//		  	            Object result = cx.evaluateString(scope, s, "<cmd>", 1, null);
//		  
//		 	             // Convert the result to a string and print it.
//		              System.err.println(cx.toString(result));
//		  
//		          } finally {
//		              // Exit from the context.
//		              Context.exit();
//		          }
//		
//		
		
		
//		Main.getGlobal().init(Main.shellContextFactory);
		
	        
		exec("importPackage(Packages.net.sf.rhinocanvas.js)");
		 
//		 exec("defineClass('net.sf.rhinocanvas.Canvas'); "+
//		 "defineClass('net.sf.rhinocanvas.Image'); "+
//		 "defineClass('net.sf.rhinocanvas.CanvasRenderingContext2D');");
//	        
		
		 defineProperty("setTimeout", new Callable(){
				// todo: allow function parameter instead of string (!!!)

			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
				new Thread(new RhinoScheduler(RhinoRuntime.this, args[0], ((Number) args[1]).intValue(), false)).start();
				return null;
			}
		});

		defineProperty("setInterval", new Callable(){

			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					// todo: allow function parameter instead of string (!!!)
				RhinoScheduler e = new RhinoScheduler(RhinoRuntime.this, args[0], ((Number) args[1]).intValue(), true);
				Integer id = new Integer(intervalId++);
				intervals.put(id, e);
				new Thread(e).start();
				return id;
			}
		});

		defineProperty("clearInterval", new Callable(){

			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
				// todo: allow function parameter instead of string (!!!)
				Integer id = new Integer(((Number) args[0]).intValue());
				RhinoScheduler e = (RhinoScheduler) intervals.get(id);
				e.loop = false;
				intervals.remove(id);
				return null;
			}
		});
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			Context.exit();
		}
	}

	public void defineProperty(String key, Object value){
		scope.put(key, scope, value);
	}
	
	public void setSource(String url){
		this.currentUrl = url;
		defineProperty("documentBase", url);
	}
	
	public Object exec(String expression) {
		//runNumber++;

		return new RhinoScriptRunner(this, expression).run(Context.enter());
	}

//	public JTextComponent getConsole(){
//		if(console == null){
//			ConsoleTextArea console	= new ConsoleTextArea(new String[0]);
////			Main.setIn(console.getIn());
////			Main.setOut(console.getOut());
////			Main.setErr(console.getErr());
//			console.setRows(24);
//			console.setColumns(80);
//			
//		}
//		return console;
//	}
	
	
	public int getLineNumber() {
		return -1;
	}


	public void stop() {
		runNumber++;
		intervals = new Hashtable();
	}

	public void setOutput(PrintWriter writer) {
		this.writer = writer;
	}

}
