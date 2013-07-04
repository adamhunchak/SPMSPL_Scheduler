package com.cctb.spmspl_scheduler;

import java.util.EventListener;
import java.util.EventObject;

public class ScheduleClickEvent extends EventObject {
	
	private Game g;
	
	public ScheduleClickEvent(Object source, Game g)
	{
		super(source);
		this.g = g;
	}
}

interface MyScheduleClickEventListener {
	public void scheduleClickEventOccured( Game g );
}
