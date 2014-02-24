package es.udc.isg011.apuestas.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("application/xml")
public class XMLShowEvents {

	@Inject
	private Request request;
	
	@Property
	private List<Event> events;

	@Property
	private Event event;
	
	@Property
	private int totalNumber;
	
	
	@Inject
	private BetService betService;
	
	//xmlshowevents/keywords=a+asdf+adf+asd
	void onActivate(){
		String keyWords = request.getParameter("keywords");
		
		try {
			totalNumber = betService.getNumberOfEvents(keyWords, null);
		} catch (InstanceNotFoundException e) {
			// Never, category null
			return;
		}
		try {
			events = betService.findEvents(keyWords, null, 0, totalNumber);
		} catch (InstanceNotFoundException e) {
			// Never, category null
		}
	}
	
}
