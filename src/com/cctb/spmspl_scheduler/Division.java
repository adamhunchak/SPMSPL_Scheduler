package com.cctb.spmspl_scheduler;

public class Division {
	private String _Code;
	private String _Name;
	private int _ID;
	
	public Division(int iID, String sCode, String sName)
	{
		_Code = sCode;
		_Name = sName;
		_ID = iID;
	}
	
	public String getDivisionCode()
	{
		return _Code;
	}
	
	public String getDivisionName()
	{
		return _Name;
	}
	
	public int getDivisionID()
	{
		return _ID;
	}
}
