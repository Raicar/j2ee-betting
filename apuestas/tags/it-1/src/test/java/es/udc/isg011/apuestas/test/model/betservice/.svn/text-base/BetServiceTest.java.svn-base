package es.udc.isg011.apuestas.test.model.betservice;

import static es.udc.isg011.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.isg011.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.bet.BetDao;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betoption.BetOptionDao;
import es.udc.isg011.apuestas.model.betservice.BetAfterEventStartedException;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.betservice.InsufficientBalanceException;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.bettype.BetTypeDao;
import es.udc.isg011.apuestas.model.category.Category;
import es.udc.isg011.apuestas.model.category.CategoryDao;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.model.event.EventDao;
import es.udc.isg011.apuestas.model.userprofile.UserProfile;
import es.udc.isg011.apuestas.model.userservice.UserProfileDetails;
import es.udc.isg011.apuestas.model.userservice.UserService;
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccount;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceTest {

	private final long NON_EXISTENT_CATEGORY_ID = -1;
	private final long NON_EXISTENT_BET_OPTION_ID = -1;
	private final long NON_EXISTENT_EVENT_ID = -1;
	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	
	private final String EVENTS_KEYWORDS_LOOK_FOR = "words to look for it";
	private final String EVENTS_KEYWORDS_NOT_LOOK_FOR = "not this ones";

	@Autowired
	private BetService betService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private BetDao betDao;
	@Autowired
	private BetOptionDao betOptionDao;
	@Autowired
	private BetTypeDao betTypeDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private CategoryDao categoryDao;
	
	
	private Category createCategory(String name){
		Category category = new Category(name);
		categoryDao.save(category);
		return category;
	}
	
	private Event createEvent(boolean late) {
		Category category = createCategory("name");
		
		Calendar date = Calendar.getInstance();
		if (late)
			date.set(2014, 9, 12);
		else
			date.set(2010, 9, 12);
		Event event = new Event("name", date);
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	private BetOption createBetOption(boolean late){
		Event event;
		
		if (late)
			event = createEvent(late);
		else
			event = createEvent(late);
		
		BetType betType = new BetType("question", false);
		betType.setEvent(event);
		betTypeDao.save(betType);
		BetOption betOption = new BetOption("option", new Float(0.5));
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		return betOption;
	}
	
	private UserProfile createUserProfile(){
        try {
            return userService.registerUser("loginName", "clearPassword", new UserProfileDetails("firstName", "lastName", "email"));
        } catch (DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }
	}
	
	// TESTS BET
	@Test(expected = InstanceNotFoundException.class)
	public void testBetNonExistentBetOption() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException{
		betService.bet(NON_EXISTENT_BET_OPTION_ID, createUserProfile().getUserProfileId(),new Float(0));
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBetNonExistentUserProfile() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException{
		betService.bet(createBetOption(true).getBetOptionId(), NON_EXISTENT_USER_PROFILE_ID,new Float(0));
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void testBetInsufficientBalance() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException{
		//initial balance of virtualAccount of new userProfile is always 0
		betService.bet(createBetOption(true).getBetOptionId(), createUserProfile().getUserProfileId(),new Float(10));
	}
	
	@Test(expected = BetAfterEventStartedException.class)
	public void testBetAfterEventStarted() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException{
		betService.bet(createBetOption(false).getBetOptionId(), createUserProfile().getUserProfileId(), new Float(0));
	}

	@Test
	public void testBetSufficientBalance() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException{
		UserProfile userProfile = createUserProfile();
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		float balance = 15;
		virtualAccount.setBalance(balance);
		float money = 10;
		Bet bet = betService.bet(createBetOption(true).getBetOptionId(), userProfile.getUserProfileId(),money);

		Bet bet2 = betDao.find(bet.getBetId());
		VirtualAccount virtualAccount2 = bet2.getVirtualAccount();
		
		assertEquals(bet, bet2);
		assertTrue(virtualAccount.getBalance() == (balance-money));
		assertTrue(virtualAccount.getBalance() == virtualAccount2.getBalance());
	}
	
	//TESTS GETNUMBEROFBETS
	
	@Test(expected=InstanceNotFoundException.class)
	public void testGetNumberOfBetsNonExistentUser() throws InstanceNotFoundException{
		betService.getNumberOfBets(NON_EXISTENT_USER_PROFILE_ID);
	}
	
	@Test
	public void testGetNumberOfBets() throws InstanceNotFoundException, 
				InsufficientBalanceException, BetAfterEventStartedException{
		UserProfile userProfile = createUserProfile();
		long userProfileId=userProfile.getUserProfileId();
		
		assertEquals(betService.getNumberOfBets(userProfileId),0);
		
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		float balance = 15;
		virtualAccount.setBalance(balance);
		float money = 10;
		
		betService.bet(createBetOption(true).getBetOptionId(), userProfile.getUserProfileId(),money);
		assertEquals(betService.getNumberOfBets(userProfileId),1);	
	}
	
	
	//TESTS OBTAINBETS
	
	@Test(expected=InstanceNotFoundException.class)
	public void testObtainBetsNonExistentUser() throws InstanceNotFoundException{
		betService.obtainBets(NON_EXISTENT_USER_PROFILE_ID,0,0);
	}
	
	@Test
	public void testObtainBets() throws InstanceNotFoundException, 
			InsufficientBalanceException, BetAfterEventStartedException {
		UserProfile userProfile = createUserProfile();
		long userProfileId=userProfile.getUserProfileId();
		List<Bet> bets=new ArrayList<Bet>();
		
		assertEquals(betService.obtainBets(userProfileId,0,10),bets);
		
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		float balance = 15;
		virtualAccount.setBalance(balance);
		float money = 10;
		
		Bet bet=betService.bet(createBetOption(true).getBetOptionId(), userProfile.getUserProfileId(),money);
		
		bets.add(bet);
		assertEquals(betService.obtainBets(userProfileId,0,10),bets);	
	}
	
	//TESTS GETNUMBEROFEVENTS
	
	private Event createEventMatchKeyWordsDateOk(Category category){
		//matched keyWord event without betType
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR,2014);
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, date );
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	private Event createEventMatchKeyWordsDateNonOk(Category category){
		//matched keyWord event without betType
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR,2011);
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, date );
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	private Event createEventNonMatchKeyWordsDateOk(Category category){
		//non matched keyWord event
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR,2015);
		Event event = new Event(EVENTS_KEYWORDS_NOT_LOOK_FOR, date);
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	private Event createEventNonMatchKeyWordsDateNonOk(Category category){
		//non matched keyWord event
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR,2010);
		Event event = new Event(EVENTS_KEYWORDS_NOT_LOOK_FOR, date);
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}

	/*
	 * createFindEvents creates:
	 * 		MatchKW  MatchCat MatchUserCond(Date) 
	 * ev1	  	Y		Y			Y
	 * ev2		Y		Y			N
	 * ev3		N		Y			Y	
	 * ev4		N		Y			N
	 * 
	 * If save==true, it adds each event to the correspondent list, one for the ones that
	 * match the category(all that fulfill user condition) and another one for the ones
	 * that match the KeyWords constant.
	 * Otherwise nothing is saved.
	 * 
	 */
	private Category createFindEvents(String categoryName, List<Event> matchedKW, List<Event> matchedCat, Boolean save) {
		if(save==true) {matchedKW.clear(); matchedCat.clear();}
		Category category = createCategory(categoryName);
		
		Event event = createEventMatchKeyWordsDateOk(category);
		if(save==true) {matchedKW.add(event); matchedCat.add(event);}
		
		event = createEventMatchKeyWordsDateNonOk(category);
		
		event = createEventNonMatchKeyWordsDateOk(category);
		if(save==true) {matchedCat.add(event);}
		
		event = createEventNonMatchKeyWordsDateNonOk(category);

		return category;
	}

	@Test(expected=InstanceNotFoundException.class)
	public void testGetNumberOfEventsNonExistenCategory() throws InstanceNotFoundException{
		betService.getNumberOfEvents(null, NON_EXISTENT_CATEGORY_ID);
	}
	
	@Test
	public void testGetNumberOfEvents() throws InstanceNotFoundException{

		//creates 4 events for each category, as describe in createFindEvents
		Category category1 = createFindEvents("cat1",null, null, false);
		createFindEvents("cat2",null, null, false);
		
		int numberEvents;
		
		numberEvents = betService.getNumberOfEvents(null, category1.getCategoryId());
		assertTrue(numberEvents == 2);
		
		numberEvents = betService.getNumberOfEvents(EVENTS_KEYWORDS_LOOK_FOR, null);
		assertTrue(numberEvents == 2);
		
		numberEvents = betService.getNumberOfEvents(EVENTS_KEYWORDS_LOOK_FOR,  category1.getCategoryId());
		assertTrue(numberEvents == 1);
		
		numberEvents = betService.getNumberOfEvents(null, null);
		assertTrue(numberEvents == 4);
	}
	
	//TESTS FINDEVENTS
	@Test(expected=InstanceNotFoundException.class)
	public void testFindEventsNonExistenCategory() throws InstanceNotFoundException{
		betService.findEvents(null, NON_EXISTENT_CATEGORY_ID, 0, 10);
	}
	
	@Test
	public void testFindEventsByCategory() throws InstanceNotFoundException{
		
		List<Event> matchedKW1 = new ArrayList<>();
		List<Event> matchedCat1 = new ArrayList<>();
		Category category1 = createFindEvents("cat1", matchedKW1, matchedCat1, true);

		createFindEvents("cat2", null, null, false);
		
		List<Event> results;
		
		//findByCategory
		results = betService.findEvents(null, category1.getCategoryId(), 0, 1);
		assertTrue(results.size()== 1);
		results.addAll(betService.findEvents(null, category1.getCategoryId(), 1, 5));
		assertTrue(results.size()== 2);
		
		assertTrue(results.size()== matchedCat1.size());
		
		for(int i=0; i<results.size(); i++){
			assertTrue(matchedCat1.contains(results.get(i)));
		}
	}

	@Test
	public void testFindEventsByKeyWords() throws InstanceNotFoundException{
		
		List<Event> matchedKW1 = new ArrayList<>();
		List<Event> matchedCat1 = new ArrayList<>();
		createFindEvents("cat1", matchedKW1, matchedCat1, true);
		
		List<Event> matchedKW2 = new ArrayList<>();
		List<Event> matchedCat2 = new ArrayList<>();
		createFindEvents("cat2", matchedKW2, matchedCat2, true);
		
		
		List<Event> results;
		//findByKeyWords
		results = betService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, null, 0, 1);
		assertTrue(results.size()== 1);
		results.addAll(betService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, null, 1, 5));
		assertTrue(results.size()== 2);
		
		assertTrue(results.size()== (matchedKW1.size()+matchedKW2.size()));
		
		for(int i=0; i<results.size(); i++){
			assertTrue(matchedKW1.contains(results.get(i))||matchedKW2.contains(results.get(i)));
		}
	}

	@Test
	public void testFindEventsByKeyWordsCategory() throws InstanceNotFoundException{
		
		List<Event> matchedKW1 = new ArrayList<>();
		List<Event> matchedCat1 = new ArrayList<>();
		Category category = createFindEvents("cat1", matchedKW1, matchedCat1, true);
		
		List<Event> matchedKW2 = new ArrayList<>();
		List<Event> matchedCat2 = new ArrayList<>();
		createFindEvents("cat2", matchedKW2, matchedCat2, true);
		
		
		List<Event> results;
		//findByKeyWords
		results = betService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, category.getCategoryId(), 0, 5);
		assertTrue(results.size()== 1);
	
		assertTrue(results.size()== matchedKW1.size());
		
		for(int i=0; i<results.size(); i++){
			assertTrue(matchedKW1.contains(results.get(i)));
		}
	}

	@Test
	public void testFindEventsAll() throws InstanceNotFoundException{
		
		List<Event> matchedKW1 = new ArrayList<>();
		List<Event> matchedCat1 = new ArrayList<>();
		createFindEvents("cat1", matchedKW1, matchedCat1, true);
		
		List<Event> matchedKW2 = new ArrayList<>();
		List<Event> matchedCat2 = new ArrayList<>();
		createFindEvents("cat2", matchedKW2, matchedCat2, true);
		
		
		List<Event> results;
		//findByKeyWords
		results = betService.findEvents(null, null, 0, 2);
		assertTrue(results.size()== 2);
		results.addAll(betService.findEvents(null, null, 2, 8));
		assertTrue(results.size()== 4);
		
		assertTrue(results.size()== (matchedCat1.size()+matchedCat2.size()));
		
		for(int i=0; i<results.size(); i++){
			assertTrue(matchedCat1.contains(results.get(i))||matchedCat2.contains(results.get(i)));
		}
	}
	
	@Test
	public void testFindEventsOrderByDate() throws InstanceNotFoundException{
		createFindEvents("cat1",null, null, false);
		createFindEvents("cat2",null, null, false);

		List<Event> results = betService.findEvents(null, null, 0, 10);

		Calendar date = results.get(0).getDate();
		for (Event e : results) {
			assertTrue(date.before(e.getDate())||date.equals(e.getDate()));
			date = e.getDate();
		}
	}
	
	//Events names ("words to look for it"|"not")
	@Test
	public void testFindEventsKeyWordsCombinations() throws InstanceNotFoundException{
		createFindEvents("cat1",null, null, false);
		createFindEvents("cat2",null, null, false);

		List<Event> results = betService.findEvents("to it look words", null, 0, 10);
		int number = betService.getNumberOfEvents("to it look words", null);
		assertTrue((results.size() == 2)&&(number == 2));

		results = betService.findEvents("ook rds", null, 0, 10);
		number = betService.getNumberOfEvents("ook rds", null);
		assertTrue((results.size() == 2)&&(number == 2));
		
		results = betService.findEvents("o", null, 0, 10);
		number = betService.getNumberOfEvents("o", null);
		assertTrue((results.size() == 4)&&(number == 4));

	}
	
	//TESTS OBTAINEVENT

	@Test(expected = InstanceNotFoundException.class)
	public void testObtainEventNonExistent() throws InstanceNotFoundException{
		betService.obtainEvent(NON_EXISTENT_EVENT_ID);
	}

	@Test
	public void testObtainEventExistent() throws InstanceNotFoundException{
		Event event = createEvent(true);
		Event event2 = betService.obtainEvent(event.getEventId());
		assertEquals(event,event2);
	}
	
	//TESTS OBTAINBETOPTION
	
	@Test(expected = InstanceNotFoundException.class)
	public void testObtainBetOptionNonExistent() throws InstanceNotFoundException{
		betService.obtainBetOption(NON_EXISTENT_BET_OPTION_ID);
	}
	
	@Test
	public void testObtainBetOptionExistent() throws InstanceNotFoundException{
		BetOption betOption = createBetOption(true);
		BetOption betOption2 = betService.obtainBetOption(betOption.getBetOptionId());
		assertEquals(betOption,betOption2);
	}
	
	//TESTS OBTAINBALANCE
	
	@Test(expected = InstanceNotFoundException.class)
	public void testObtainBalanceNonExistentUserProfile() throws InstanceNotFoundException{
		betService.obtainBalance(NON_EXISTENT_USER_PROFILE_ID);
	}

	@Test 
	public void testObtainBalance() throws InstanceNotFoundException{
		UserProfile userProfile = createUserProfile();
		VirtualAccount virtualAccount = userProfile.getVirtualAccount();
		float balance = virtualAccount.getBalance();
		float balance2 = betService.obtainBalance(userProfile.getUserProfileId());
		assertTrue(balance==balance2);
		
		virtualAccount.setBalance(balance+10);
		balance = virtualAccount.getBalance();
		balance2 = betService.obtainBalance(userProfile.getUserProfileId());
		assertTrue(balance==balance2);
		
	}

	//TESTS OBTAINCATEGORIES

	@Test
	public void testObtainCategories() {
		Category c1 = new Category("name1");
		Category c2 = new Category("name2");
		Category c3 = new Category("name3");
		categoryDao.save(c3);
		categoryDao.save(c2);
		categoryDao.save(c1);
		
		List<Category> l2 = betService.obtainCategories();
		
		assertEquals(c1, l2.get(0));
		assertEquals(c2, l2.get(1));
		assertEquals(c3, l2.get(2));
	
	}

	



}
