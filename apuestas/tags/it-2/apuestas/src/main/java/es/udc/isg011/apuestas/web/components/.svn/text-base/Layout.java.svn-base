package es.udc.isg011.apuestas.web.components;

import java.util.Locale;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PersistentLocale;

import es.udc.isg011.apuestas.web.pages.Index;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.CookiesManager;
import es.udc.isg011.apuestas.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Layout {
    @Property
    @Parameter(required = false, defaultPrefix = "message")
    private String menuExplanation;

	@Property
	@Parameter(required = false, defaultPrefix = "message")
	private String pageExplanation;

    @Property
    @Parameter(required = true, defaultPrefix = "message")
    private String pageTitle;

    @Property
    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;
    
    
    @Inject
	private Locale locale;
	
	@Inject
	private PersistentLocale persistentLocale;
    

    @AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED)
   	Object onActionFromLogout() {
        userSession = null;
        CookiesManager.removeCookies(cookies);
        return Index.class;
	}
    
    void onLanguage(String code) {
		persistentLocale.set(new Locale(code));
	}
    
}