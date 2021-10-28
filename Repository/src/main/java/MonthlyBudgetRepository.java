import org.hibernate.cfg.NotYetImplementedException;

public class MonthlyBudgetRepository implements IMonthlyBudgetRepository{
    @Override
    public MonthlyBudget findOne(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public Iterable<MonthlyBudget> findAll() {
        throw new NotYetImplementedException();
    }

    @Override
    public void save(MonthlyBudget entity) {
        throw new NotYetImplementedException();
    }

    @Override
    public void delete(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public void update(MonthlyBudget entity) {
        throw new NotYetImplementedException();
    }
}
