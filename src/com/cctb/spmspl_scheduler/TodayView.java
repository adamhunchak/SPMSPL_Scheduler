package com.cctb.spmspl_scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TodayView extends Fragment {

	private DBAdapter db;	
	private ListView lvSchedule;
	private TextView tvTeam;
	private TextView tvRecord;
	private ArrayList<Game> results;
	private SharedPreferences prefs;
	private String sSetTeam = "";
	private TextView tvMessage;
	private boolean bGamesExist = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = null;
		initializeDB();
		
		if( db.getGameCount() > 0 ) {
			rootView = inflater.inflate(R.layout.fragment_today_view, container, false);	
			tvTeam = (TextView)rootView.findViewById(R.id.tvTeamName);
			tvRecord = (TextView)rootView.findViewById(R.id.tvRecord);
			lvSchedule = (ListView)rootView.findViewById(R.id.m_lv_Schedule);     
			bGamesExist = true;
			
			refreshData();
		}
		else
		{
			rootView = inflater.inflate(R.layout.fragment_welcome_no_games, container, false);
			tvMessage = (TextView)rootView.findViewById(R.id.tvMessage);
			tvMessage.setText("No Games Exist To Display.\r\nSelect To Load The Schedule From The Menu.");
			bGamesExist = false;
		}
		
		return rootView;
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		refreshData();	
	}
	
	private void refreshData()
	{
		if( bGamesExist )
		{
			initializePref();
			//tvRecord.setText(db.getRecord(sSetTeam));
			
			new LoadScheduleTask().execute();
		}
	}
	
	private void initializePref()
	{
		prefs = getActivity().getSharedPreferences("com.cctb.spmspl_scheduler.team_pref", 0); 
		sSetTeam = prefs.getString("editTextTeamPref", "");
		tvTeam.setText(sSetTeam);
	}
	
	private void initializeDB()
	{
    	db = new DBAdapter(getActivity());
	}
	
	private void populateList()
	{
		ScheduleBaseAdapter sba = new ScheduleBaseAdapter(getActivity(), results);
        sba.setOnMyScheduleClickEventListener(new MyScheduleClickEventListener() 
        {
			@Override
			public void scheduleClickEventOccured(Game g) {
				Intent i = new Intent(getActivity(), GameView.class);
				i.putExtra("Game", g);
				startActivity(i);
			}
        });
        lvSchedule.setAdapter(sba);
	}
	
	private void displayRecord(String sRecord)
	{
		tvRecord.setText(sRecord);
	}
	
	private class LoadScheduleTask extends AsyncTask<Void, Void, Void> {

		private String sRecord = "0-0-0";
		@Override
		protected Void doInBackground(Void... params) {
			
			loadSchedule();		
			getTeamRecord();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){			
			populateList();
			displayRecord(sRecord);
		}
	
		private void getTeamRecord()
		{
			sRecord = db.getRecord(sSetTeam);
		}
		
		private void loadSchedule(){
			
			results = null;
			results = new ArrayList<Game>();
			Game g;
			db.open();
			
			List<Game> gameList = db.getNextGames();
			if( gameList != null)
			{
				for( int i = 0; i < gameList.size(); i++ )
				{
					g = (Game)gameList.get(i);
				    results.add(g);
				}
			}
			
			db.close();
		}
	}

}
