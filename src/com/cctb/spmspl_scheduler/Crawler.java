package com.cctb.spmspl_scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Crawler {

	private String _url;
	private static final String CONST_SCHEDULE_PAGE = "schedule.php";
	private Vector _vectSchedulePage;
	private Vector _vectScheduleData;
	
	public Crawler(String sURL) 
	{
		_url = sURL;
		_vectSchedulePage = new Vector();
		_vectScheduleData = new Vector();
	}
	
	public void loadCurrentYear()
	{
		if( _vectSchedulePage.size() > 0 )
		{
			String sLine = "";
			//read until locate "<form name";
			for( int i = 0; i < _vectSchedulePage.size(); i++ )
			{
				sLine = _vectSchedulePage.get(i).toString();
				if( sLine.contains("<form name"))
					break;
			}
			Vector vRounds = new Vector();
			String sID, sName, sBreak;
			
			sLine = sLine.substring(sLine.indexOf("&View=All") + 13);
			
			while( sLine.length() > 0)
			{
				sLine = sLine.substring( sLine.indexOf("<option value=") +15);
				sID = sLine.substring(0, sLine.indexOf("\""));
				sLine = sLine.substring( sLine.indexOf(">") +1);
				sName = sLine.substring(0, sLine.indexOf("<"));
				sBreak = sLine.substring( sLine.indexOf("<") );
				sBreak = sBreak.substring(0, 9);
				vRounds.add(new Round(sID, sName));
				
				if( sBreak.equals("</select>"))
					break;
			}
			
			printRounds(vRounds);
		}
	}
	
	public void loadRounds()
	{
		if( _vectSchedulePage.size() > 0 )
		{
			String sLine = "";
			//read until locate "<form name";
			for( int i = 0; i < _vectSchedulePage.size(); i++ )
			{
				sLine = _vectSchedulePage.get(i).toString();
				if( sLine.contains("<form name"))
					break;
			}
			Vector vRounds = new Vector();
			String sID, sName, sBreak;
			
			sLine = sLine.substring(sLine.indexOf("&View=All") + 10);
			sLine = sLine.substring(sLine.indexOf("&View=All") + 13);
			
			while( sLine.length() > 0)
			{
				sLine = sLine.substring( sLine.indexOf("<option value=") +15);
				sID = sLine.substring(0, sLine.indexOf("\""));
				sLine = sLine.substring( sLine.indexOf(">") +1);
				sName = sLine.substring(0, sLine.indexOf("<"));
				sBreak = sLine.substring( sLine.indexOf("<") );
				sBreak = sBreak.substring(0, 9);
				vRounds.add(new Round(sID, sName));
				
				if( sBreak.equals("</select>"))
					break;
			}
			
			printRounds(vRounds);
		}
	}
	
	public Vector loadTeams()
	{
		Vector vTeams = new Vector();
		String sName, sBreak, sID, sDiv;
		int iID, iDiv;
		
		if( _vectSchedulePage.size() > 0 )
		{
			String sLine = "";
			//read until locate "<form name";
			for( int i = 0; i < _vectSchedulePage.size(); i++ )
			{
				sLine = _vectSchedulePage.get(i).toString();
				if( sLine.contains("<form name"))
					break;
			}
			
			sLine = sLine.substring(sLine.indexOf("<select name=\"View\""));
			sLine = sLine.substring(sLine.indexOf("F Division"));
			
			while( sLine.length() > 0)
			{
				sLine = sLine.substring( sLine.indexOf("<option value=") +15);
				sID = sLine.substring(0, sLine.indexOf("\""));
				iID = Integer.parseInt(sID);
				sLine = sLine.substring( sLine.indexOf(">") +1);
				sDiv = sLine.substring(0,1);
				if( sDiv.equals("A"))
					iDiv = 1;
				else if( sDiv.equals("B"))
					iDiv = 2;
				else if( sDiv.equals("C"))
					iDiv = 3;
				else if( sDiv.equals("D"))
					iDiv = 4;
				else if( sDiv.equals("E"))
					iDiv = 5;
				else if( sDiv.equals("F"))
					iDiv = 6;
				else
					iDiv = -1;
				
				sLine = sLine.substring(sLine.indexOf("-")+2);
				sName = sLine.substring(0, sLine.indexOf("<"));
				sBreak = sLine.substring( sLine.indexOf("<") );
				sBreak = sBreak.substring(0, 9);
				vTeams.add(new Team(iID, sName, new Division(iDiv, "", "")));
				
				if( sBreak.equals("</select>"))
					break;
			}
		}
		return vTeams;
	}
	
	private void printTeams(Vector vTeams)
	{
		for( int i = 0; i < vTeams.size(); i++)
		{
			System.out.println(((Team)vTeams.get(i)).getTeamName());
		}
	}
	
	private void printRounds(Vector vRounds)
	{
		for( int i = 0; i < vRounds.size(); i++)
		{
			System.out.println(((Round)vRounds.get(i)).getRoundName());
		}
	}
	
	public void printPage()
	{
		for( int i = 0; i < _vectSchedulePage.size(); i++)
		{
			System.out.println(_vectSchedulePage.get(i));
		}
	}
	
	public void printSchedulePage()
	{
		for( int i = 0; i < _vectScheduleData.size(); i++)
		{
			System.out.println(_vectScheduleData.get(i));
		}
	}
	
	public void loadScheduleProperties()
	{
		try 
		{
	        URL site = new URL(_url+CONST_SCHEDULE_PAGE);
	        URLConnection uc = site.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	        {
	            _vectSchedulePage.add(inputLine);
	        }
	        in.close();
	    } 
		catch (Exception e) 
		{
	        //e.printStackTrace();
			System.out.println("Error loading site: "+_url+CONST_SCHEDULE_PAGE);
	    }
	}
	
	public Vector loadSchedule(int iRound, int iTeam)
	{
		//http://spmspl.com/schedulep.php?Year=2013&Round=1&View=1
		loadScheduleData("http://spmspl.com/schedulep.php?Year=2013&Round="+iRound+"&View="+iTeam);
		return processGames();
	}
	
	private Vector processGames()
	{
		Vector vGames = new Vector();
		Vector vRealGames = new Vector();
		String sDate, sTime, sHome, sAway, sDone;
		int iDiamond;
		
		if( _vectScheduleData.size() > 0 )
		{
			String sLine = "";
			//read until locate "Game #";
			for( int i = 0; i < _vectScheduleData.size(); i++ )
			{
				sLine = _vectScheduleData.get(i).toString();
				if( sLine.contains("Game #"))
				{
					sLine = _vectScheduleData.get(i+8).toString();
					break;
				}
			}
			
			while( sLine.length() > 0)
			{
				sLine = sLine.substring(sLine.indexOf("<tr"));
				sLine = sLine.substring(sLine.indexOf("<td")+3);
				sLine = sLine.substring(sLine.indexOf("<td")+3);
				
				sLine = sLine.substring(sLine.indexOf("<td"));
				sLine = sLine.substring(sLine.indexOf(" color=") + 17);
				sDate = sLine.substring(0, sLine.indexOf("</"));
				sDate = sDate.replace("<br>", " ");
				
				/*try {
					dDate = new SimpleDateFormat("E M d y", Locale.ENGLISH).parse(sDate+Calendar.getInstance().get(Calendar.YEAR));
				} catch (ParseException e) {
				}*/
				
				sLine = sLine.substring(sLine.indexOf("<td"));
				sLine = sLine.substring(sLine.indexOf(" color=") + 17);
				sTime = sLine.substring(0, sLine.indexOf("</"));
				
				sLine = sLine.substring(sLine.indexOf("<td"));
				sLine = sLine.substring(sLine.indexOf(" color=") + 17);
				iDiamond = Integer.parseInt( sLine.substring(0, sLine.indexOf("</")) );
				
				sLine = sLine.substring(sLine.indexOf("<td"));
				sLine = sLine.substring(sLine.indexOf(" color=") + 15);
				sAway = sLine.substring(0, sLine.indexOf("</"));
				sAway = sAway.substring(sAway.indexOf("-")+2);
				sAway = sAway.replace("&nbsp;", "");
				
				
				sLine = sLine.substring(sLine.indexOf("<td"));
				sLine = sLine.substring(sLine.indexOf(" color=") + 15);
				sHome = sLine.substring(0, sLine.indexOf("</"));
				sHome = sHome.substring(sHome.indexOf("-")+2);
				sHome = sHome.replace("&nbsp;", "");
				
				vGames.add(new Game(-1, sDate, sTime, iDiamond, sHome, sAway));
				
				sDone = sLine.substring(sLine.indexOf("</tr") + 4);
				sDone = sDone.substring(sDone.indexOf("</") + 2);
				sDone = sDone.substring(0, 5);
				
				if( sDone.equals("table"))
					break;
			}
		}
		return vGames;
	}
	
	private void printSchedule(Vector vGames)
	{
		for( int i = 0; i < vGames.size(); i++)
		{
			System.out.println(((Game)vGames.get(i)).toString()+"\r\n");
		}
	}
	
	private void loadScheduleData(String sLink)
	{
		try 
		{
	        URL site = new URL(sLink);
	        URLConnection uc = site.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	        {
	        	_vectScheduleData.add(inputLine);
	        }
	        in.close();
	    } 
		catch (Exception e) 
		{
	        //e.printStackTrace();
			System.out.println("Error loading site: "+_url+CONST_SCHEDULE_PAGE);
	    }
	}
	
	/*
	public class Team
	{
		private String _Name;
		private String _ID;
		
		public Team( String iID, String sName)
		{
			_Name = sName;
			_ID = iID;
		}
		
		public String getTeamName()
		{
			return _Name;
		}
	}
	*/
	
	public class Round
	{
		private String _Name;
		private String _ID;
		
		public Round( String iID, String sName)
		{
			_Name = sName;
			_ID = iID;
		}
		
		public String getRoundName()
		{
			return _Name;
		}
	}

}

