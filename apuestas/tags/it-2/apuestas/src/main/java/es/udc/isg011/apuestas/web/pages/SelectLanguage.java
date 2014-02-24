package es.udc.isg011.apuestas.web.pages;

import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.SupportedLanguages;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class SelectLanguage {
	
	@Property
	private String language;
	
	@Inject
	private Locale locale;
	
	@Inject
	private PersistentLocale persistentLocale;
	
	void onPrepareForRender(){
		language=locale.getLanguage();
	}
	
	public String getLanguages() {
        return SupportedLanguages.getOptions(locale.getLanguage());
    }
	
	Object onSuccess() {
		persistentLocale.set(new Locale(language));
		return Index.class;
	}
}
