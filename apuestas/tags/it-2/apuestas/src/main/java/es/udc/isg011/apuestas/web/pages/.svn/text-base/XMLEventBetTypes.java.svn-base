package es.udc.isg011.apuestas.web.pages;

import java.util.Calendar;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("application/xml")
public class XMLEventBetTypes {
	@Inject
	private Request request;
	

	@Property
	private Event event;
	
	@Property 
	private BetType betType;
	
	@Property
	private BetOption betOption;
	
	@Inject
	private BetService betService;
	
	//xmleventbettypes/eventid=1
	void onActivate(){
		String eventId = request.getParameter("eventId");
		
		Long myEventId;
		try{
			myEventId = Long.parseLong(eventId);
		} catch (NumberFormatException e) {
			event = null;
			return;
		}
		
		try {
			event = betService.obtainEvent(myEventId);
		} catch (InstanceNotFoundException e) {
			event = null;
			return;
		}
		
		if(event.getDate().before(Calendar.getInstance())){
			event = null;
		}
	}
}
