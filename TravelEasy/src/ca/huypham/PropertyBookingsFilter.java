 // Do Gia Huy Pham
package ca.huypham;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.huypham.Check_Login;
import ca.on.phrinix.data.RentalPropertyBookingDAOFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.trepra.bean.RentalPropertyBooking;
import ca.senecacollege.prg556.trepra.bean.Traveller;
import ca.huypham.RequestFaultException;


/**
 * Servlet Filter implementation class PropertyBookingsFilter
 */
public class PropertyBookingsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public PropertyBookingsFilter() {
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
		HttpSession session = request.getSession();
		Traveller user = ((Check_Login)session.getAttribute("userSession")).getUser();
		List<RentalPropertyBooking> Properties;
		if ("POST".equals(request.getMethod()) && StringHelper.isNotNullOrEmpty(request.getParameter("cancel")))
		{
			try
			{
				int bookingCode = Integer.parseInt(request.getParameter("BookingCode"));
				RentalPropertyBooking cancel_property = RentalPropertyBookingDAOFactory.getRentalPropertyBookingDAO().getRentalPropertyBooking(bookingCode);
				if (cancel_property != null) 
				{
					RentalPropertyBookingDAOFactory.getRentalPropertyBookingDAO().cancelRentalPropertyBooking(bookingCode);
				}
			}
			catch (NumberFormatException nfe)
			{
		       throw new RequestFaultException(nfe);
			}
		    catch (SQLException sqle)
			{
		       throw new ServletException(sqle);
			}
		}
		try {
			int id = user.getId();
			Properties = RentalPropertyBookingDAOFactory.getRentalPropertyBookingDAO().getRentalPropertyBookings(id);
		}
		catch (SQLException e)
		{
		    throw new ServletException(e);
		}
		request.setAttribute("PropertyBookings", Properties);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
