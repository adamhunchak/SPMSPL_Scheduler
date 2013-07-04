package com.cctb.spmspl_scheduler;

public enum ScheduleDate {

	MONDAY(2), 
	TUESDAY(3), 
	WEDNESDAY(4), 
	THURSDAY(5), 
	FRIDAY(6), 
	SATURDAY(7), 
	SUNDAY(1),
	
	JAN(1), 
	FEB(2), 
	MAR(3), 
	APR(4), 
	MAY(5), 
	JUN(6), 
	JUL(7), 
	AUG(8), 
	SEP(9), 
	OCT(10), 
	NOV(11), 
	DEC(12);
	
	private int iValue;
	
	private ScheduleDate(int value)
	{
		//Calendar cal2 = Calendar.getInstance();
        //cal2.set(1957, 6, 21);
		this.iValue = value;
	}
	
	public int value() {
        return iValue;
    }
	
}