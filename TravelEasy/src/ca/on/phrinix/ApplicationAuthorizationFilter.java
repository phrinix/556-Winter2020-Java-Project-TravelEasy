// Prateek Singh
package ca.on.phrinix;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.on.senecac.prg556.common.StringHelper;

/**
 * Servlet Filter implementation class ApplicationAuthorizationFilter
 */

public class ApplicationAuthorizationFilter implements Filter {
	private String loginPage = "/travellerlogin.jspx";

	/**
	 * Default constructor.
	 */
	public ApplicationAuthorizationFilter()
	{
		
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String uriLogin = req.getContextPath() + getLoginPage();
		if (null == req.getSession().getAttribute("userSession") && !uriLogin.equals(req.getRequestURI())) // null means not logged in, don't redirect /login.jspx
		{
			((HttpServletResponse) response).sendRedirect(uriLogin);
			
		}
		else {
			chain.doFilter(request,  response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		if (StringHelper.isNotNullOrEmpty(fConfig.getInitParameter("login")))
			setLoginPage(StringHelper.stringPrefix(fConfig.getInitParameter("login"), "/"));
	}

	public synchronized String getLoginPage() {
		return loginPage;
	}

	private synchronized void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
}
