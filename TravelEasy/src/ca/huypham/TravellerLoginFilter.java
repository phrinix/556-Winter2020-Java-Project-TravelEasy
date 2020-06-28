package ca.huypham;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.trepra.bean.Traveller;
import ca.huypham.data.TravellerDAOFactory;
import ca.huypham.Check_Login;

/**
 * Servlet Filter implementation class TravellerLoginFilter
 */

public class TravellerLoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TravellerLoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		try
		{
			HttpSession session = request.getSession();
			Check_Login usession = (Check_Login)session.getAttribute("userSession");

			if (null == usession)
			{
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				if ("POST".equals(request.getMethod()) && StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
				{
					Traveller user = TravellerDAOFactory.getTravellerDAO().authorizeTraveller(username, password);
					if (user != null)
					{
						session.setAttribute("userSession", new Check_Login(user));
						response.sendRedirect(request.getContextPath() + "/"); // redirect to context root folder
						return;
					}
					else {
						request.setAttribute("unsuccessfulLogin", Boolean.TRUE);
						request.setAttribute("username", StringHelper.xmlEscape(username));
					}
				}
				chain.doFilter(req, resp);
			}
			else
			{
				response.sendRedirect(request.getContextPath() + "/"); // already logged in -- redirect to context root folder
				return;
			}
		}
		catch (SQLException sqle)
		{
			throw new ServletException(sqle);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
