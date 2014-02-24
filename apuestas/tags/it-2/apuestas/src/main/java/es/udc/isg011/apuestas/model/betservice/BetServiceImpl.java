package es.udc.isg011.apuestas.model.betservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.bet.BetDao;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betoption.BetOptionDao;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.bettype.BetTypeDao;
import es.udc.isg011.apuestas.model.category.Category;
import es.udc.isg011.apuestas.model.category.CategoryDao;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.model.event.EventDao;
import es.udc.isg011.apuestas.model.userprofile.UserProfile;
import es.udc.isg011.apuestas.model.userprofile.UserProfileDao;
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccount;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@Service("BetService")
@Transactional
public class BetServiceImpl implements BetService {

	@Autowired
	private BetDao betDao;
	@Autowired
	private BetOptionDao betOptionDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private UserProfileDao userProfileDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private BetTypeDao betTypeDao;
	
	@Override
	public Bet bet(Long betOptionId, Long userProfileId, float money) throws InsufficientBalanceException, InstanceNotFoundException, BetAfterEventStartedException, IncorrectBalanceException {
		UserProfile userProfile = userProfileDao.find(userProfileId);
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		
		BetOption betOption = betOptionDao.find(betOptionId);
		Event event = betOption.getBetType().getEvent();
		
		Calendar eventDate = event.getDate();
		Calendar currentDate = Calendar.getInstance();
		
		if (eventDate.before(currentDate)){
			throw new BetAfterEventStartedException(event.getEventId(), eventDate, currentDate);
		}
		
		float currentBalance = virtualAccount.getBalance();
		
		if(money <= 0)
			throw new IncorrectBalanceException(virtualAccount.getVirtualAccountId(), currentBalance, money);
		if(currentBalance < money){
			throw new InsufficientBalanceException(virtualAccount.getVirtualAccountId(), currentBalance, money);
		}
		
		
		Bet bet = new Bet(money, currentDate, virtualAccount, betOption);
		virtualAccount.setBalance(currentBalance-money);
		betDao.save(bet);

		return bet;
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumberOfBets(Long userProfileId) throws InstanceNotFoundException {
		if (!userProfileDao.exists(userProfileId)){
			throw new InstanceNotFoundException(userProfileId, UserProfile.class.getName());
		}
		return betDao.getNumberOfBets(userProfileId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Bet> obtainBets(Long userProfileId, int startIndex, int count) throws InstanceNotFoundException {
		if (!userProfileDao.exists(userProfileId)){
			throw new InstanceNotFoundException(userProfileId, UserProfile.class.getName());
		}
		return betDao.obtainBets(userProfileId, startIndex, count);
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumberOfEvents(String keyWords, Long categoryId) throws InstanceNotFoundException {
		// category --> exists | !exist | null
		if(categoryId != null){ 
			if (!categoryDao.exists(categoryId))
				throw new InstanceNotFoundException(categoryId, Category.class.getName());
		}
		
		return eventDao.getNumberOfEventsByKeyWordsCategoryUser(keyWords, categoryId);
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<Event> findEvents(String keyWords, Long categoryId,
			int startIndex, int count) throws InstanceNotFoundException {
		
		// category --> exists | !exist | null
		if(categoryId != null){ //boolCategory's already false
			if (!categoryDao.exists(categoryId))
				throw new InstanceNotFoundException(categoryId, Category.class.getName());
		}
		
		return eventDao.findByKeyWordsCategoryUser(keyWords, categoryId, startIndex, count);
	}

	@Override
	@Transactional(readOnly = true)
	public Event obtainEvent(Long eventId) throws InstanceNotFoundException {
		Event event = eventDao.find(eventId);
		return event;
	}

	@Override
	@Transactional(readOnly = true)
	public BetOption obtainBetOption(Long betOptionId) throws InstanceNotFoundException {
		BetOption betOption= betOptionDao.find(betOptionId);
		return betOption;
	}

	@Override
	@Transactional(readOnly = true)
	public float obtainBalance(Long userProfileId) throws InstanceNotFoundException {
		UserProfile userProfile = userProfileDao.find(userProfileId);
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		return virtualAccount.getBalance();
	}

	@Override
	@Transactional(readOnly = true)
	public BetType obtainBetType(Long betTypeId) throws InstanceNotFoundException {
		BetType betType= betTypeDao.find(betTypeId);
		return betType;
	}

	@Override
	@Transactional(readOnly = true)
	public Bet obtainBet(Long betId, Long userProfileId) throws InstanceNotFoundException, BetNotBelongUserException {
		Bet bet = betDao.find(betId);
		if (bet.getVirtualAccount().getUserProfile().getUserProfileId() == userProfileId)
			return bet;
		else throw new BetNotBelongUserException(bet.getBetId(), userProfileId);
	}


}
