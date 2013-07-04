package com.cctb.spmspl_scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	//region Database Constants
	
	//region DIVISION Constants
	static final String DIV_ROWID = "id";
	static final String DIV_CODE = "divCode";
	static final String DIV_NAME = "divName";
	static final String DIV_TABLE_NAME = "division";
	public static final String TABLE_CREATE_DIV = "create table "+DIV_TABLE_NAME+" ( "+
			DIV_ROWID+" integer primary key, "+
			DIV_CODE+" varchar(5) not null, "+
			DIV_NAME+" varchar(50) not null);";
	
	static final String DIV_INSERT1 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (1, 'A', 'A Division');";
	static final String DIV_INSERT2 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (2, 'B', 'B Division');";
	static final String DIV_INSERT3 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (3, 'C', 'C Division');";
	static final String DIV_INSERT4 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (4, 'D', 'D Division');";
	static final String DIV_INSERT5 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (5, 'E', 'E Division');";
	static final String DIV_INSERT6 = "insert into "+DIV_TABLE_NAME+" ("+DIV_ROWID+", "+DIV_CODE+", "+DIV_NAME+") "+
			"values (6, 'F', 'F Division');";
	//endregion DIVISION Constants
	
	//region TEAM Constants
	static final String TEAM_ROWID = "id";
	static final String TEAM_NAME = "teamName";
	static final String TEAM_DIV_ID = "divID";
	static final String TEAM_TABLE_NAME = "team";
	public static final String TABLE_CREATE_TEAM = "create table "+TEAM_TABLE_NAME+" ( "+
			TEAM_ROWID+" integer primary key, "+
			TEAM_DIV_ID+" integer not null, " +
			TEAM_NAME+" varchar(50) not null);";
	//endregion TEAM Constants
	
	//region ROUND Constants
	static final String ROUND_ROWID = "id";
	static final String ROUND_NAME = "roundName";
	static final String ROUND_TABLE_NAME = "round";
	public static final String TABLE_CREATE_ROUND = "create table "+ROUND_TABLE_NAME+" ( "+
			ROUND_ROWID+" integer primary key, "+
			ROUND_NAME+" varchar(50) not null);";
	//endregion ROUND Constants
	
	//region GAME Constants
	static final String GAME_ROWID = "id";
	static final String GAME_DATE = "gameDate";
	static final String GAME_TIME = "gameTime";
	static final String GAME_DATETIME = "gameDatetime";
	static final String GAME_DIAMOND = "diamond";
	static final String GAME_HOME_TEAM = "homeTeam";
	static final String GAME_AWAY_TEAM = "awayTeam";
	static final String GAME_HOME_SCORE = "homeScore";
	static final String GAME_AWAY_SCORE = "awayScore";
	static final String GAME_TABLE_NAME = "game";
	public static final String TABLE_CREATE_GAME = "create table "+GAME_TABLE_NAME+" ( "+
			GAME_ROWID+" integer primary key autoincrement, "+
			GAME_DATE+" date null, "+
			GAME_TIME+" time null, "+
			GAME_DATETIME+" datetime null, "+
			GAME_DIAMOND+" int null, "+
			GAME_HOME_TEAM+" varchar(50) null, "+
			GAME_AWAY_TEAM+" varchar(50) null, "+
			GAME_HOME_SCORE+" int null, "+
			GAME_AWAY_SCORE+" int null "+
			");";
	//endregion GAME Constants
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "spmspl_db.db";
	static final int CONST_ONE_DAY = 86400000; //one day in milliseconds
	
	//endregion Database Constants
	
	final Context context;
	
	ScheduleDBHelper dbHelper;
	SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		dbHelper = new ScheduleDBHelper(context); 
	}
	
	private static class ScheduleDBHelper extends SQLiteOpenHelper
	{
		ScheduleDBHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try
			{
				db.execSQL(TABLE_CREATE_DIV);
				db.execSQL(TABLE_CREATE_TEAM);
				db.execSQL(TABLE_CREATE_ROUND);
				db.execSQL(TABLE_CREATE_GAME);
				db.execSQL(DIV_INSERT1);
				db.execSQL(DIV_INSERT2);
				db.execSQL(DIV_INSERT3);
				db.execSQL(DIV_INSERT4);
				db.execSQL(DIV_INSERT5);
				db.execSQL(DIV_INSERT6);
				
				//load rounds
				//load teams
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			onCreate(db);
		}
	}
	
	public DBAdapter open() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void resetTables()
	{
		open();
		db.execSQL("drop table if exists division");
		db.execSQL("drop table if exists team");
		db.execSQL("drop table if exists round");
		db.execSQL("drop table if exists game");
		db.execSQL(TABLE_CREATE_DIV);
		db.execSQL(TABLE_CREATE_TEAM);
		db.execSQL(TABLE_CREATE_ROUND);
		db.execSQL(TABLE_CREATE_GAME);
		db.execSQL(DIV_INSERT1);
		db.execSQL(DIV_INSERT2);
		db.execSQL(DIV_INSERT3);
		db.execSQL(DIV_INSERT4);
		db.execSQL(DIV_INSERT5);
		db.execSQL(DIV_INSERT6);
		
		//load rounds
		//load teams
		close();
	}
	
	//region Division SQL Commands

	public long insertDivision(Division div)
	{
		ContentValues cv = new ContentValues();
		cv.put(DIV_ROWID, div.getDivisionID());
		cv.put(DIV_NAME, div.getDivisionName());
		
		return db.insert(TEAM_TABLE_NAME, null, cv);
	}
	
	public boolean deleteDivision(int rowID)
	{
		return db.delete(DIV_TABLE_NAME, DIV_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor getAllDivisions()
	{
		return db.query(DIV_TABLE_NAME, new String[]{ DIV_ROWID, DIV_NAME }, null, null, null, null, null);
	}
	
	public Cursor getDiv(int rowID) throws SQLException
	{
		Cursor cursor = db.query(true, DIV_TABLE_NAME, new String[]{ DIV_ROWID, DIV_NAME }, DIV_ROWID +"="+rowID, null, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateDiv(Division div)
	{
		ContentValues cv = new ContentValues();
		cv.put(DIV_NAME, div.getDivisionName());
		
		return db.update(DIV_TABLE_NAME, cv, DIV_ROWID + "=" + div.getDivisionID(), null) > 0;
	}
	
	public List<Division> getDivisions()
	{
		List<Division> divList = new ArrayList<Division>();
		String sSQL = "select id, divCode, divName from division order by divName";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			do{
				divList.add(new Division(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
			}while(cursor.moveToNext());
		}
		close();
		
		return divList;
	}
	
	public List<String> getDivisionsStr()
	{
		List<String> divList = new ArrayList<String>();
		String sSQL = "select divName from division order by divName";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			do{
				divList.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		close();
		
		return divList;
	}
	
	//endregion Division SQL Commands
	
	//region Team SQL Commands

	public long insertTeam(Team team)
	{
		ContentValues cv = new ContentValues();
		cv.put(TEAM_ROWID, team.getTeamID());
		cv.put(TEAM_NAME, team.getTeamName());
		cv.put(TEAM_DIV_ID, team.getDivision().getDivisionID());
		
		return db.insert(TEAM_TABLE_NAME, null, cv);
	}
	
	public boolean deleteTeam(int rowID)
	{
		return db.delete(TEAM_TABLE_NAME, TEAM_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor getAllTeams()
	{
		return db.query(TEAM_TABLE_NAME, new String[]{ TEAM_ROWID, TEAM_NAME }, null, null, null, null, null);
	}
	
	public Cursor getTeam(int rowID) throws SQLException
	{
		Cursor cursor = db.query(true, TEAM_TABLE_NAME, new String[]{ TEAM_ROWID, TEAM_NAME }, TEAM_ROWID +"="+rowID, null, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public int getTeamID(String team){
		int iTeamID = -1;
		
		String sSQL = "select t.id "+
				"from team t "+
				"where t.teamName='"+team+"'";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			iTeamID = cursor.getInt(0);
		}
		close();
		
		return iTeamID;
	}
	
	public boolean updateTeam(Team team)
	{
		ContentValues cv = new ContentValues();
		cv.put(TEAM_NAME, team.getTeamName());
		
		return db.update(TEAM_TABLE_NAME, cv, TEAM_ROWID + "=" + team.getTeamID(), null) > 0;
	}
	
	public List<Team> getTeams()
	{
		List<Team> teamList = new ArrayList<Team>();
		String sSQL = "select t.id, t.teamName, d.id, d.divCode, d.divName "+
				"from team t "+
				"join division d on d.id=t.divID "+
				"order by t.teamName";
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			do{
				teamList.add(new Team(cursor.getInt(0), cursor.getString(1), 
						new Division(cursor.getInt(2), cursor.getString(3), cursor.getString(4))));
			}while(cursor.moveToNext());
		}
		close();
		
		return teamList;
	}
	
	public List<String> getTeamsByDivision(String divCode)
	{
		List<String> teamList = new ArrayList<String>();
		String sSQL = "select t.teamName "+
				"from team t "+
				"join division d on d.id=t.divID "+
				"where d.divName='"+divCode+"' "+
				"order by t.teamName";
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			do{
				teamList.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		close();
		
		return teamList;
	}
	
	public int getTeamCount()
	{
		String sSQL = "select count(*) from team";
		int iCount = 0;
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iCount = cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		return iCount;
	}
	
	//endregion Team SQL Commands

	//region Round SQL Commands
	public long insertRound(Round round)
	{
		ContentValues cv = new ContentValues();
		cv.put(ROUND_ROWID, round.getRoundID());
		cv.put(ROUND_NAME, round.getRoundName());
		
		return db.insert(ROUND_TABLE_NAME, null, cv);
	}
	
	public boolean deleteRound(int rowID)
	{
		return db.delete(ROUND_TABLE_NAME, ROUND_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor getAllRounds()
	{
		return db.query(ROUND_TABLE_NAME, new String[]{ ROUND_ROWID, ROUND_NAME }, null, null, null, null, null);
	}
	
	public Cursor getRound(int rowID) throws SQLException
	{
		Cursor cursor = db.query(true, ROUND_TABLE_NAME, new String[]{ ROUND_ROWID, ROUND_NAME }, ROUND_ROWID +"="+rowID, null, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateRound(Round round)
	{
		ContentValues cv = new ContentValues();
		cv.put(ROUND_NAME, round.getRoundName());
		
		return db.update(ROUND_TABLE_NAME, cv, ROUND_ROWID + "=" + round.getRoundID(), null) > 0;
	}
	
	public List<Round> getRounds()
	{
		List<Round> roundList = new ArrayList<Round>();
		
		String sSQL = "select id, roundName from round order by roundName";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			do{
				roundList.add(new Round(cursor.getInt(0), cursor.getString(1)));
			}while(cursor.moveToNext());
		}
		close();
		
		return roundList;
	}
	//endregion Round SQL Commands
	
	//region Game SQL Commands
	public long insertGame(Game game)
	{
		ContentValues cv = new ContentValues();
		cv.put(GAME_DATE, game.getDate());
		cv.put(GAME_TIME, game.getTime());
		cv.put(GAME_DATETIME, game.getDateTimeLong());		
		cv.put(GAME_DIAMOND, game.getDiamond());
		cv.put(GAME_HOME_TEAM, game.getHomeTeam());
		cv.put(GAME_AWAY_TEAM, game.getAwayTeam());
		cv.put(GAME_HOME_SCORE, game.getHomeScore());
		cv.put(GAME_AWAY_SCORE, game.getAwayScore());
		
		return db.insert(GAME_TABLE_NAME, null, cv);
	}
	
	public boolean deleteGame(int rowID)
	{
		return db.delete(GAME_TABLE_NAME, GAME_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor getAllGames()
	{
		return db.query(GAME_TABLE_NAME, new String[]{ GAME_ROWID, GAME_DATE, GAME_TIME, GAME_DIAMOND, GAME_HOME_TEAM, GAME_AWAY_TEAM, GAME_HOME_SCORE, GAME_AWAY_SCORE, GAME_DATETIME }, null, null, null, null, null);
	}
	
	public Cursor getGame(int rowID) throws SQLException
	{
		Cursor cursor = db.query(true, GAME_TABLE_NAME, new String[]{ GAME_ROWID, GAME_DATE, GAME_TIME, GAME_DIAMOND, GAME_HOME_TEAM, GAME_AWAY_TEAM, GAME_HOME_SCORE, GAME_AWAY_SCORE, GAME_DATETIME }, GAME_ROWID +"="+rowID, null, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public void updateScore( int gameID, int homeScore, int awayScore){
		open();
		String sSQL = "update game set homeScore="+homeScore+", awayScore="+awayScore+" where id="+gameID+";";
		db.execSQL(sSQL);
		close();
	}
	
	public boolean updateGame(Game game)
	{
		ContentValues cv = new ContentValues();
		cv.put(GAME_DATE, game.getDate());
		cv.put(GAME_TIME, game.getTime());
		cv.put(GAME_DATETIME, game.getDateTimeLong());		
		cv.put(GAME_DIAMOND, game.getDiamond());
		cv.put(GAME_HOME_TEAM, game.getHomeTeam());
		cv.put(GAME_AWAY_TEAM, game.getAwayTeam());
		cv.put(GAME_HOME_SCORE, game.getHomeScore());
		cv.put(GAME_AWAY_SCORE, game.getAwayScore());
		
		return db.update(GAME_TABLE_NAME, cv, GAME_ROWID + "=" + game.getGameID(), null) > 0;
	}
	
	public List<Game> getGames()
	{
		List<Game> gameList = new ArrayList<Game>();
		
		String sSQL = "select id, gameDate, gameTime, diamond, homeTeam, awayTeam, homeScore, awayScore, gameDatetime "+
				"from game order by gameDatetime";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if(cursor.moveToFirst())
		{
			do{
				gameList.add(new Game(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7) ));
			}while(cursor.moveToNext());
		}
		close();
		
		return gameList;
	}
	
	public List<Game> getNextGames()
	{
		int iGameID = -1;
		long lGameDate = -1;
				
		String sSQL = "select id, gameDatetime from game where gameDatetime >= "+System.currentTimeMillis()+" order by gameDatetime";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				try{
					iGameID = cursor.getInt(0);
					lGameDate = cursor.getLong(1);
				}
				catch(Exception e){}
			}
		}
		close();
		
		
		
		List<Game> gameList = new ArrayList<Game>();
		
		sSQL = "select id, gameDate, gameTime, diamond, homeTeam, awayTeam, homeScore, awayScore, gameDatetime "+
				"from game "+
				"where id >= "+iGameID+" and gameDatetime <= "+(lGameDate+CONST_ONE_DAY)+" order by gameDatetime";
		
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if(cursor.moveToFirst())
		{
			do{
				gameList.add(new Game(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7) ));
			}while(cursor.moveToNext());
		}
		close();
		
		return gameList;
	}
	
	public int getGamesFromToday()
	{
		int iGameID = -1;
		long lGameDate = -1;
				
		String sSQL = "select id, gameDatetime from game where gameDatetime >= "+System.currentTimeMillis()+" order by gameDatetime";
		
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				try{
					iGameID = cursor.getInt(0);
					lGameDate = cursor.getLong(1);
				}
				catch(Exception e){}
			}
		}
		close();
		
		int iGames = 0;
		sSQL = "select count(*) from game where id >= "+iGameID+" and gameDatetime <= "+(lGameDate+CONST_ONE_DAY)+" order by gameDatetime";
		
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				try{
					iGames = cursor.getInt(0);
				}
				catch(Exception e){}
			}
		}
		close();
		
		
		return iGames;
	}
	
	public int getGameCount()
	{
		String sSQL = "select count(*) from game";
		int iCount = 0;
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iCount = cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		return iCount;
	}
	
	public String getRecord(String sTeam)
	{
		int iWin = 0;
		int iLoss = 0;
		int iTie = 0;
		
		//WINS
		String sSQL = "select count(*) "+
			"from game g "+
			"join team t on t.teamName=g.homeTeam "+
			"where homeScore > awayScore and t.teamName='"+sTeam+"'";
		open();
		Cursor cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iWin = cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		sSQL = "select count(*) "+
				"from game g "+
				"join team t on t.teamName=g.awayTeam "+
				"where awayScore > homeScore and t.teamName='"+sTeam+"'";
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iWin += cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
			
			
		//LOSSES
		sSQL = "select count(*) "+
				"from game g "+
				"join team t on t.teamName=g.homeTeam "+
				"where homeScore < awayScore and t.teamName='"+sTeam+"'";
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iLoss = cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		sSQL = "select count(*) "+
				"from game g "+
				"join team t on t.teamName=g.awayTeam "+
				"where awayScore < homeScore and t.teamName='"+sTeam+"'";
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iLoss += cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		
		//TIES
		sSQL = "select count(*) from game where homeScore = awayScore and homeScore <> 0";
		open();
		cursor = db.rawQuery(sSQL, null);
		
		if( cursor.moveToFirst())
		{
			try
			{
				iTie = cursor.getInt(0);
			}catch(Exception e){}
		}
		close();
		
		return iWin+"-"+iLoss+"-"+iTie;
	}
	//endregion Game SQL Commands
	
}













