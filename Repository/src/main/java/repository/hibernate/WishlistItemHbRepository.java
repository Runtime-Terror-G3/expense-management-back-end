package repository.hibernate;

import domain.Expense;
import domain.MonthlyBudget;
import domain.WishlistItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IWishlistItemRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class WishlistItemHbRepository extends AbstractHbRepository<Integer, WishlistItem> implements IWishlistItemRepository {
    @Override
    protected Query<WishlistItem> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from WishlistItem where id=:id", WishlistItem.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<WishlistItem> getFindAllQuery(Session session) {
        return session.createQuery("from WishlistItem", WishlistItem.class);
    }

    @Override
    public Iterable<WishlistItem> findByUser(int userId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<WishlistItem> sqlQuery;

            String sqlQueryString = "select new domain.WishlistItem(id, title, price, link, image, vendor) " +
                    "from WishlistItem where userid=:userId";

            sqlQuery = session.createQuery(sqlQueryString, WishlistItem.class);

            List<WishlistItem> wishlistItems = sqlQuery
                    .setParameter("userId", userId)
                    .list();
            transaction.commit();
            return wishlistItems;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return new ArrayList<>();
    }

    @Override
    public Iterable<WishlistItem> getAffordableWishlistItems(int userId) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<WishlistItem> sqlQuery;

            String sqlQueryString = "select w.id, w.title, w.price, w.link, w.image, w.vendor, w.userid " +
                    "from wishlistitems w " +
                    "where w.userid=:userId and w.price <= ((select sum(mb.income) from monthlybudgets mb where mb.userid=:userId) - (select sum(e.amount) from expenses e where e.userid=:userId))";

            sqlQuery = session.createNativeQuery(sqlQueryString, WishlistItem.class);

            List<WishlistItem> affordableItems = sqlQuery
                    .setParameter("userId", userId)
                    .list();
            transaction.commit();

            return affordableItems;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            if (transaction != null)
                transaction.rollback();
        }
        return new ArrayList<>();
    }
}
