package es.udc.isg011.apuestas.test.model.adminservice;

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

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.adminservice.BetTypeWithEmptyBetOptionsListException;
import es.udc.isg011.apuestas.model.adminservice.MismatchedBetTypeBetOptionException;
import es.udc.isg011.apuestas.model.adminservice.NonMultipleChoiceBetTypeException;
import es.udc.isg011.apuestas.model.adminservice.WinnerAlreadySetException;
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
import es.udc.isg011.apuestas.model.userservice.UserProfileDetails;
import es.udc.isg011.apuestas.model.userservice.UserService;
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccount;
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccountDao;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional

public class AdminServiceTest {
	
	private final long NON_EXISTENT_EVENT_ID = -1;
	private final long NON_EXISTENT_CATEGORY_ID = -1;
	private final long NON_EXISTENT_BETTYPE_ID = -1;
	private final long NON_EXISTENT_BETOPTION_ID = -1;
	
	private final String EVENTS_KEYWORDS_LOOK_FOR = "words to look for it";
	private final String EVENTS_KEYWORDS_NOT_LOOK_FOR = "not this ones";

	
	@Autowired
    private AdminService adminService;
	@Autowired
    private UserService userService;
	
	@Autowired
	private EventDao eventDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private BetTypeDao betTypeDao;
	@Autowired
	private BetOptionDao betOptionDao;
	@Autowired
	private UserProfileDao userProfileDao;
	@Autowired
	private VirtualAccountDao virtualAccountDao;
	@Autowired
	private BetDao betDao;
	

	private Category createCategory(String name){
		Category category = new Category(name);
		categoryDao.save(category);
		return category;
	}
	
	
	private Event createEvent() {
		Category category = createCategory("name");
		Event event = new Event("event", Calendar.getInstance());
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	
	//TESTS ADDEVENT
	
	@Test(expected = InstanceNotFoundException.class)
	public void testAddEventNonExistentCategory() throws InstanceNotFoundException{
		Event event = new Event("event", Calendar.getInstance());
		adminService.addEvent(event, NON_EXISTENT_CATEGORY_ID);
	}
	
	@Test
	public void testAddEventExistentCategory() throws InstanceNotFoundException{

		Event event = new Event("event", Calendar.getInstance());
		Category category = createCategory("name");
		
		adminService.addEvent(event, category.getCategoryId());
		
		Event event2 = eventDao.find(event.getEventId());
		Category category2 = event2.getCategory();

		assertEquals(event,event2);
		assertEquals(category, category2);
	}
	
	
	//TESTS ADDBETTYPE
	
	@Test(expected = InstanceNotFoundException.class)
	public void testAddBetTypeNonExistentEvent() throws InstanceNotFoundException,
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException{
		BetType betType=new BetType("question",false);
		adminService.addBetType(NON_EXISTENT_EVENT_ID, betType);
	}
	
	@Test(expected=BetTypeWithEmptyBetOptionsListException.class)
	public void testAddBetTypeWithoutBetOptions() throws InstanceNotFoundException, 
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException{
		Event event=createEvent();
		BetType betType=new BetType("question",false);
		adminService.addBetType(event.getEventId(), betType);
		
		BetType betType2 = betTypeDao.find(betType.getBetTypeId());
		
		assertEquals(betType, betType2);
	}
	
	@Test
	public void testAddBetTypeWithBetOptions() throws InstanceNotFoundException, 
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException{
		Event event = createEvent();
		
		BetType betType = new BetType("question",false);
		betType.setEvent(event);
		
		BetOption betOption1 = new BetOption("option1", (float)0.5);
		BetOption betOption2 = new BetOption("option2", (float)0.5);
		
		betType.addBetOption(betOption1);
		betType.addBetOption(betOption2);	
		
		adminService.addBetType(event.getEventId(), betType);

		BetType betType2 = betTypeDao.find(betType.getBetTypeId());
		
		assertEquals(betType, betType2);
	}
	
	@Test(expected=DuplicateInstanceException.class)
	public void testAddBetTypeDuplicateQuestion() throws InstanceNotFoundException, 
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException{
		Event event = createEvent();
		BetType betType1 = new BetType("question",false);
		betType1.setEvent(event);
		
		
		betType1.addBetOption(new BetOption("option", (float)0.5));
		
		adminService.addBetType(event.getEventId(), betType1);
		BetType betType2 = new BetType("question",false);
		betType2.setEvent(event);
		
		betType2.addBetOption(new BetOption("option", (float)0.5));
		
		adminService.addBetType(event.getEventId(), betType2);
	}
	
	@Test(expected=DuplicateInstanceException.class)
	public void testAddBetTypeWithBetOptionsDuplicateAnswer() throws InstanceNotFoundException, 
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException{
		Event event = createEvent();
		
		BetType betType = new BetType("question",false);
		betType.setEvent(event);
		
		BetOption betOption1 = new BetOption("option", (float)0.5);
		BetOption betOption2 = new BetOption("option", (float)0.5);
		
		betType.addBetOption(betOption1);
		betType.addBetOption(betOption2);	
		
		adminService.addBetType(event.getEventId(), betType);
	}
	
	@Test(expected=WinnerAlreadySetException.class)
	public void testAddBetTypeWithBetOptionsWinnerAlreadySetException() throws InstanceNotFoundException, 
				DuplicateInstanceException, WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException {
		Event event = createEvent();
		
		BetType betType = new BetType("question",false);
		betType.setEvent(event);
		
		BetOption betOption1 = new BetOption("option", (float)0.5);
		betOption1.setWinner(true);
		BetOption betOption2 = new BetOption("option", (float)0.5);
		
		betType.addBetOption(betOption1);
		betType.addBetOption(betOption2);	
		
		adminService.addBetType(event.getEventId(), betType);
	}
	
	
	//TESTS ESTABLISHWINNER
	
	
	@Test
	public void testEstablishWinner() throws InstanceNotFoundException, MismatchedBetTypeBetOptionException, 
				DuplicateInstanceException, NonMultipleChoiceBetTypeException{
		Event event = createEvent();
		
		BetType betType=new BetType("question", true);
		betType.setEvent(event);
		betTypeDao.save(betType);
		
		float rateProfit=new Float(0.5);
		
		BetOption betOption=new BetOption("option", rateProfit);
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		
		List<Long> list=new ArrayList<Long>();
		list.add(betOption.getBetOptionId());
		
		UserProfile userProfile = userService.registerUser("user", "userPassword", new UserProfileDetails("name", "lastName", "user@udc.es"));
		VirtualAccount virtualAccount=userProfile.getVirtualAccount();
		float balance=10;
		virtualAccount.setBalance(balance);
		
		Bet bet = new Bet(balance, Calendar.getInstance(), virtualAccount, betOption);
		betDao.save(bet);
		
		adminService.establishWinner(betType.getBetTypeId(), list);
		assertTrue(betOption.getWinner());
		float newBalance= balance*(1+rateProfit);
		assertEquals(newBalance, virtualAccount.getBalance(),0.0);
	} 
	
	@Test(expected=InstanceNotFoundException.class)
	public void testEstablishWinnerNoExistentBetType() throws InstanceNotFoundException, 
				MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException{
		List<Long> list=new ArrayList<Long>();
		adminService.establishWinner(NON_EXISTENT_BETTYPE_ID, list);
	} 
	
	@Test(expected=InstanceNotFoundException.class)
	public void testEstablishWinnerNoExistentBetOption() throws InstanceNotFoundException, 
				MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException{
		Event event = createEvent();
		
		BetType betType=new BetType("question", true);
		betType.setEvent(event);
		betTypeDao.save(betType);
		
		BetOption betOption=new BetOption("option", new Float(0.5));
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		
		List<Long> list=new ArrayList<Long>();
		list.add(NON_EXISTENT_BETOPTION_ID);
		adminService.establishWinner(betType.getBetTypeId(), list);
	}
	
	@Test(expected=MismatchedBetTypeBetOptionException.class)
	public void testEstablishWinnerMismatchedBetTypeBetOption() throws InstanceNotFoundException, 
				MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException{
		Event event = createEvent();
		
		BetType betType=new BetType("question", true);
		betType.setEvent(event);
		betTypeDao.save(betType);	
		
		BetOption betOption=new BetOption("option", new Float(0.5));
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		
		BetType betType1=new BetType("question", true);
		betType1.setEvent(event);
		betTypeDao.save(betType1);
		
		BetOption betOption1=new BetOption("option", new Float(0.5));
		betType1.addBetOption(betOption1);
		betOptionDao.save(betOption1);
		
		List<Long> list=new ArrayList<Long>();
		list.add(betOption1.getBetOptionId());
		adminService.establishWinner(betType.getBetTypeId(), list);
	}
	
	@Test(expected = NonMultipleChoiceBetTypeException.class)
	public void testEstablishWinnerNonMultipleChoiceBetType() throws InstanceNotFoundException, 
				MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException{
		Event event = createEvent();
		
		BetType betType=new BetType("question", false);
		betType.setEvent(event);
		betTypeDao.save(betType);
		
		BetOption betOption1 = new BetOption("option1", new Float(0.5));
		betType.addBetOption(betOption1);
		betOptionDao.save(betOption1);
		BetOption betOption2 = new BetOption("option2", new Float(0.5));
		betType.addBetOption(betOption2);
		betOptionDao.save(betOption2);
		
		List<Long> list = new ArrayList<Long>();
		list.add(betOption1.getBetOptionId());
		list.add(betOption2.getBetOptionId());
		adminService.establishWinner(betType.getBetTypeId(), list);
		
	}

	
	//TESTS GETNUMBEROFEVENTS

	private Event createEventMatchKeyWordsWithoutBetType(Category category){
		//matched keyWord event without betType
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, Calendar.getInstance());
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}

	private Event createEventNonMatchKeyWords(Category category){
		//non matched keyWord event
		Event event = new Event(EVENTS_KEYWORDS_NOT_LOOK_FOR, Calendar.getInstance());
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}
	
	private Event createEventMatchKeyWordsWithBetTypeWinnerNull(Category category){
		//matched keyWord event with betType, 1 betOption.winner Null
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, Calendar.getInstance());
		event.setCategory(category);
		
		BetType betType = new BetType("question", false);
		event.addBetType(betType);
		
		eventDao.save(event);
		betTypeDao.save(betType);
		
		BetOption betOption = new BetOption("option", (float) 2.5);
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);

		return event;
	}
	
	private Event createEventMatchKeyWordsWithBetTypeWinnerNullTrue(Category category){
		//matched keyWord event with betType, 1 betOption.winner Null,1 betOption.winner true
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, Calendar.getInstance());
		event.setCategory(category);
		
		BetType betType = new BetType("question", false);
		event.addBetType(betType);
		eventDao.save(event);
		betTypeDao.save(betType);

		BetOption betOption = new BetOption("option", (float) 2.5);
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		
		betOption = new BetOption("option2", (float) 2.5);
		betOption.setWinner(true);
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);
		
		return event;
	}
	
	private Event createEventMatchKeyWordsWithBetTypeWinnerTrue(Category category){
		//matched keyWord event with betType, 1 betOption.winner true --> nonMatch
		Event event = new Event(EVENTS_KEYWORDS_LOOK_FOR, Calendar.getInstance());
		event.setCategory(category);
		
		BetType betType = new BetType("question", false);
		event.addBetType(betType);
		eventDao.save(event);
		betTypeDao.save(betType);
		
		BetOption betOption = new BetOption("option", (float) 2.5);
		betOption.setWinner(true);
		betType.addBetOption(betOption);
		betOptionDao.save(betOption);

		return event;
	}

	/*
	 * Creates:
	 * 		MatchKW  MatchCat MatchAdminCond 
	 * ev1	  	Y		Y			Y
	 * ev2		N		Y			Y
	 * ev3		Y		Y			Y	
	 * ev4		Y		Y			Y
	 * ev5		Y		Y			N
	 * 
	 * If save==true, it adds each event to the correspondent list, one for the ones that
	 * match the category(all that fulfill admin condition) and another one for the ones
	 * that match the KeyWords constant.
	 * Otherwise nothing is saved.
	 */
	private Category createFindEvents(String categoryName, List<Event> matchedKW, List<Event> matchedCat, Boolean save) {
		if(save==true) {matchedKW.clear(); matchedCat.clear();}
		Category category = createCategory(categoryName);
		
		Event event = createEventMatchKeyWordsWithoutBetType(category);
		if(save==true) {matchedKW.add(event); matchedCat.add(event);}
		
		event = createEventNonMatchKeyWords(category);
		if(save==true) {matchedCat.add(event);}
		
		event = createEventMatchKeyWordsWithBetTypeWinnerNull(category);
		if(save==true) {matchedKW.add(event); matchedCat.add(event);}
		
		event = createEventMatchKeyWordsWithBetTypeWinnerNullTrue(category);
		if(save==true) {matchedKW.add(event); matchedCat.add(event);}
		
		event = createEventMatchKeyWordsWithBetTypeWinnerTrue(category);

		return category;
	}
	
	
	@Test(expected=InstanceNotFoundException.class)
	public void testGetNumberOfEventsNonExistenCategory() throws InstanceNotFoundException{
		adminService.getNumberOfEvents(null, NON_EXISTENT_CATEGORY_ID);
	}
	
	@Test
	public void testGetNumberOfEvents() throws InstanceNotFoundException{

		Category category1 = createFindEvents("cat1",null, null, false);
		
		createFindEvents("cat2",null, null, false);
		
		int numberEvents;
		
		numberEvents = adminService.getNumberOfEvents(null, category1.getCategoryId());
		assertTrue(numberEvents == 4);
		
		numberEvents = adminService.getNumberOfEvents(EVENTS_KEYWORDS_LOOK_FOR, null);
		assertTrue(numberEvents == 6);
		
		numberEvents = adminService.getNumberOfEvents(EVENTS_KEYWORDS_LOOK_FOR,  category1.getCategoryId());
		assertTrue(numberEvents == 3);
		
		numberEvents = adminService.getNumberOfEvents(null, null);
		assertTrue(numberEvents == 8);
	}
	
	
	//TESTS FINDEVENTS
	
	@Test(expected=InstanceNotFoundException.class)
	public void testFindEventsNonExistenCategory() throws InstanceNotFoundException{
		adminService.findEvents(null, NON_EXISTENT_CATEGORY_ID, 0, 10);
	}
	
	@Test
	public void testFindEventsByCategory() throws InstanceNotFoundException{
		
		List<Event> matchedKW1 = new ArrayList<>();
		List<Event> matchedCat1 = new ArrayList<>();
		Category category1 = createFindEvents("cat1", matchedKW1, matchedCat1, true);

		createFindEvents("cat2", null, null, false);
		
		List<Event> results;
		
		//findByCategory
		results = adminService.findEvents(null, category1.getCategoryId(), 0, 2);
		assertTrue(results.size()== 2);
		results.addAll(adminService.findEvents(null, category1.getCategoryId(), 2, 2));
		assertTrue(results.size()== 4);
		results.addAll(adminService.findEvents(null, category1.getCategoryId(), 4, 4));
		assertTrue(results.size()== 4);
		
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
		results = adminService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, null, 0, 2);
		assertTrue(results.size()== 2);
		results.addAll(adminService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, null, 2, 2));
		assertTrue(results.size()== 4);
		results.addAll(adminService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, null, 4, 4));
		assertTrue(results.size()== 6);
		
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
		results = adminService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, category.getCategoryId(), 0, 2);
		assertTrue(results.size()== 2);
		results.addAll(adminService.findEvents(EVENTS_KEYWORDS_LOOK_FOR, category.getCategoryId(), 2, 8));
		assertTrue(results.size()== 3);

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
		results = adminService.findEvents(null, null, 0, 2);
		assertTrue(results.size()== 2);
		results.addAll(adminService.findEvents(null, null, 2, 8));
		assertTrue(results.size()== 8);
		
		assertTrue(results.size()== (matchedCat1.size()+matchedCat2.size()));
		
		for(int i=0; i<results.size(); i++){
			assertTrue(matchedCat1.contains(results.get(i))||matchedCat2.contains(results.get(i)));
		}
	}
	
	@Test
	public void testFindEventsOrderByDate() throws InstanceNotFoundException{
		createFindEvents("cat1",null, null, false);
		createFindEvents("cat2",null, null, false);

		List<Event> results = adminService.findEvents(null, null, 0, 10);

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

		List<Event> results = adminService.findEvents("to it look words", null, 0, 10);
		int number = adminService.getNumberOfEvents("to it look words", null);
		assertTrue((results.size() == 6)&&(number == 6));

		results = adminService.findEvents("ook rds", null, 0, 10);
		number = adminService.getNumberOfEvents("ook rds", null);
		assertTrue((results.size() == 6)&&(number == 6));
		
		results = adminService.findEvents("o", null, 0, 10);
		number = adminService.getNumberOfEvents("o", null);
		assertTrue((results.size() == 8)&&(number == 8));

	}
	
	
	//TESTS OBTAINCATEGORIES
	
	@Test()
	public void testObtainCategories() {
		Category c1 = new Category("name1");
		Category c2 = new Category("name2");
		Category c3 = new Category("name3");
		categoryDao.save(c3);
		categoryDao.save(c2);
		categoryDao.save(c1);
		
		List<Category> l2 = adminService.obtainCategories();
	
		assertEquals(c1, l2.get(0));
		assertEquals(c2, l2.get(1));
		assertEquals(c3, l2.get(2));
		
	}
	
}


