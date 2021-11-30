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
/*
   @Override
    public Optional<WishlistItem> save(WishlistItem entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
        Optional<WishlistItem> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query q=session.createNativeQuery("insert into wishlistitems(title,price,link,image,vendor,userId,id) values (?,?,?,?,?,?,?)")
                    .setParameter(1,entity.getTitle())
                    .setParameter(2,entity.getPrice())
                    .setParameter(3,entity.getLink())
                    .setParameter(4,entity.getImage())
                    .setParameter(5,entity.getVendor())
                    .setParameter(6,entity.getUser().getId())
                    .setParameter(7,0);
            q.executeUpdate();
            transaction.commit();
            //int id= ((BigInteger)q.getSingleResult()).intValue();
            //entity.setId(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
            result = Optional.of(entity);
        }

        return result;
    }*/

}
