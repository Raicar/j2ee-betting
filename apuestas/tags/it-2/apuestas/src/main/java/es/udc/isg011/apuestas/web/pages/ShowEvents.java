package es.udc.isg011.apuestas.web.pages;



import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.EventsGridDataSource;
import es.udc.isg011.apuestas.web.util.UserSession;




@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class ShowEvents {
	private final static int ROWS_PER_PAGE = 10;

	@Persist//JUSTIFICACIÓN: si sigues navegando todas las pags siguientes lo van a tener que recibir para que al volver atrás se pueda
	private String keyWords;

	@Persist
	private String categoryId;
	
	@Property
	private Event event;

	@Inject
	private AdminService adminService;
	@Inject
	private BetService betService;

	@Property
	private EventsGridDataSource eventsGridDataSource;
	
    @SessionState(create=false)
    private UserSession userSession;

	@Inject
	private Locale locale;
	
	private boolean newSearch;

	public void setNewSearch(boolean newSearch) {
		this.newSearch = newSearch;
	}

    public String getDateFormatted(Event event){
    	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    	DateFormat dh = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    	
    	String str = df.format(event.getDate().getTime());
    	str+=" "+dh.format(event.getDate().getTime());
    	
	   	return str;
    }

	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String category) {
		this.categoryId = category;
	}

	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}
	
	@InjectComponent
	private Grid grid;
	
	void onActivate(String categoryId, String keyWords, boolean newSearch){
		Long myCategoryId;
		try{
			if(categoryId==null) myCategoryId=null;
			else myCategoryId = Long.parseLong(categoryId);
		} catch (NumberFormatException e) {
			myCategoryId = (long) -1;
		}
		this.keyWords = keyWords;
		this.categoryId = categoryId;
		if((userSession!=null) && userSession.isAdmin())
			eventsGridDataSource = new EventsGridDataSource(adminService, myCategoryId, keyWords);
		else
			eventsGridDataSource = new EventsGridDataSource(betService, myCategoryId, keyWords);
		this.newSearch = newSearch;
		if(newSearch){
			this.newSearch=false;
			grid.setCurrentPage(1);
		}
	}

	Object[] onPassivate() {
		return new Object[] {categoryId, keyWords, newSearch};
	}

}
