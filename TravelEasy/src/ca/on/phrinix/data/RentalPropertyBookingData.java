// Prateek Singh
package ca.on.phrinix.data;

import ca.senecacollege.prg556.trepra.bean.RentalPropertyBooking;
import ca.senecacollege.prg556.trepra.dao.RentalPropertyBookingDAO;
import ca.huypham.data.RentalPropertyDAOFactory;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.Timestamp;


class RentalPropertyBookingData implements RentalPropertyBookingDAO{

	@Override
	public RentalPropertyBooking bookRentalProperty(int travellerId, String propertyCode, Date arrival, Date departure) throws SQLException
	{
		Timestamp arrivalTime = new Timestamp(arrival.getTime());
		Timestamp departureTime = new Timestamp(departure.getTime());
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			int bookingCode;
			try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				try (ResultSet rslt = stmt.executeQuery("SELECT booking_code, traveller_id, property_code, arrival_date, departure_date FROM reservation"))
				{
					rslt.moveToInsertRow();
					rslt.updateInt("traveller_id", travellerId);
					rslt.updateString("property_code", propertyCode);
					rslt.updateTimestamp("arrival_date", arrivalTime);
					rslt.updateTimestamp("departure_date", departureTime);
					rslt.insertRow();
				}
				String sql = "SELECT @@IDENTITY";
				try (ResultSet rslt = stmt.executeQuery(sql))
				{
					rslt.next();
					bookingCode = rslt.getInt(1);
					String address = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(propertyCode).getAddress();
					int numberOfBedrooms = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(propertyCode).getNumberOfBedrooms();
					return new RentalPropertyBooking(bookingCode, address, propertyCode, numberOfBedrooms, arrival, departure);	
				}
			}
		}
		
	}
	
	public void cancelRentalPropertyBooking(int bookingCode) throws SQLException 
	{
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code FROM reservation WHERE booking_code = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				pstmt.setInt(1, bookingCode);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
						rslt.deleteRow();
				}
			}
		}
	}

	@Override
	public RentalPropertyBooking getRentalPropertyBooking(int bookingCode) throws SQLException {
		
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code, traveller_id, property_code, arrival_date, departure_date FROM reservation WHERE booking_code = ?"))
			{
				pstmt.setInt(1, bookingCode);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
					{
						String address = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(rslt.getString("property_code")).getAddress();
						int numberOfBedrooms = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(rslt.getString("property_code")).getNumberOfBedrooms();
						return new RentalPropertyBooking(rslt.getInt("booking_code"), address, rslt.getString("property_code"), numberOfBedrooms, rslt.getDate("arrival_date") , rslt.getDate("departure_date"));	
					}
					else
						return null;
					
				}
			}
			
		}
	}

	@Override
	public List<RentalPropertyBooking> getRentalPropertyBookings(int travellerId) throws SQLException 
	{
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code, traveller_id, property_code, arrival_date, departure_date FROM reservation  WHERE traveller_id = ? ORDER BY arrival_date ASC, departure_date ASC"))
			{
				pstmt.setInt(1, travellerId);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					List<RentalPropertyBooking> PropertyBookings = new ArrayList<>();
					while (rslt.next())
					{
						String address = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(rslt.getString("property_code")).getAddress();
						int numberOfBedrooms = RentalPropertyDAOFactory.getRentalPropertyDAO().getRentalProperty(rslt.getString("property_code")).getNumberOfBedrooms();
						PropertyBookings.add(new RentalPropertyBooking(rslt.getInt("booking_code"), address, rslt.getString("property_code"), numberOfBedrooms, rslt.getDate("arrival_date") , rslt.getDate("departure_date")));
					}
					return PropertyBookings;
				}
			}
		}
	}
}
