package es.udc.isg011.apuestas.model.event;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("eventDao")
public class EventDaoHibernate extends GenericDaoHibernate<Event, Long> implements EventDao{
	
	@SuppressWarnings("unchecked")
	private List<Long> getEventsAdminRestriction(){
		Query query = getSession().createQuery(
                "SELECT DISTINCT e.eventId FROM Event e " +
                " WHERE "+
	                " e.eventId NOT IN ( SELECT DISTINCT bt.event.eventId "+
	                					"FROM BetType bt ) " +//events without any betType
	                "OR"+
					" e.eventId IN ( SELECT DISTINCT bt.event.eventId "+// BetType bt JOIN bt.betOptions 
					"FROM BetType bt JOIN bt.betOptions bo " + //JOIN condition ON bt2.betTypeId = bo.betType.betTypeId
					"WHERE bo.winner IS NULL ) "
				
				);
		
		 List<Long> matchedEventIds = query.list();
		
		return matchedEventIds;
	}
	
	private String getKeyWordsCondition(String keyWords){// Always starting with AND
		//obtain vector with key words
		String[] keyWordsVector = keyWords.split("\\W");
		//construct key words conditions
		String keyWordsConditions="";
		for(int i =0; i<keyWordsVector.length ; i++){
			if(keyWordsVector[i].length()>0) keyWordsConditions = keyWordsConditions + " AND LOWER(e.name) LIKE :"+"keyWords"+i+" ";
		}
		return keyWordsConditions;
	}
	
	private Query setKeyWordsParameters(Query query, String keyWords){
		//obtain vector with key words
		String[] keyWordsVector = keyWords.split("\\W");
		//set key words parameters
		for(int i =0; i<keyWordsVector.length ; i++){
			if(keyWordsVector[i].length()>0) 	query.setParameter("keyWords"+i, "%"+keyWordsVector[i].toLowerCase()+"%");
		}
        return query;  
	}
	
	private String buildConditions(String keyWords, Long categoryId) { // Always starting with AND
		String keyWordsConditions="";
		String categoryConditions="";
		if(keyWords!=null){
			keyWordsConditions=getKeyWordsCondition(keyWords);	
		}
		
		if(categoryId!=null){
			categoryConditions = " AND e.category.categoryId = :categoryId ";
		}
		return keyWordsConditions + categoryConditions; // AND k1 ==k2 .... AND cId.. || AND k1 ==k2 .... ||AND cId..
		
	}
	
	private Query setQueryParameters(Query query, String keyWords,
			Long categoryId) {
		if(keyWords!=null){
			query = setKeyWordsParameters(query, keyWords);
		}
		if(categoryId!=null){
			query.setParameter("categoryId", categoryId);
		}
		return query;
	}
	
	
	/***********************ADMIN***********************/
	
	private Query getQueryByKeyWordsCategoryAdmin(String keyWords, Long categoryId, boolean getNumber) {
		
		String conditions = buildConditions(keyWords,categoryId);
		List<Long> matchedEventIds = getEventsAdminRestriction();
		
		String queryHead;
		if(getNumber==true){
			queryHead = " COUNT(e) ";
		}else queryHead = " e ";
		
		Query query = getSession().createQuery(
                "SELECT"+ queryHead +"FROM Event e "+
        		"WHERE " +
        			"e.eventId IN :matchedEventIds "+
        			conditions+ //conditions empieza con AND
    			"ORDER BY e.date"
        			);
        
		//set key words parameters
		query = setQueryParameters(query, keyWords, categoryId);
             
		//set rest of parameters
		query.setParameterList("matchedEventIds", matchedEventIds);
		return query;
	}


	@Override
	public int getNumberOfEventsByKeyWordsCategoryAdmin(String keyWords,
			Long categoryId) {
		
		Query query = getQueryByKeyWordsCategoryAdmin(keyWords, categoryId, true);
	
		long numberOfEvents = (long) query.uniqueResult();
				
        return (int) numberOfEvents;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Event> findByKeyWordsCategoryAdmin(String keyWords,
			Long categoryId, int startIndex, int count) {
			
		Query query = getQueryByKeyWordsCategoryAdmin(keyWords, categoryId, false);
		
		query.setFirstResult(startIndex).
        	setMaxResults(count);

        return query.list();
	}

	
	
	
	/***********************USER***********************/

	private Query getQueryByKeyWordsCategoryUser(String keyWords, Long categoryId, boolean getNumber) {
		
		String conditions = buildConditions(keyWords,categoryId);
		
		String queryHead;
		if(getNumber==true){
			queryHead = " COUNT(e) ";
		}else queryHead = " e ";
		
		Query query = getSession().createQuery(
                "SELECT"+ queryHead +"FROM Event e "+
        		"WHERE " +
        			"e.date > :currentDate "+
        			conditions+ //conditions empieza con AND
    			"ORDER BY e.date"
        			);
        
		//set key words parameters
		query = setQueryParameters(query, keyWords, categoryId);
             
		//set rest of parameters
		query.setParameter("currentDate", Calendar.getInstance());
		return query;
	}


	@Override
	public int getNumberOfEventsByKeyWordsCategoryUser(String keyWords,
			Long categoryId) {
		
		Query query = getQueryByKeyWordsCategoryUser(keyWords, categoryId, true);
	
		long numberOfEvents = (long) query.uniqueResult();
				
        return (int) numberOfEvents;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Event> findByKeyWordsCategoryUser(String keyWords,
			Long categoryId, int startIndex, int count) {
			
		Query query = getQueryByKeyWordsCategoryUser(keyWords, categoryId, false);
		
		query.setFirstResult(startIndex).
        	setMaxResults(count);

        return query.list();
	}

	
	
	

}
