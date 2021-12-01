package repository.hibernate;


import domain.Expense;
import domain.MonthlyBudget;
import domain.WishlistItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IWishlistItemRepository;

import java.math.BigInteger;
import java.util.Optional;

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


}
