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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ScheduleView extends Fragment {

	private DBAdapter db;	
	private ListView lvSchedule;
	private ArrayList<Game> results;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_schedule_view, container, false);		
		
		initializeDB();
		
		lvSchedule = (ListView)rootView.findViewById(R.id.m_lv_Schedule);     
		
		new LoadScheduleTask().execute();
		
		return rootView;
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		new LoadScheduleTask().execute();
		
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
	
	private class LoadScheduleTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			LoadSchedule();			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){			
			populateList();
		}
	}
	
	private void LoadSchedule(){
		
		results = null;
		results = new ArrayList<Game>();
		Game g;
		db.open();
		
		List<Game> gameList = db.getGames();
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
