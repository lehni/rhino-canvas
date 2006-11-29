/**
 * 
 */
package net.sf.rhinocanvas.rt;

import java.awt.EventQueue;
import java.io.PrintStream;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Main;

class RhinoScheduler implements Runnable {
	
	private final RhinoRuntime runtime;
	
	RhinoScriptRunner runner;
	int time;
	int run;
	boolean loop;

	
	RhinoScheduler(RhinoRuntime runtime, Object command, int time, boolean loop){
		this.runtime = runtime;
		this.time = time;		
		this.run = this.runtime.runNumber;
		this.loop = loop;
		
		runner = new RhinoScriptRunner(runtime, command);
	}
	
	public void run(){
		do{
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			// Auto-generated catch block
				throw new RuntimeException(e);
			}
			
			if(run != this.runtime.runNumber){
				break;
			}
			
			if(EventQueue.isDispatchThread()){
				runner.run(Context.enter());
			}
			else{
				try {
					EventQueue.invokeAndWait(runner);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
		while(loop);
	}
	
}