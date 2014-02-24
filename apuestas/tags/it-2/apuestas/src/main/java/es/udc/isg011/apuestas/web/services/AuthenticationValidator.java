package es.udc.isg011.apuestas.web.services;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

import es.udc.isg011.apuestas.web.util.UserSession;

public class AuthenticationValidator {

	private final static String LOGIN_PAGE = "user/Login";

	private final static String INIT_PAGE = "Index";

	public static final String PAGE_AUTHENTICATION_TYPE = "page-authentication-type";
	public static final String EVENT_HANDLER_AUTHENTICATION_TYPE = "event-handler-authentication-type";

	public static String checkForPage(String pageName,
			ApplicationStateManager applicationStateManager,
			ComponentSource componentSource, MetaDataLocator locator) {

		String redirectPage = null;
		Component page = componentSource.getPage(pageName);
		try {
			String policyAsString = locator.findMeta(PAGE_AUTHENTICATION_TYPE,
					page.getComponentResources(), String.class);

			AuthenticationPolicyType policy = AuthenticationPolicyType
					.valueOf(policyAsString);
			redirectPage = check(policy, applicationStateManager);
		} catch (RuntimeException e) {
			System.out.println("Page: '" + pageName + "': " + e.getMessage());
		}
		return redirectPage;

	}

	public static String checkForComponentEvent(String pageName,
			String componentId, String eventId, String eventType,
			ApplicationStateManager applicationStateManager,
			ComponentSource componentSource, MetaDataLocator locator) {

		String redirectPage = null;
		String authenticationPolicyMeta = EVENT_HANDLER_AUTHENTICATION_TYPE
				+ "-" + eventId + "-" + eventType;
		authenticationPolicyMeta = authenticationPolicyMeta.toLowerCase();

		Component component = null;
		if (componentId == null) {
			component = componentSource.getPage(pageName);
		} else {
			component = componentSource.getComponent(pageName + ":"
					+ componentId);
		}
		try {
			String policyAsString = locator.findMeta(authenticationPolicyMeta,
					component.getComponentResources(), String.class);
			AuthenticationPolicyType policy = AuthenticationPolicyType
					.valueOf(policyAsString);
			redirectPage = AuthenticationValidator.check(policy,
					applicationStateManager);
		} catch (RuntimeException e) {
			System.out.println("Component: '" + pageName + ":" + componentId
					+ "': " + e.getMessage());
		}
		return redirectPage;

	}

	public static String check(AuthenticationPolicy policy,
			ApplicationStateManager applicationStateManager) {

		if (policy != null) {
			return check(policy.value(), applicationStateManager);
		} else {
			return null;
		}

	}

	public static String check(AuthenticationPolicyType policyType,
			ApplicationStateManager applicationStateManager) {
		String redirectPage = null;

		boolean userAuthenticated = applicationStateManager
				.exists(UserSession.class);

		switch (policyType) {

		case AUTHENTICATED: //cualquiera autenticado usuriosNormales+Admins -> necesario para updateProfile

			if (!userAuthenticated) {
				redirectPage = LOGIN_PAGE;
			}
			break;

		case NON_AUTHENTICATED: // necesario para login y registro solo esos pueden.

			if (userAuthenticated) {
				redirectPage = INIT_PAGE;
			}
			break;
		
		case ADMINS://administrador autenticado, necesario para añadirEvent, betType.....
			
			if(!userAuthenticated){
				redirectPage = LOGIN_PAGE;
			}
			if(userAuthenticated){
				UserSession userSession = applicationStateManager.get(UserSession.class);
				if(!userSession.isAdmin()){
					redirectPage = INIT_PAGE;
				}
			}
			break;
			
		case USERS://usuarios normales -> necesario para bet...
			
			if(!userAuthenticated){
				redirectPage = LOGIN_PAGE;
			}
			if(userAuthenticated){
				UserSession userSession = applicationStateManager.get(UserSession.class);
				if(userSession.isAdmin()){
					redirectPage = INIT_PAGE;
				}
			}
			break;
		
		default://ALL_USERS --> necesario para findEvents y luego ya dentro elige que metodo segun userSession.
			
			break;

		}

		return redirectPage;
	}

}
