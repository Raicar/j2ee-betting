package es.udc.isg011.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class EventsGridDataSource implements GridDataSource{
	private BetService betService;
	private AdminService adminService;
	private Long categoryId;
	private String keyWords;
	private List<Event> events;
	private int totalNumberOfEvents;
	private int startIndex;

	public EventsGridDataSource(BetService betService, Long categoryId, 
			String keyWords) {

		this.betService = betService;
		this.categoryId = categoryId;
		this.keyWords = keyWords;
		this.adminService = null;
	
		try {
			totalNumberOfEvents = this.betService.getNumberOfEvents(keyWords, categoryId);
		} catch (InstanceNotFoundException e) {

		}
	}
	
	public EventsGridDataSource(AdminService adminService, Long categoryId, 
			String keyWords) {

		this.adminService = adminService;
		this.categoryId = categoryId;
		this.keyWords = keyWords;
		this.betService = null;
	
		try {
			totalNumberOfEvents = this.adminService.getNumberOfEvents(keyWords, categoryId);
		} catch (InstanceNotFoundException e) {

		}
	}
	
    public int getAvailableRows() {
        return totalNumberOfEvents;
    }

    public Class<Event> getRowType() {
        return Event.class;
    }

    public Object getRowValue(int index) {
        return events.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {
    	try {
			if(adminService==null)
				events = betService.findEvents(keyWords, categoryId, startIndex, endIndex-startIndex+1);
			else 
				events = adminService.findEvents(keyWords, categoryId, startIndex, endIndex-startIndex+1);
		} catch (InstanceNotFoundException e) {
		}
        this.startIndex = startIndex;

    }

}
