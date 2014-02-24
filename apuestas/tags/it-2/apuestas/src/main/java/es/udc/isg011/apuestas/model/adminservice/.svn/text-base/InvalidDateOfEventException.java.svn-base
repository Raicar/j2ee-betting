package es.udc.isg011.apuestas.model.adminservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidDateOfEventException extends Exception{
	
	private Long eventId;
	private String name;
	private Calendar date;

    public InvalidDateOfEventException(Long eventId, String name, 
    		Calendar date) {
        
        super("Invalid date to create an event exception => " +
            "eventId = " + eventId + " | " +
            "name = " + name + " | " +
            "date = " + date);
            
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        
    }
    
    public long getEventId() {
        return eventId;
    }
    
    public String getName() {
        return name;
    }
    
    public Calendar getDate() {
        return date;
    }

}