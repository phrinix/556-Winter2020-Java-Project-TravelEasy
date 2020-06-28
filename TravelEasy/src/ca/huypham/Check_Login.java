package ca.huypham;

import ca.senecacollege.prg556.trepra.bean.Traveller;;

public class Check_Login {
	private Traveller user;
	
	public Check_Login(Traveller user)
	{
		setUser(user);
		
	}
	public Traveller getUser()
	{
		return user;
	}
	private void setUser(Traveller user)
	{
		this.user = user;
	}
}
