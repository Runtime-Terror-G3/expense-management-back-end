import org.hibernate.cfg.NotYetImplementedException;

public class ExpenseRepository implements IExpenseRepository{
    @Override
    public Expense findOne(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public Iterable<Expense> findAll() {
        throw new NotYetImplementedException();
    }

    @Override
    public void save(Expense entity) {
        throw new NotYetImplementedException();
    }

    @Override
    public void delete(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public void update(Expense entity) {
        throw new NotYetImplementedException();
    }
}
