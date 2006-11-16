/**
 * 
 */
package net.sf.rhinocanvas.ide;

import java.io.PrintStream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Main;

class RhinoScriptRunner implements ContextAction, Runnable {

	
	private final RhinoRuntime runtime;
	private Object script;
	private Context context;

	
	RhinoScriptRunner(RhinoRuntime runtime, Object script, Context context){
		this.runtime = runtime;
		this.script = script;
		this.context = context;
	}
	

	public void run(){
		run(context);
	}
	
	
	public Object run(Context cx) {
		
		if(script instanceof String){
			this.script = cx.compileString((String) script,
                this.runtime.currentUrl, 1, null);
		}
		
		
		 if(script instanceof Script){
		 
			 Object result = Main.evaluateScript((Script) script, cx, Main.getGlobal());
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
		 else {
			 Function fn = (Function) script;
			 return fn.call(context, fn, fn, new Object[0]);
		 }
	 }
}