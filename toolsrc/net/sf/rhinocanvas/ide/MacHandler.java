package net.sf.rhinocanvas.ide;

import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;
import com.apple.eawt.ApplicationListener;



public class MacHandler extends Application {

	IDE ide;
		
	public MacHandler(IDE ide) {
		this.ide = ide;
		addAboutMenuItem();
		
		addApplicationListener(new ApplicationListener(){

			public void handleAbout(ApplicationEvent arg0) {
				MacHandler.this.ide.actionAbout();
				arg0.setHandled(true);
			}

			public void handleOpenApplication(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void handleOpenFile(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void handlePreferences(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void handlePrintFile(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void handleQuit(ApplicationEvent arg0) {
				if(MacHandler.this.ide.actionExit()){
					arg0.setHandled(true);
				}
				else {
					arg0.setHandled(false);
				}
			}

			public void handleReOpenApplication(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

/*	    class AboutBoxHandler extends ApplicationAdapter {
	        public void handleAbout(ApplicationEvent event) {
	        }
	    }*/
	
}
