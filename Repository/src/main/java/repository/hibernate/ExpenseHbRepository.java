package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExpenseHbRepository extends AbstractHbRepository<Integer, Expense> implements IExpenseRepository {
    @Override
    protected Query<Expense> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from Expense where id=:id", Expense.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<Expense> getFindAllQuery(Session session) {
        return session.createQuery("from Expense", Expense.class);
    }

    @Override
    public Iterable<Expense> findByFilter(int userId, String category, long startDate, long endDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Expense> sqlQuery;

            String sqlQueryString = "select new domain.Expense(id, amount, category, date) " +
                    "from Expense where userid=:userId " +
                    "and date between :startDate and :endDate ";

            if (!category.equalsIgnoreCase("ALL")) {
                sqlQueryString += "and category=:category order by date desc";
                sqlQuery = session.createQuery(sqlQueryString, Expense.class)
                    .setParameter("category", ExpenseCategory.valueOf(category));
            } else {
                sqlQueryString += "order by date desc";
                sqlQuery = session.createQuery(sqlQueryString, Expense.class);
            }
            List<Expense> expenses = sqlQuery
                    .setParameter("userId", userId)
                    .setParameter("startDate",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(startDate),
                                TimeZone.getDefault().toZoneId()))
                    .setParameter("endDate",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(endDate),
                                TimeZone.getDefault().toZoneId()))
                    .list();
            transaction.commit();
            return expenses;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return new ArrayList<>();
    }

    @Override
    public Map<ExpenseCategory, Double> getTotalAmountByCategory(User user, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<ExpenseCategoryAmountTuple> criteriaQuery = builder.createQuery(ExpenseCategoryAmountTuple.class);
            Root<Expense> root = criteriaQuery.from(Expense.class);

            Predicate predicateDate = builder.between(root.get("date"), start, end);
            Predicate predicateUser = builder.equal(root.<User>get("user"), user);

            criteriaQuery.where(builder.and(predicateUser, predicateDate));
            criteriaQuery.groupBy(root.get("category"));
            criteriaQuery.multiselect(root.get("category"), builder.sum(root.get("amount")));

            Query<ExpenseCategoryAmountTuple> query = session.createQuery(criteriaQuery);
            List<ExpenseCategoryAmountTuple> resultList = query.getResultList();

            return resultList.stream()
                    .collect(Collectors.toMap(ExpenseCategoryAmountTuple::getCategory, ExpenseCategoryAmountTuple::getAmount));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}

class ExpenseCategoryAmountTuple {
    private final ExpenseCategory category;
    private final double amount;

    public ExpenseCategoryAmountTuple(ExpenseCategory category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ExpenseCategoryAmountTuple{" +
                "category=" + category +
                ", amount=" + amount +
                '}';
    }
}
