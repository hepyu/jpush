/**
 * 
 */
package com.tongbupan.tpush.listener;

import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import com.tongbupan.tpush.TPushInitializer;

/**
 * @author hpy
 * 
 */
public class TPushContextListener extends ContextLoaderListener {

	// private static final Logger logger = Logger
	// .getLogger(TPushContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TPushInitializer.init();
		super.contextInitialized(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		super.contextDestroyed(sce);
	}

}
