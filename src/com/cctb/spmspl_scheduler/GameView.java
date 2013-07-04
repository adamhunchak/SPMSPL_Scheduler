package com.cctb.spmspl_scheduler;

import java.util.ArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameView extends Activity implements OnClickListener {

	private final int CONST_EMAIL_MENU_ID = 0;
	private DBAdapter db;
	private EditText txtHome;
	private EditText txtAway;
	private Game gCurrentGame;
	private Button btnSave;
	private SharedPreferences prefs;
	private SharedPreferences teamPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_view);
		
		txtHome = (EditText)findViewById(R.id.txtHome);
		txtAway = (EditText)findViewById(R.id.txtAway);
		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		initializeDB();
		initializePref();
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		gCurrentGame = (Game)getIntent().getSerializableExtra("Game");
		
		ArrayList<Game> gameResults = new ArrayList<Game>();
		gameResults.add(gCurrentGame);
		 
        final ListView lv = (ListView) findViewById(R.id.m_lv_Game);
        ScheduleBaseAdapter sba = new ScheduleBaseAdapter(this, gameResults);
        sba.setDisplayButton(false);
        lv.setAdapter(sba);
        
        loadScore();
	}

	@Override
	public void onClick(View v) {
		Button b = (Button)v;
		switch( b.getId() )
		{
			case R.id.btnSave :
				saveScore();
				break;
			default :
				break;
		}	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_view, menu);
		createEmailMenu(menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				Intent i = new Intent(this, Welcome.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(GameView.this, R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
				startActivity(i, translateBundle);
				break;
			case R.id.action_quit_GV :
				quit();
				break;
			case CONST_EMAIL_MENU_ID:
				sendEmail();
				break;
			default:
				return super.onOptionsItemSelected(item);				
		}
		return true;
	}
	
	private void initializeDB()
	{
    	db = new DBAdapter(this);
	}
	
	private void initializePref()
	{
		prefs = getSharedPreferences("SPMSPL_pref", MODE_PRIVATE); 
		teamPrefs = getSharedPreferences("com.cctb.spmspl_scheduler.team_pref", MODE_PRIVATE);
	}
	
	private void saveScore()
	{
		int iHome = -1;
		int iAway = -1;
		try{
			iHome = Integer.parseInt(txtHome.getText().toString());
		}
		catch(Exception e){
			return;
		}
		
		try{
			iAway = Integer.parseInt(txtAway.getText().toString());
		}
		catch(Exception e){
			return;
		}
		
		int iGameID = gCurrentGame.getGameID();
		db.updateScore(iGameID, iHome, iAway);
		gCurrentGame.setHomeScore(iHome);
		gCurrentGame.setAwayScore(iAway);

		Toast.makeText(this, "Score Saved", Toast.LENGTH_SHORT).show();
	}
	
	private void loadScore(){
		String sHome = new Integer(gCurrentGame.getHomeScore()).toString();
		String sAway = new Integer(gCurrentGame.getAwayScore()).toString();
		
		if( sHome.equals("0") )
			sHome = "";
		
		if( sAway.equals("0") )
			sAway = "";
		txtHome.setText(sHome);
        txtAway.setText(sAway);
	}
	
	private void sendEmail()
	{
		String sToEmail = prefs.getString("editTextEmailPref", "");
		if( sToEmail.equals(""))
			sToEmail = "scores@spmspl.com";
		
		String sTeam = teamPrefs.getString("editTextTeamPref", "");
		if( sTeam.equals(""))
			sTeam = "Team";
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);		
		emailIntent.setData(Uri.parse("mailto:"));
		String[] to = new String[] {sToEmail};
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, sTeam+" Score Submission");
		emailIntent.putExtra(Intent.EXTRA_TEXT, getScoreText());
		emailIntent.setType("message/rfc822");			
		//startActivity(Intent.createChooser(emailIntent, "Email"));
		startActivity(emailIntent);
	}
	
	private String getScoreText(){
		String sMessage = "";
		int iHome = gCurrentGame.getHomeScore();
		int iAway = gCurrentGame.getAwayScore();
		
		if( iHome > iAway )
			sMessage = gCurrentGame.getHomeTeam()+" won.\r\n\r\n";
		else if( iHome < iAway )
			sMessage = gCurrentGame.getAwayTeam()+" won.\r\n\r\n";
		else
			sMessage = "Game ended in a tie.\r\n\r\n";
		
		sMessage += gCurrentGame.getHomeTeam()+": "+gCurrentGame.getHomeScore()+"\r\n";
		sMessage += gCurrentGame.getAwayTeam()+": "+gCurrentGame.getAwayScore();
		
		return sMessage;
	}
	
	private void createEmailMenu(Menu menu)
	{
		MenuItem mnu1 = menu.add(0, 0, 0, "Send Email");
		{
			mnu1.setIcon(R.drawable.email);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}
	
	private void quit()
	{	
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage("Are you sure you want to exit?").setCancelable(false);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GameView.this.moveTaskToBack(true);			
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

}
