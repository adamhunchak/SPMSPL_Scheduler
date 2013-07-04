package com.cctb.spmspl_scheduler;

import java.util.List;
import java.util.Vector;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoadSchedule extends Activity implements OnClickListener {
	
	private DBAdapter db;
	private Spinner spDivision;
	private Spinner spTeam;
	private Dialog messageDialog;
	private Button btnLoad;
	private SharedPreferences prefs;
	private boolean bLoadingPrefs = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_load_schedule);
		
		showMessageDialog("Loading Teams...");
		
		initializeDB();
		initializePref();
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		btnLoad = (Button)findViewById(R.id.btnLoad);
		btnLoad.setOnClickListener(this);
		
		spDivision = (Spinner)findViewById(R.id.spinnerDiv);
		spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				loadTeams(parent.getItemAtPosition(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        });
		
		spTeam = (Spinner)findViewById(R.id.spinnerTeam);
		
		new DownloadTeamsTask().execute();
	}
	
	@Override
	public void onClick(View v) {
		Button b = (Button)v;
		switch( b.getId() )
		{
			case R.id.btnLoad :
				loadSchedule();
				break;
			default :
				break;
		}	
	}
	
	private void initializePref()
	{
		prefs = getSharedPreferences("com.cctb.spmspl_scheduler.team_pref", 0); 
	}
	
	private void loadSchedule(){
		int iPos = spDivision.getSelectedItemPosition();
		if( iPos == -1){		
			Toast.makeText(getApplicationContext(), "A division needs to be selected before the schedule can load.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		iPos = spTeam.getSelectedItemPosition();
		if( iPos == -1){		
			Toast.makeText(getApplicationContext(), "A team needs to be selected before the schedule can load.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Editor edit = prefs.edit();
		edit.putString("editTextTeamPref", spTeam.getSelectedItem().toString());
		edit.putString("editTextDivPref", spDivision.getSelectedItem().toString());
		edit.commit();
		
		downloadSchedule();
	}
	
	private void downloadSchedule()
	{
		showMessageDialog("Downloading Schedule...");
		int iTeamID = getSelectedTeamID();
		new DownloadScheduleTask().execute(iTeamID);
	}
	
	private int getSelectedTeamID(){
		String sTeam = spTeam.getSelectedItem().toString();
		return db.getTeamID(sTeam);
	}
	
	private void loadTeams(String sDiv){
		db.open();
    	List<String> lDiv = db.getTeamsByDivision(sDiv);
    	db.close();
    	
		// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item, lDiv);
		// Apply the adapter to the spinner
		spTeam.setAdapter(teamAdapter);
		
		if(bLoadingPrefs)
		{
			spTeam.setSelection(teamAdapter.getPosition(prefs.getString("editTextTeamPref", "")));
			bLoadingPrefs = false;
		}
	}
	
	public void showMessageDialog(String sMessage)
	{
		messageDialog = new Dialog(this, R.style.loading_dialog);
		messageDialog.setContentView(R.layout.loading_dialog);
	    ((TextView)messageDialog.findViewById(R.id.dialogText)).setText(sMessage);
	 
	    messageDialog.show();
	}
	
	public void hideMessageDialog() {
	    if (messageDialog != null)
	    	messageDialog.dismiss();
	 
	    messageDialog = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_schedule, menu);
		return true;
	}

	//region <-- Menu -->
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				Intent i = new Intent(this, Welcome.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(LoadSchedule.this, R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
				startActivity(i, translateBundle);
				break;
			case R.id.action_quit_LS :
				quit();
				break;	
			default:
				return super.onOptionsItemSelected(item);				
		}
		return true;
	}
		
	private void quit()
	{	
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage("Are you sure you want to exit?").setCancelable(false);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LoadSchedule.this.moveTaskToBack(true);			
			}
		});
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();			
			}
		});
		AlertDialog ad = adb.create();
		ad.show();
	}
	
	//endregion <-- Menu -->

	private void initializeDB()
	{
    	db = new DBAdapter(this);
	}
	private void loadDivision()
	{
		db.open();
    	List<String> lDiv = db.getDivisionsStr();
    	db.close();
    	
		// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<String> divAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item, lDiv);
		// Apply the adapter to the spinner
		spDivision.setAdapter(divAdapter);
		
		bLoadingPrefs = true;
		spDivision.setSelection(divAdapter.getPosition(prefs.getString("editTextDivPref", "")));
	}
	
	private class DownloadTeamsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			int iCnt = 0;
			db.open();
			iCnt = db.getTeamCount();
			db.close();
			
			if( iCnt == 0 )
				DownloadTeams();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){
			loadDivision();
			hideMessageDialog();
		}
	}
	
	private void DownloadTeams(){
		
		Crawler c = new Crawler("http://spmspl.com/");
		c.loadScheduleProperties();
		Vector vTeams = c.loadTeams();
		
		db.open();
		for( int i = 0; i < vTeams.size(); i++ ){
			Team t = (Team)vTeams.get(i);
			t.getTeamName();
	    	db.insertTeam(t);
		}
		db.close();
	}
	
	private class DownloadScheduleTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... teamIDs) {
			
			DownloadSchedule(teamIDs[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){			
			hideMessageDialog();
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private void DownloadSchedule(int teamID){
		Crawler c = new Crawler("http://spmspl.com/");		
		Vector vGames = c.loadSchedule(1,teamID);
		
		db.open();
		for( int i = 0; i < vGames.size(); i++ ){
			Game g = (Game)vGames.get(i);			
	    	db.insertGame(g);
		}
		db.close();
	}
}




