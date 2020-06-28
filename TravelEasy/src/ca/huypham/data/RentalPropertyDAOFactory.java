// Do Gia Huy Pham
package ca.huypham.data;

import ca.senecacollege.prg556.trepra.dao.RentalPropertyDAO;

public class RentalPropertyDAOFactory
{
	public static RentalPropertyDAO getRentalPropertyDAO() 
	{
		return new RentalPropertyData();
	}
}
