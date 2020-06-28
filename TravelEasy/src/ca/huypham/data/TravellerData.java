// Do Gia Huy Pham
package ca.huypham.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.on.phrinix.data.DataSourceFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.trepra.dao.TravellerDAO;
import ca.senecacollege.prg556.trepra.bean.Traveller;

class TravellerData implements TravellerDAO{
	
	@Override
	public Traveller authorizeTraveller(String username, String password) throws SQLException
	{
		if (StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
		{
			try (Connection conn = DataSourceFactory.getDataSource().getConnection())
			{
				try (PreparedStatement statement = conn.prepareStatement("SELECT id, firstname, lastname FROM traveller WHERE user_name = ? AND password = ? "))
				{
					statement.setString(1,username);
					statement.setString(2,password);
					try (ResultSet rs = statement.executeQuery())
					{
						if (rs.next())
							return new Traveller(rs.getInt("id"), rs.getString("firstname"),rs.getString("lastname"));
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public Traveller getTraveller(int id) throws SQLException
	{
		try (Connection conn = DataSourceFactory.getDataSource().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT firstname, lastname, id FROM traveller WHERE id= ?"))
			{
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
						return new Traveller(rs.getInt("id"), rs.getString("firstname"),rs.getString("lastname"));
					else
						return null;
				}
			}
		}
	}
}
