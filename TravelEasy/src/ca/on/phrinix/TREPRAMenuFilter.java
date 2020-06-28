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
import javax.servlet.http.HttpSession;

import java.sql.SQLException;

import ca.senecacollege.prg556.trepra.bean.Traveller;
import ca.senecacollege.prg556.trepra.bean.RentalPropertyBooking;
import ca.on.phrinix.data.RentalPropertyBookingDAOFactory;
import ca.huypham.Check_Login;
/**
 * Servlet Filter implementation class TREPRAMenuFilter
 */

public class TREPRAMenuFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TREPRAMenuFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		java.util.List<RentalPropertyBooking> bookings;
		try
		{
			Traveller traveller = ((Check_Login)session.getAttribute("userSession")).getUser();
			bookings = RentalPropertyBookingDAOFactory.getRentalPropertyBookingDAO().getRentalPropertyBookings(traveller.getId());
			if(!bookings.isEmpty())
			{
				request.setAttribute("nextBooking", bookings.get(0));
			}
			
			chain.doFilter(request, response);
		}
		catch(SQLException exsql)
		{
			throw new ServletException(exsql);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
