package repository.hibernate;

import domain.MonthlyBudget;
import domain.User;
import org.junit.jupiter.api.*;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class MonthlyBudgetHbRepositoryTest {
    private final IMonthlyBudgetRepository monthlyBudgetRepository = new MonthlyBudgetHbRepository();
    private final IUserRepository userRepository = new UserHbRepository();

    @BeforeEach
    void insertDefaultExpenses() {
        Constants.defaultUsers.forEach(userRepository::save);
        Constants.defaultMonthlyBudgets.forEach(monthlyBudgetRepository::save);
    }

    @AfterEach
    void deleteDefaultExpenses() {
        Constants.defaultMonthlyBudgets.stream().map(MonthlyBudget::getId).forEach(monthlyBudgetRepository::delete);
        Constants.defaultUsers.stream().map(User::getId).forEach(userRepository::delete);
    }

    @TestFactory
    public Stream<DynamicTest> findOne() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<MonthlyBudget> expected;

            TestCase(String name, int id, Optional<MonthlyBudget> expected) {
                this.name = name;
                this.id = id;
                this.expected = expected;
            }

            public void check() {
                Optional<MonthlyBudget> computed = monthlyBudgetRepository.findOne(id);
                Assertions.assertEquals(expected, computed);
            }

            public String getName() {
                return name;
            }
        }

        MonthlyBudget budget = Constants.defaultMonthlyBudgets.get(0);
        var testCases = new TestCase[]{
                new TestCase("Find Monthly Budget successful", budget.getId(), Optional.of(budget)),
                new TestCase("Find Monthly Budget unsuccessful", Integer.MAX_VALUE, Optional.empty())
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @Test
    public void testFindAll() {
        List<MonthlyBudget> monthlyBudgets = StreamSupport.stream(monthlyBudgetRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assertions.assertEquals(Constants.defaultMonthlyBudgets.size(), monthlyBudgets.size());
        monthlyBudgets.stream()
                .map(Constants.defaultMonthlyBudgets::contains)
                .forEach(Assertions::assertTrue);
    }

    @TestFactory
    public Stream<DynamicTest> save() {
        class TestCase {
            private final String name;
            private final MonthlyBudget monthlyBudget;
            private final Optional<MonthlyBudget> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, MonthlyBudget monthlyBudget, Optional<MonthlyBudget> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.monthlyBudget = monthlyBudget;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<MonthlyBudget> computed = monthlyBudgetRepository.save(monthlyBudget);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    Optional<MonthlyBudget> budgetFound = monthlyBudgetRepository.findOne(monthlyBudget.getId());
                    Assertions.assertTrue(budgetFound.isPresent());
                    Assertions.assertEquals(budgetFound.get(), monthlyBudget);

                    Optional<User> user = userRepository.findOne(monthlyBudget.getUser().getId());
                    Assertions.assertTrue(user.isPresent());
                    Assertions.assertTrue(user.get().getBudgets().contains(monthlyBudget));
                } catch (Exception e) {
                    Assertions.assertNotNull(exception);
                    Assertions.assertEquals(exception, e.getClass());
                }
            }

            public String getName() {
                return name;
            }
        }

        var testCases = new TestCase[]{
                new TestCase("Save Monthly Budget successful", Constants.budgetToSave, Optional.empty(), null),
                new TestCase("Save Monthly Budget null", null, null, IllegalArgumentException.class)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> delete() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<MonthlyBudget> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, int id, Optional<MonthlyBudget> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.id = id;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<MonthlyBudget> monthlyBudget = monthlyBudgetRepository.findOne(id);

                    Optional<MonthlyBudget> computed = monthlyBudgetRepository.delete(id);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // if successfully saved
                    if (monthlyBudget.isPresent()) {
                        Assertions.assertTrue(monthlyBudgetRepository.findOne(id).isEmpty());

                        Optional<User> user = userRepository.findOne(monthlyBudget.get().getUser().getId());
                        Assertions.assertTrue(user.isPresent());
                        Assertions.assertFalse(user.get().getBudgets().contains(monthlyBudget.get()));
                    }
                } catch (Exception e) {
                    Assertions.assertNotNull(exception);
                    Assertions.assertEquals(exception, e.getClass());
                }
            }

            public String getName() {
                return name;
            }
        }

        MonthlyBudget budget = Constants.defaultMonthlyBudgets.get(0);
        var testCases = new TestCase[]{
                new TestCase("Delete Monthly Budget successfully", budget.getId(), Optional.of(budget), null),
                new TestCase("Delete Monthly Budget non-existent", Integer.MAX_VALUE, Optional.empty(), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> update() {
        class TestCase {
            private final String name;
            private final MonthlyBudget budget;
            private final Optional<MonthlyBudget> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, MonthlyBudget budget, Optional<MonthlyBudget> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.budget = budget;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<MonthlyBudget> computed = monthlyBudgetRepository.update(budget);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // if successfully updated
                    if (expected.isEmpty()) {
                        Assertions.assertEquals(budget, monthlyBudgetRepository.findOne(budget.getId()).orElse(null));
                    }
                } catch (Exception e) {
                    Assertions.assertNotNull(exception);
                    Assertions.assertEquals(exception, e.getClass());
                }
            }

            public String getName() {
                return name;
            }
        }

        MonthlyBudget budget = new MonthlyBudget(Constants.defaultUsers.get(0), 111900, new Date());
        budget.setId(Constants.defaultMonthlyBudgets.get(0).getId());

        MonthlyBudget budgetNonExistent = new MonthlyBudget(Constants.defaultUsers.get(1), 99, new Date());
        budgetNonExistent.setId(Integer.MAX_VALUE);

        var testCases = new TestCase[]{
                new TestCase("Update Monthly Budget successfully", budget, Optional.empty(), null),
                new TestCase("Update Monthly Budget non-existent budget", budgetNonExistent, Optional.of(budgetNonExistent), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }
}