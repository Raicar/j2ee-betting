package es.udc.isg011.apuestas.web.pages.admin;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.adminservice.InvalidDateOfEventException;
import es.udc.isg011.apuestas.model.category.Category;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.model.userservice.UserService;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINS)
public class AddEvent {

    @Property
    private String eventName;

    @Property
    private String eventDate;

    @Property
    private long category;

    @Property
    private int hour;
    @Property
    private int minute;
    
    @Inject
    private AdminService adminService;
    @Inject
    private UserService userService;
    @Inject
    private Messages messages;
    
	@Component
	private Form addEventForm;
	@Component(id="eventDate")
	private TextField eventDateField;
	
	@Component(id="category")
	private Select categoryField;
	
	private Event eventCreated;
	
	@InjectPage
	private EventCreated eventCreatedPage;
	
	@Inject
	private Locale locale;
	
    public String getCategories() {
    	ArrayList<Category> categoryList = (ArrayList<Category>) userService.obtainCategories();
    	String categories="";
    	for (Category category : categoryList) {
    		categories+=category.getCategoryId()+"="+category.getName()+", ";
		}
    	categories = categories.substring(0, categories.length()-1); // avoid final coma which means empty entry in select

    	return categories;

    }
    public String getHours() {
    	String hours="";
    	for (int i = 0; i < 24; i++) {
			if(i<10)hours+=i+"=0"+i+", ";
			else hours+=i+"="+i+", ";
		}
		hours = hours.substring(0, hours.length()-1);

    	return hours;
    }
    
    public String getMinutes() {
    	String minutes="";
    	for (int i = 0; i < 60; i++) {
			if(i<10)minutes+=i+"=0"+i+", ";
			else minutes+=i+"="+i+", ";
		}
		minutes = minutes.substring(0, minutes.length()-1);

    	return minutes;
    }

    void onValidateFromAddEventForm() {
    	
    	if (!addEventForm.isValid())
			return;
    	
    	Date d = validateDate(eventDateField, eventDate);
    	
    	if(d==null) return;
    	Calendar eventCalendar = Calendar.getInstance();
    	eventCalendar.setTime(d);
    	
    	try {
    		eventCalendar.set(Calendar.HOUR, hour);
    		eventCalendar.set(Calendar.MINUTE, minute);
    		eventCreated = adminService.addEvent(new Event(eventName, eventCalendar), category);
    	} catch (InstanceNotFoundException e) {
    		addEventForm.recordError(categoryField, messages.format("error-categoryNotFound"));
    		return;
    	} catch (InvalidDateOfEventException e) {//date in the past
    		addEventForm.recordError(messages.format("error-invalidDateOfEvent",eventDate));
    		return;
		}
    }
    
    private Date validateDate(TextField dateField, String dateAsString) {
		
		ParsePosition position = new ParsePosition(0);//SOLO FUNCIONA CON SHOET
		Date date = DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(dateAsString, position);
		if((date==null) || (position.getIndex() != dateAsString.length()) ) {
			addEventForm.recordError(dateField,messages.format("error-incorrectDateFormat", dateAsString));
			return null;
		}
		return date;
	}
    
   Object onSuccess() {
    	eventCreatedPage.setEventId(eventCreated.getEventId().toString());
        return eventCreatedPage;
    }
    
    
   

		
	
    
    
    
}