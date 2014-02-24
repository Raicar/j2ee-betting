package es.udc.isg011.apuestas.model.betservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class BetAfterEventStartedException extends Exception {
	private long eventId;
    private Calendar eventDate;
    private Calendar currentDate;

    public BetAfterEventStartedException(long eventId, Calendar eventDate, Calendar currentDate) {
        
        super("Bet After Event Started => " +
            "eventId = " + eventId + " | " +
            "eventDate = " + eventDate + " | " +
            "currentDate = " + currentDate);
            
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.currentDate = currentDate;
        
    }
    
    public long getEventId() {
        return eventId;
    }
    
    public Calendar getEventDate() {
        return eventDate;
    }
    
    public Calendar getCurrentDate() {
        return currentDate;
    }

}
