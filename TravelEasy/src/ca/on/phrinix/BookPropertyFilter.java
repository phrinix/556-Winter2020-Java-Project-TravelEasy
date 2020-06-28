// Prateek Singh
package ca.on.phrinix;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.huypham.Check_Login;
import ca.huypham.RequestFaultException;
import ca.huypham.data.RentalPropertyDAOFactory;
import ca.on.phrinix.data.RentalPropertyBookingDAOFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.trepra.bean.RentalProperty;
import ca.senecacollege.prg556.trepra.bean.RentalPropertyBooking;
import ca.senecacollege.prg556.trepra.bean.Traveller;

/**
 * Servlet Filter implementation class BookPropertyFilter
 */

public class BookPropertyFilter implements Filter {

    /**
     * Default constructor. 
     */
    public BookPropertyFilter() {
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
	public void doFilter(ServletRequest request,ServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		if("POST".equals(req.getMethod()))
		{
			Traveller traveller = ((Check_Login)req.getSession().getAttribute("userSession")).getUser();
			SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
			java.util.Date arrivalDate = null;
			boolean ValidArrivalDate = true;
			java.util.Date departureDate = null;
			boolean ValidDepartureDate = true;
			boolean ValidDates = true;
			String PropertyCode;
			String numberBedrooms;
			RentalProperty selectedProperty;
			RentalPropertyBooking bookingProperty;
			List<RentalProperty> properties;
			try
			{
				arrivalDate = formatter.parse(req.getParameter("ArrivalDate"));
				request.setAttribute("ArrivalDate",  arrivalDate);
				ValidArrivalDate = true;
			}
			catch(ParseException pe)
			{
				ValidArrivalDate = false;
			}
			request.setAttribute("ValidArrivalDate", ValidArrivalDate);
			try
			{
				departureDate = formatter.parse(req.getParameter("DepartureDate"));
				request.setAttribute("DepartureDate",  departureDate);
				ValidDepartureDate = true;
			}
			catch(ParseException pe)
			{
				ValidDepartureDate = false;
			}
			request.setAttribute("ValidDepartureDate", ValidDepartureDate);
			numberBedrooms = req.getParameter("NumberBedrooms");
			request.setAttribute("NumberBedrooms", numberBedrooms);
			
			if(StringHelper.isNotNullOrEmpty((String)req.getParameter("Book")))
			{
				if(!ValidArrivalDate || !ValidDepartureDate)
				{
					throw new RequestFaultException();
				}
				else
				{
					PropertyCode = req.getParameter("PropertyCode");
					if (PropertyCode == null || 0 == PropertyCode.length())
					{
						throw new RequestFaultException();
					}
					else
					{
						try
						{
							selectedProperty = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(PropertyCode);
						}
						catch(SQLException e)
						{
							throw new ServletException(e);
						}
						if(selectedProperty == null)
						{
							throw new RequestFaultException();
						}
						else
						{
							try 
							{
								bookingProperty = RentalPropertyBookingDAOFactory.getRentalPropertyBookingDAO().bookRentalProperty(traveller.getId(), PropertyCode, arrivalDate, departureDate);
							}
							catch(SQLException e)
							{
								throw new ServletException(e);
							}
							if( bookingProperty != null)	
							{
								resp.sendRedirect(req.getContextPath() + "/");
								return;
							}
						}
						
					}
				}
			}
			
			if(ValidArrivalDate && ValidDepartureDate)
			{
				if(arrivalDate.before(departureDate))
				{
					 ValidDates = true;
					 try {
						 if("Any".equals(numberBedrooms))
						 {
							 numberBedrooms = "0";
						 } 
						 properties = RentalPropertyDAOFactory.getRentalPropertyDAO().getAvailableProperties(arrivalDate, departureDate,Integer.parseInt(numberBedrooms));
						 
					 }
					 catch(SQLException e){
						 throw new ServletException(e);
					 }
					 request.setAttribute("PropertyList", properties);
				}
				else
				{
					 ValidDates = false;
				}
				request.setAttribute("ValidDates", ValidDates);
			}
			
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
