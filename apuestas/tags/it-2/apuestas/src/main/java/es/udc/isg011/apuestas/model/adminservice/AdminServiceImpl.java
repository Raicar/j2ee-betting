package es.udc.isg011.apuestas.model.adminservice;

import java.util.ArrayList;
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
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccount;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
    private EventDao eventDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired 
	private BetTypeDao betTypeDao;
	
	@Autowired
	private BetOptionDao betOptionDao;
	
	@Autowired
	private BetDao betDao;
	
	@Override
	public Event addEvent(Event event, Long categoryId) throws InstanceNotFoundException, InvalidDateOfEventException{
		if (event.getDate().before(Calendar.getInstance()))
			throw new InvalidDateOfEventException(event.getEventId(), event.getName(), event.getDate());
		
		Category category=categoryDao.find(categoryId);
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}

	@Override
	public BetType addBetType(Long eventId, BetType betType) throws InstanceNotFoundException, 
	WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException, DuplicateBetTypeQuestionException, DuplicateBetOptionException, EventStartedException {
		Event event=eventDao.find(eventId);
		if(event.getDate().before(Calendar.getInstance()))
			throw new EventStartedException(event.getName());

		if (betTypeDao.existEventIdQuestion(eventId,betType.getQuestion())) 
			throw new DuplicateBetTypeQuestionException(betType.getQuestion());
		
		if(betType.getBetOptions().isEmpty())
			throw new BetTypeWithEmptyBetOptionsListException(betType.getQuestion());
		
		List<String> answers = new ArrayList<String>();
		for (BetOption bOp : betType.getBetOptions()) {
			if(bOp.getWinner()!= null) throw new WinnerAlreadySetException(bOp.getWinner());
			if(answers.contains(bOp.getOption())) 
				throw new DuplicateBetOptionException(bOp.getOption());
			answers.add(bOp.getOption());
		}
		
		event.addBetType(betType);
		betTypeDao.save(betType);
		
		for (BetOption bOp : betType.getBetOptions()) {
			betOptionDao.save(bOp);
		}
		
		return betType;	
		
	}

	// with page-by-page iterator
	private void distributeProfits(BetOption betOption){
				
		int startIndex = 0;
		int count = 10;
		List<Bet> winnerBetList = null;
		float rateProfit,balance;
		
		VirtualAccount virtualAccount;
		
		winnerBetList = betDao.findBetsByBetOptionId(betOption.getBetOptionId(), startIndex, count);

		while(!winnerBetList.isEmpty()){
			startIndex+=count;
			rateProfit=betOption.getRateProfit();
			
			for(Bet b : winnerBetList){
			    virtualAccount=b.getVirtualAccount();
				balance=virtualAccount.getBalance();
				balance+=b.getMoney()*rateProfit;
				virtualAccount.setBalance(balance);
			}
			winnerBetList = betDao.findBetsByBetOptionId(betOption.getBetOptionId(), startIndex, count);
		}
	}	
	
	@Override
	public void establishWinner(Long betTypeId, List<Long> winnerOptionIdList) throws InstanceNotFoundException, 
			MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException, WinnerAlreadySetException {
		BetType betType = betTypeDao.find(betTypeId);

		if(winnerOptionIdList.size()>1 && !betType.getMultipleWinner())
			throw new NonMultipleChoiceBetTypeException(betTypeId);

		if(betType.getEstablishedWinner())
			throw new WinnerAlreadySetException(betType.getEstablishedWinner());

		
		for(Long l : winnerOptionIdList){
			BetOption betOption = betOptionDao.find(l);
			if(betOption.getBetType().getBetTypeId()!=betTypeId)
				throw new MismatchedBetTypeBetOptionException(betTypeId,betOption.getBetOptionId());
		}
		
		betType.setEstablishedWinner(true);
		for(BetOption bOp : betType.getBetOptions()){
			if(winnerOptionIdList.contains(bOp.getBetOptionId())){
				bOp.setWinner(true);
				distributeProfits(bOp);
			}else bOp.setWinner(false);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Event> findEvents(String keyWords, Long categoryId, int startIndex, int count) throws InstanceNotFoundException {
		
		// category --> exists | !exist | null
		if(categoryId != null){ //boolCategory's already false
			if (!categoryDao.exists(categoryId))
				throw new InstanceNotFoundException(categoryId, Category.class.getName());
		}
		
		return eventDao.findByKeyWordsCategoryAdmin(keyWords, categoryId, startIndex, count);
		
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumberOfEvents(String keyWords, Long categoryId) throws InstanceNotFoundException {

		// category --> exists | !exist | null
		if(categoryId != null){ 
			if (!categoryDao.exists(categoryId))
				throw new InstanceNotFoundException(categoryId, Category.class.getName());
		}

		return eventDao.getNumberOfEventsByKeyWordsCategoryAdmin(keyWords, categoryId);


	}

}
