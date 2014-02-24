package es.udc.isg011.apuestas.web.pages.bet;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.USERS)
public class ShowBalance {
	
    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private BetService betService;
    
    @Inject
    private Locale locale;

    @Property
    private float balance;
    
    public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}
    
    void onActivate() throws InstanceNotFoundException {

        balance = betService.obtainBalance(userSession.getUserProfileId());

    }
    
}
