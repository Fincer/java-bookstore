package com.fjordtek.bookstore.model.book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pekka Helenius
 */

@Transactional
public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/*
	 * Selective update
	 * Hibernate takes care of entityManager transactions (begin, commit, rollback, close, etc.)
	 */
	@Override
	public void updateWithoutPrice(Book book) {
		try {


			/*
			 * Book: refers to entity class, not table name!
			 */
			Query query = entityManager.createQuery(
					"UPDATE Book SET"
							+ " author_id   = :author,"
							+ " category_id = :category,"
							+ " isbn        = :isbn,"
							+ " title       = :title,"
							+ " year        = :year"
							+ " WHERE id    = :id"
					);

			query.setParameter("author",   book.getAuthor().getId());
			query.setParameter("category", book.getCategory().getId());
			query.setParameter("isbn",     book.getIsbn());
			query.setParameter("title",    book.getTitle());
			query.setParameter("year",     book.getYear());
			query.setParameter("id",       book.getId());

			query.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}