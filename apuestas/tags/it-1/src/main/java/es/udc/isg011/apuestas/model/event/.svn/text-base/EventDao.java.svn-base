package es.udc.isg011.apuestas.model.event;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface EventDao extends GenericDao<Event, Long>{

	public List<Event> findByKeyWordsCategoryAdmin(String keyWords,
			Long categoryId, int startIndex, int count);

	public int getNumberOfEventsByKeyWordsCategoryAdmin(String keyWords,
			Long categoryId);


	public List<Event> findByKeyWordsCategoryUser(String keyWords,
			Long categoryId, int startIndex, int count);

	public int getNumberOfEventsByKeyWordsCategoryUser(String keyWords,
			Long categoryId);
}
