package repository.hibernate;

import domain.User;
import org.junit.jupiter.api.*;
import repository.IUserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class UserHbRepositoryTest {
    private final IUserRepository userRepository = new UserHbRepository();

    /**
     * Method for inserting some predetermined users.
     * Using the annotation @BeforeEach, it is guaranteed that at each test case these users will exist.
     */
    @BeforeEach
    void insertDefaultUsers() {
        Constants.defaultUsers.forEach(userRepository::save);
    }

    /**
     * Method for deleting all the users in the repository.
     * Using the annotation @AfterEach, it is guaranteed that the repository will be empty after every test.
     */
    @AfterEach
    void deleteUsers() {
        StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(User::getId)
                .forEach(userRepository::delete);
    }

    @TestFactory
    public Stream<DynamicTest> findOne() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<User> expected;

            TestCase(String name, int id, Optional<User> expected) {
                this.name = name;
                this.id = id;
                this.expected = expected;
            }

            public void check() {
                Optional<User> computed = userRepository.findOne(id);
                Assertions.assertEquals(expected, computed);
            }

            public String getName() {
                return name;
            }
        }

        User user = Constants.defaultUsers.get(0);
        var testCases = new TestCase[]{
                new TestCase("Find User successful", user.getId(), Optional.of(user)),
                new TestCase("Find User unsuccessful", Integer.MAX_VALUE, Optional.empty())
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @Test
    public void findAll() {
        Iterable<User> usersIterable = userRepository.findAll();
        List<User> users = StreamSupport.stream(usersIterable.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(Constants.defaultUsers.size(), users.size());
        users.stream()
                .map(Constants.defaultUsers::contains)
                .forEach(Assertions::assertTrue);
    }

    @TestFactory
    public Stream<DynamicTest> save() {
        class TestCase {
            private final String name;
            private final User user;
            private final Class<? extends Exception> exception;

            TestCase(String name, User user, Class<? extends Exception> exception) {
                this.name = name;
                this.user = user;
                this.exception = exception;
            }

            public void check() {
                try {
                    userRepository.save(user);
                    Assertions.assertNull(exception);

                    // successfully saved the user, find it to confirm
                    Optional<User> userFound = userRepository.findOne(user.getId());
                    Assertions.assertTrue(userFound.isPresent());
                    Assertions.assertEquals(userFound.get(), user);
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
                new TestCase("Save User successful", Constants.userToSave, null),
                new TestCase("Save User null", null, IllegalArgumentException.class)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> delete() {
        class TestCase {
            private final String name;
            private final int id;
            private final Optional<User> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, int id, Optional<User> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.id = id;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<User> computed = userRepository.delete(id);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // successfully deleted, finding it should fail
                    Assertions.assertTrue(userRepository.findOne(id).isEmpty());
                } catch (Exception e) {
                    Assertions.assertNotNull(exception);
                    Assertions.assertEquals(exception, e.getClass());
                }
            }

            public String getName() {
                return name;
            }
        }

        User userToDelete = Constants.defaultUsers.get(0);
        var testCases = new TestCase[]{
                new TestCase("Delete User successfully", userToDelete.getId(), Optional.of(userToDelete), null),
                new TestCase("Delete User non-existent", Integer.MAX_VALUE, Optional.empty(), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }

    @TestFactory
    public Stream<DynamicTest> update() {
        class TestCase {
            private final String name;
            private final User user;
            private final Optional<User> expected;
            private final Class<? extends Exception> exception;

            TestCase(String name, User user, Optional<User> expected, Class<? extends Exception> exception) {
                this.name = name;
                this.user = user;
                this.expected = expected;
                this.exception = exception;
            }

            public void check() {
                try {
                    Optional<User> computed = userRepository.update(user);
                    Assertions.assertNull(exception);
                    Assertions.assertEquals(expected, computed);

                    // if successfully updated, should match the find result
                    if (expected.isEmpty()) {
                        Assertions.assertEquals(user, userRepository.findOne(user.getId()).orElse(null));
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

        User user = new User("xyz@g.com", "X", "Y", new Date(), "hashX");
        user.setId(Constants.defaultUsers.get(0).getId());

        User userNonExistent = new User("", "", "", new Date(), "");
        userNonExistent.setId(Integer.MAX_VALUE);
        var testCases = new TestCase[]{
                new TestCase("Update user successfully", user, Optional.empty(), null),
                new TestCase("Update user non-existent user", userNonExistent, Optional.of(userNonExistent), null)
        };

        return DynamicTest.stream(Stream.of(testCases), TestCase::getName, TestCase::check);
    }
}
