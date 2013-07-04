package com.cctb.spmspl_scheduler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Game implements Serializable{

	private Calendar cDate;
	private int iGameID;
	private String sDate; 
	private String sTime; 
	private int iDiamond;  
	private String sDone;
	private String sHome;
	private String sAway;
	private int iHomeScore;
	private int iAwayScore;
	private String sDayOfWeek;
	private String sMonth;
	private String sDay;
	private int iDay;
	private int iHr;
	private int iMin;
	
	//region Getters & Setters
	public int getGameID() {
		return iGameID;
	}

	public void setGameID(int iGameID) {
		this.iGameID = iGameID;
	}

	public String getDate() {
		return sDate;
	}

	public void setDate(String sDate) {
		this.sDate = sDate;
	}

	public String getTime() {
		return sTime;
	}

	public void setTime(String sTime) {
		this.sTime = sTime;
	}
	
	public int getDiamond() {
		return iDiamond;
	}
	
	public void setDiamond(int iDiamond) {
		this.iDiamond = iDiamond;
	}

	public String getHomeTeam() {
		return sHome;
	}

	public void setHomeTeam(String sHomeTeam) {
		this.sHome = sHomeTeam;
	}

	public String getAwayTeam() {
		return sAway;
	}

	public void setAwayTeam(String sAwayTeam) {
		this.sAway = sAwayTeam;
	}
	
	public int getHomeScore() {
		return iHomeScore;
	}

	public void setHomeScore(int iHomeScore) {
		this.iHomeScore = iHomeScore;
	}

	public int getAwayScore() {
		return iAwayScore;
	}

	public void setAwayScore(int iAwayScore) {
		this.iAwayScore = iAwayScore;
	}
	
	//endregion Getters & Setters

	public Game(int id, Calendar date, int diamond, String home, String away)
	{
		this.iGameID = id;
		this.cDate = date;
		this.iDiamond = diamond;
		this.sHome = home;
		this.sAway = away;
		this.iHomeScore = 0;
		this.iAwayScore = 0;
	}
	
	public Game(int id, String sDate, String sTime, int iDiamond, String sHome, String sAway)
	{
		this.iGameID = id;
		this.sDate = sDate;
		this.sTime = sTime;
		this.iDiamond = iDiamond;
		this.sHome = sHome;
		this.sAway = sAway;
		this.iHomeScore = 0;
		this.iAwayScore = 0;
		
		sDayOfWeek = sDate.substring(0, sDate.indexOf(" "));
		sMonth = sDate.substring(sDate.indexOf(" ")+1);
		sMonth = sMonth.substring(0, sMonth.indexOf(" "));
		sDay = sDate.substring(sDate.indexOf(" ")+1);
		iDay = Integer.parseInt( sDay.substring(sDay.indexOf(" ")+1) );
		
		iHr = Integer.parseInt( sTime.substring(0, sTime.indexOf(":")) ) + 12;
		iMin = Integer.parseInt( sTime.substring(sTime.indexOf(":")+1) );
		
		
		cDate = Calendar.getInstance();
		
		int iYear = Calendar.getInstance().get(Calendar.YEAR);
		int iMonth = ScheduleDate.valueOf(sMonth.toUpperCase()).value();
        cDate.set(iYear, iMonth-1, iDay, iHr, iMin);     
	}
	
	public Game(int id, String sDate, String sTime, int iDiamond, String sHome, String sAway, int iHomeScore, int iAwayScore)
	{
		this.iGameID = id;
		this.sDate = sDate;
		this.sTime = sTime;
		this.iDiamond = iDiamond;
		this.sHome = sHome;
		this.sAway = sAway;
		this.iHomeScore = iHomeScore;
		this.iAwayScore = iAwayScore;
		
		sDayOfWeek = sDate.substring(0, sDate.indexOf(" "));
		sMonth = sDate.substring(sDate.indexOf(" ")+1);
		sMonth = sMonth.substring(0, sMonth.indexOf(" "));
		sDay = sDate.substring(sDate.indexOf(" ")+1);
		iDay = Integer.parseInt( sDay.substring(sDay.indexOf(" ")+1) );
		
		iHr = Integer.parseInt( sTime.substring(0, sTime.indexOf(":")) ) + 12;
		iMin = Integer.parseInt( sTime.substring(sTime.indexOf(":")+1) );
		
		
		cDate = Calendar.getInstance();
		
		int iYear = Calendar.getInstance().get(Calendar.YEAR);
		int iMonth = ScheduleDate.valueOf(sMonth.toUpperCase()).value();
        cDate.set(iYear, iMonth-1, iDay, iHr, iMin);     
	}
	
	public String toString()
	{
		return "Date:\t"+getDateString()+"\r\nTime:\t"+sTime+"\r\nDiamond:\t"+iDiamond+"\r\nHome:\t"+sHome+"\r\nAway:\t"+sAway;
	}
	
	public String getDateString() {
        return cDate.get(Calendar.MONTH)+"-"+cDate.get(Calendar.DATE)+"-"+cDate.get(Calendar.YEAR)+" "+cDate.get(Calendar.HOUR)+":"+cDate.get(Calendar.MINUTE);
    }
	
	public Calendar getcDate() {
		return cDate;
	}
	public void setcDate(Calendar cDate) {
		this.cDate = cDate;
	}
	
	public String getDateTimeStr(){
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		return sdf.format(cDate);
	}
	
	public long getDateTimeLong(){
		return cDate.getTimeInMillis();
	}
	
}
