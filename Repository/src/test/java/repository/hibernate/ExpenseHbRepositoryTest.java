package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import domain.User;
import org.junit.jupiter.api.*;
import repository.IExpenseRepository;
import repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ExpenseHbRepositoryTest {
    private final IExpenseRepository expenseRepository = new ExpenseHbRepository();
    private final IUserRepository userRepository = new UserHbRepository();

    @BeforeEach
    void insertDefaultExpenses() {
        Constants.defaultUsers.forEach(userRepository::save);
        Constants.defaultExpenses.forEach(expenseRepository::save);
    }

    @AfterEach
    void deleteDefaultExpenses() {
        Constants.defaultExpenses.stream().map(Expense::getId).forEach(expenseRepository::delete);
        Constants.defaultUsers.stream().map(User::getId).forEach(userRepository::delete);
    }

    @TestFactory
    public Stream<DynamicTest> findOne() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<Expense> expected;

            TestCase(String name, int id, Optional<Expense> expected) {
                this.name = name;
                this.id = id;
                this.expected = expected;
            }

            public void check() {
                Optional<Expense> computed = expenseRepository.findOne(id);
                Assertions.assertEquals(expected, computed);
            }

            public String getName() {
                return name;
            }
        }

        Expense expense = Constants.defaultExpenses.get(0);
        var testCases = new TestCase[]{
                new TestCase("Find Expense Successful", expense.getId(), Optional.of(expense)),
                new TestCase("Find Expense Unsuccessful", Integer.MAX_VALUE, Optional.empty())
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @Test
    public void testFindAll() {
        List<Expense> expenses = StreamSupport.stream(
                expenseRepository.findAll().spliterator(),
                false
        ).collect(Collectors.toList());
        Assertions.assertEquals(Constants.defaultExpenses.size(), expenses.size());
        expenses.stream()
                .map(Constants.defaultExpenses::contains)
                .forEach(Assertions::assertTrue);
    }

    @TestFactory
    public Stream<DynamicTest> save() {
        class TestCase {
            private final String name;
            private final Expense expense;
            private final Optional<Expense> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, Expense expense, Optional<Expense> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.expense = expense;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<Expense> computed = expenseRepository.save(expense);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // successfully saved, find it to confirm
                    Optional<Expense> expenseFound = expenseRepository.findOne(expense.getId());
                    Assertions.assertTrue(expenseFound.isPresent());
                    Assertions.assertEquals(expenseFound.get(), expense);

                    // verify if the relationship has been defined
                    Optional<User> user = userRepository.findOne(expense.getUser().getId());
                    Assertions.assertTrue(user.isPresent());
                    Assertions.assertTrue(user.get().getExpenses().contains(expense));
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
                new TestCase("Save Expense successful", Constants.expenseToSave, Optional.empty(),null),
                new TestCase("Save Expense null", null, null, IllegalArgumentException.class)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> delete() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<Expense> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, int id, Optional<Expense> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.id = id;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<Expense> expense = expenseRepository.findOne(id);
                    Optional<Expense> computed = expenseRepository.delete(id);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // successfully deleted, finding it should fail
                    Assertions.assertTrue(expenseRepository.findOne(id).isEmpty());

                    // the user should not have the expense anymore, if the deletion was successful
                    if (expense.isPresent()) {
                        Optional<User> user = userRepository.findOne(expense.get().getUser().getId());
                        Assertions.assertTrue(user.isPresent());
                        Assertions.assertFalse(user.get().getExpenses().contains(expense.get()));
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

        Expense expenseToDelete = Constants.defaultExpenses.get(0);
        var testCases = new TestCase[]{
                new TestCase("Delete Expense successfully", expenseToDelete.getId(), Optional.of(expenseToDelete), null),
                new TestCase("Delete Expense non-existent", Integer.MAX_VALUE, Optional.empty(), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> update() {
        class TestCase {
            private final String name;
            private final Expense expense;
            private final Optional<Expense> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, Expense expense, Optional<Expense> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.expense = expense;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<Expense> computed = expenseRepository.update(expense);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // successfully updated, should match the find result
                    if (computed.isEmpty()) {
                        Optional<Expense> expenseFound = expenseRepository.findOne(expense.getId());
                        Assertions.assertTrue(expenseFound.isPresent());
                        Assertions.assertEquals(expense, expenseFound.get());
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

        Expense expense = new Expense(987, ExpenseCategory.SelfCare, LocalDateTime.now(), Constants.defaultUsers.get(0));
        expense.setId(Constants.defaultExpenses.get(0).getId());

        Expense expenseNonExistent = new Expense(824, ExpenseCategory.Housekeeping, LocalDateTime.now(), Constants.defaultUsers.get(0));
        expenseNonExistent.setId(Integer.MAX_VALUE);

        var testCases = new TestCase[]{
                new TestCase("Update Expense successfully", expense, Optional.empty(), null),
                new TestCase("Update Expense non-existent expense", expenseNonExistent, Optional.of(expenseNonExistent), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }
}