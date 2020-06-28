// Do Gia Huy Pham
package ca.huypham.data;

import ca.senecacollege.prg556.trepra.dao.TravellerDAO;

public class TravellerDAOFactory 
{
	public static TravellerDAO getTravellerDAO() 
	{
		return new TravellerData();
	}
}
