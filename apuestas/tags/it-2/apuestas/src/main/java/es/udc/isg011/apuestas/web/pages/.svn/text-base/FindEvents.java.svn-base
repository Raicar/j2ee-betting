package es.udc.isg011.apuestas.web.pages;

import java.util.ArrayList;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.category.Category;
import es.udc.isg011.apuestas.model.userservice.UserService;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEvents {

    @Property
    private String keyWords;
    @Property
    private Long category;

    @Inject
    private AdminService adminService;
    @Inject
    private UserService userService;
    @Inject
    private Messages messages;


	@Component
	private Form findEventsForm;
	@Component(id="category")
	private Select categoryField;

	@InjectPage
	private ShowEvents showEvents;
	
    public String getCategories() {
    	ArrayList<Category> categoryList = (ArrayList<Category>) userService.obtainCategories();
    	String categories="";

    	for (Category category : categoryList) {
    		categories+=category.getCategoryId()+"="+category.getName()+", ";
		}
    	categories = categories.substring(0, categories.length()-1); // avoid final comma which means empty entry in select

    	return categories;
    }
    
    void onValidateFromFindEventsForm() {
    	if(!findEventsForm.isValid()) 
    		return;
    	if(category!=null&&!userService.existsCategory(category))
    		findEventsForm.recordError(categoryField, messages.format("error-categoryNotFound"));		
    }
    
    Object onSuccess() {
    	if(category==null) showEvents.setCategoryId(null);
    	else showEvents.setCategoryId(category.toString());
    	showEvents.setKeyWords(keyWords);
    	showEvents.setNewSearch(true);
    	
        return showEvents;
    }
}
