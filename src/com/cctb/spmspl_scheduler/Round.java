package com.cctb.spmspl_scheduler;

public class Round
{
	private String _Name;
	private int _ID;
	
	public Round( int iID, String sName)
	{
		_Name = sName;
		_ID = iID;
	}
	
	public String getRoundName()
	{
		return _Name;
	}
	
	public int getRoundID()
	{
		return _ID;
	}
}
