// Do Gia Huy Pham
package ca.huypham.data;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

import ca.on.phrinix.data.DataSourceFactory;
import ca.senecacollege.prg556.trepra.dao.RentalPropertyDAO;
import ca.senecacollege.prg556.trepra.bean.RentalProperty;

class RentalPropertyData implements RentalPropertyDAO {

	@Override
	public RentalProperty getRentalProperty(String propertyCode) throws SQLException 
	{
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT address, code, bedrooms FROM property WHERE code = ?"))
			{
				statement.setString(1, propertyCode);
				try (ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
						 return new RentalProperty(rs.getString("address"), rs.getString("code"),rs.getInt("bedrooms"));
					else
						return null;
				}
			}
		}
	}
	
	@Override
	public List<RentalProperty> getAvailableProperties(Date arrival, Date departure, int numberOfBedrooms) throws SQLException {
		Timestamp arrival_date = new Timestamp(arrival.getTime());
		Timestamp departure_date = new Timestamp(departure.getTime());
		List<RentalProperty> Property_list = new ArrayList<>();
		String sql = "SELECT address, code, bedrooms FROM property WHERE code NOT IN (SELECT property_code FROM reservation WHERE '"+ arrival_date +"' < departure_date AND '"+ departure_date +"' > arrival_date) AND bedrooms =? ORDER BY bedrooms DESC";
		if (numberOfBedrooms == 0) {
			sql = "SELECT address, code, bedrooms FROM property WHERE code NOT IN (SELECT property_code FROM reservation WHERE '"+ arrival_date +"' < departure_date AND '"+ departure_date +"' > arrival_date) ORDER BY bedrooms DESC";
		}
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement(sql))
			{
				if(numberOfBedrooms != 0) {
					statement.setInt(1, numberOfBedrooms);
				}
				try (ResultSet rslt = statement.executeQuery())
				{
					while (rslt.next()) {
						Property_list.add(new RentalProperty(rslt.getString("address"), rslt.getString("code"),rslt.getInt("bedrooms")));
					}
				}
			}
		}
		return Property_list;
	}
	
}
