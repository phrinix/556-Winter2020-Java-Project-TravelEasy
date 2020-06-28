package ca.huypham;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import ca.on.phrinix.data.DataSourceFactory;

/**
 * Application Lifecycle Listener implementation class TREPRAContextListener
 *
 */
@WebListener
public class TREPRAContextListener implements ServletContextListener {

	@Resource(name = "trepraDS")
	private DataSource ds;
    public TREPRAContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	DataSourceFactory.setDataSource(ds);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	DataSourceFactory.setDataSource(null);
    }
	
}
