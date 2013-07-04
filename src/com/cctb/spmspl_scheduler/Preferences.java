package com.cctb.spmspl_scheduler;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity 
{ 
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		
		PreferenceManager prefMgr = getPreferenceManager(); 
		prefMgr.setSharedPreferencesName("SPMSPL_pref");
		
		//---load the preferences from an XML file--- 
		addPreferencesFromResource(R.xml.my_prefs);
	}
}
