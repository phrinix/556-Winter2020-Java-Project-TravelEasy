// Prateek Singh
package ca.on.phrinix.data;

import ca.senecacollege.prg556.trepra.dao.RentalPropertyBookingDAO;

public class RentalPropertyBookingDAOFactory {
	public static RentalPropertyBookingDAO getRentalPropertyBookingDAO() 
	{
		return new RentalPropertyBookingData();
	}
}
