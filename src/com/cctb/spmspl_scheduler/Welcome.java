package com.cctb.spmspl_scheduler;

//region imports
import java.util.List;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//endregion imports

public class Welcome extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final int TODAY_FRAGMENT = 0;
	private static final int FULL_SCHEDULE_FRAGMENT = 1;
	private static final int LOAD_SCHEDULE_REQUEST = 0;
	private DBAdapter db;
	private Dialog messageDialog;
	private TextView tvMessage;
	private SharedPreferences teamPrefs;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);		
		
		initializeDB();
		initializePref();
		
		tvMessage = (TextView)findViewById(R.id.textView1);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_welcome),
								getString(R.string.title_full_schedule), }), this);
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
	
	private void initializeDB()
	{
    	db = new DBAdapter(this);
	}
	
	private void initializePref()
	{
		teamPrefs = getSharedPreferences("com.cctb.spmspl_scheduler.team_pref", MODE_PRIVATE);
		prefs = getSharedPreferences("SPMSPL_pref", MODE_PRIVATE);
	}
	
	private void displayNextGame(){
		tvMessage.setText("Next game will be...");
	}
	
	private void displayNoGames(){
		tvMessage.setText("No Games Exist To Display.");
	}
	
	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		if( position == TODAY_FRAGMENT ) 
		{
			tvMessage.setText("");
			Fragment fragment = new TodayView();
			getFragmentManager().beginTransaction()
					.replace(R.id.container, fragment, "FRAGMENT_TODAY_VIEW").commit();
			
			return true;
		}
		else if( position == FULL_SCHEDULE_FRAGMENT )
		{
			tvMessage.setText("");
			Fragment fragment = new ScheduleView();
			getFragmentManager().beginTransaction()
					.replace(R.id.container, fragment, "FRAGMENT_SCHEDULE_VIEW").commit();
			return true;
		}
	
		return false;
	}

	//region <-- Menu -->
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch( item.getItemId() )
		{
			case R.id.action_download :
				downloadSchedule();
				break;
			case R.id.action_reset :
				resetApp();
				break;
			case R.id.action_quit :
				quit();
				break;
			case R.id.action_settings :
				settings();
				break;
			default :
				break;
		}	
		return true;
	}
	
	private void downloadSchedule()
	{
		Intent i = new Intent(Welcome.this, LoadSchedule.class);
		Bundle translateBundle = ActivityOptions.makeCustomAnimation(Welcome.this, R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
		//startActivity(i, translateBundle);
		startActivityForResult(i, LOAD_SCHEDULE_REQUEST, translateBundle );
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_SCHEDULE_REQUEST) {
            if (resultCode == RESULT_OK) {
            	onNavigationItemSelected(TODAY_FRAGMENT, 0);
            }
        }
    }
	
	private void resetApp()
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage("This will clear all schedule, game and team data. Are you sure you want to reset?").setCancelable(false);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.resetTables();	
				
				Editor edit = teamPrefs.edit();
				edit.putString("editTextTeamPref", "");
				edit.putString("editTextDivPref", "");
				edit.commit();
				
				edit = prefs.edit();
				edit.putString("editTextEmailPref", "scores@spmspl.com");
				edit.commit();
				
				FragmentManager fm = getFragmentManager();
				Fragment fragment = fm.findFragmentByTag("FRAGMENT_TODAY_VIEW");
				try{
					TodayView tv = (TodayView)fragment;
					tv.onResume();
				}
				catch(Exception e){}
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
	
	private void quit()
	{	
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage("Are you sure you want to exit?").setCancelable(false);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Welcome.this.finish();					
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
	
	private void settings()
	{
		Intent i = new Intent("com.cctb.spmspl_scheduler.Preferences"); 
		startActivity(i);
	}
	
	//endregion <-- Menu -->

	private class InitTask extends AsyncTask<Void, Void, Void> {

		private boolean gamesExist = false;
		@Override
		protected Void doInBackground(Void... params) {
			if( db.getGamesFromToday() > 0 )
				gamesExist = true;
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){
			
			if( gamesExist )
				displayNextGame();
			else
				displayNoGames();
		}
	}
	
}
