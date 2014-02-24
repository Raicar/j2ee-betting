package es.udc.isg011.apuestas.model.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("categoryDao")
public class CategoryDaoHibernate extends GenericDaoHibernate<Category, Long> implements CategoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> obtainCategories() {
		return getSession().createQuery(
				"SELECT c FROM Category c " +
				"ORDER BY c.name").
				list();
	}

}
