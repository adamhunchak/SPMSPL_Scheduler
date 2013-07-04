package com.cctb.spmspl_scheduler;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ScheduleBaseAdapter extends BaseAdapter {
	
	private static ArrayList<Game> gameArrayList;
	
	private Boolean bDisplayButton;
	private LayoutInflater mInflater;
	private Context context;
	protected ArrayList<MyScheduleClickEventListener> listeners = new ArrayList<MyScheduleClickEventListener>();

	public ScheduleBaseAdapter(Context context, ArrayList<Game> results) {
		gameArrayList = results;
        mInflater = LayoutInflater.from(context);
        this.context=context;
        bDisplayButton = true;
    }
	
	public void setDisplayButton( Boolean bDisplay )
	{
		bDisplayButton = bDisplay;
	}
 
    public int getCount() {
        return gameArrayList.size();
    }
 
    public Object getItem(int position) {
        return gameArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public void setOnMyScheduleClickEventListener(MyScheduleClickEventListener listener)
    {
    	listeners.add(listener);
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
        	if( bDisplayButton )
        		convertView = mInflater.inflate(R.layout.schedule_row_view, null);
        	else
        		convertView = mInflater.inflate(R.layout.game_row_view, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.sch_row_Date);
            holder.txtTime = (TextView) convertView.findViewById(R.id.sch_row_Time);
            holder.txtDiamond = (TextView) convertView.findViewById(R.id.sch_row_Diamond);
            holder.txtHome = (TextView) convertView.findViewById(R.id.sch_row_Home);
            holder.txtAway = (TextView) convertView.findViewById(R.id.sch_row_Away);
           
            if( bDisplayButton ){
	            holder.ibViewGame = (ImageButton) convertView.findViewById(R.id.sch_row_ViewGame);
	            holder.ibViewGame.setTag(position);  
	            holder.ibViewGame.setOnClickListener(new OnClickListener() {
	    			@Override
	    			public void onClick(View view) {
	    				int position = Integer.parseInt(view.getTag().toString());
	    				Game g = (Game)gameArrayList.get(position);		
	    				String sHome = g.getHomeTeam();
	    				String sAway = g.getAwayTeam();
	
	    				for( MyScheduleClickEventListener listener : listeners ){
	    					listener.scheduleClickEventOccured(g);
	    				}
	    			}
	    		});
            }
            
            convertView.setTag(holder);
        } 
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        Game g = gameArrayList.get(position);
        
        Calendar c = g.getcDate();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		String sFormattedDate = sdf.format(g.getDateTimeLong());
        
        holder.txtDate.setText(sFormattedDate);
        holder.txtTime.setText(g.getTime()+"PM");
        holder.txtDiamond.setText("Diamond #"+g.getDiamond());
        holder.txtHome.setText("Home: "+g.getHomeTeam());
        holder.txtAway.setText("Away: "+g.getAwayTeam());        
        
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtDate;
        TextView txtTime;
        TextView txtDiamond;
        TextView txtHome;
        TextView txtAway;
        ImageButton ibViewGame;
    }
}
