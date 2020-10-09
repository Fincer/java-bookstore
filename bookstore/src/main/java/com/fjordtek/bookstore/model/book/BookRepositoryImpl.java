//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * This class implements methods defined in BookRepositoryCustom interface.
 *
 * @author Pekka Helenius
 */

@Transactional
public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/*
	 * WARNING: Custom queries MAY BREAK in some SQL server implementations.
	 * Basically, a custom query can work in MySQL server environment whereas it breaks
	 * in PostgreSQL or Microsoft SQL server context.
	 *
	 * Make sure that any custom query you define is properly supported by your target
	 * SQL server.
	 */

	/*
	 * Selective update
	 * Hibernate takes care of entityManager transactions (begin, commit, rollback, close, etc.)
	 */
	@Override
	public void updateWithoutPriceAndWithoutPublish(Book book) {
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

	/*
	 * Find all published books
	 */
	@Override
	public List<Book> findAllPublished() {
		try {

			TypedQuery<Book> query = entityManager.createQuery(
					"SELECT i FROM Book i WHERE publish = 1",
					Book.class
					);

			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}