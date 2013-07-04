package com.cctb.spmspl_scheduler;

public class Team
{
	private String _Name;
	private int _ID;
	private Division _Division;
	
	public Team(int iID, String sName, Division division)
	{
		_Name = sName;
		_ID = iID;
		_Division = division;
	}
	
	public Team(int iID, String sName)
	{
		_Name = sName;
		_ID = iID;
		_Division = null;
	}
	
	public String getTeamName()
	{
		return _Name;
	}
	
	public int getTeamID()
	{
		return _ID;
	}
	
	public Division getDivision()
	{
		return _Division;
	}
}