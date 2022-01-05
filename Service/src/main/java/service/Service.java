package service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import domain.*;
import dto.ExpenseDto;
import dto.MonthlyBudgetDto;
import dto.WishlistItemDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;
import service.exception.AuthorizationException;
import service.exception.ServiceException;
import service.productParser.CelParser;
import service.productParser.WebScraperServiceAltex;
import viewmodel.ExpenseViewModel;
import viewmodel.MonthlyBudgetViewModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import repository.IUserRequestRepository;
import repository.IWishlistItemRepository;
import service.mail.ActivateAccountEmailModel;
import service.mail.EmailType;
import service.mail.IEmailService;
import viewmodel.WishlistItemViewModel;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Service implements IService {
    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private final IExpenseRepository expenseRepository;
    @Autowired
    private final IMonthlyBudgetRepository monthlyBudgetRepository;
    @Autowired
    private final IUserRequestRepository userRequestRepository;
    @Autowired
    private final IEmailService emailService;
    @Autowired
    private final IWishlistItemRepository wishlistItemRepository;
    @Autowired
    private final CelParser celParser;
    @Autowired
    private final WebScraperServiceAltex altexParser;

    private static final long TOKEN_TIME = 8L * 60 * 60 * 1000; // 8 hours
    private static final Algorithm signingAlgorithm = Algorithm.HMAC256("super secret token secret");

    public Service(
            IUserRepository userRepository,
            IExpenseRepository expenseRepository,
            IMonthlyBudgetRepository monthlyBudgetRepository,
            IUserRequestRepository userRequestRepository,
            IWishlistItemRepository wishlistItemRepository,
            IEmailService emailService,
            CelParser celParser,
            WebScraperServiceAltex altexParser
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
        this.userRequestRepository = userRequestRepository;
        this.emailService = emailService;
        this.wishlistItemRepository=wishlistItemRepository;
        this.celParser = celParser;
        this.altexParser = altexParser;
    }

    private static String hashPassword(String password) {
        final String salt = "primarily sodium chloride";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            return new String(md.digest((password + salt)
                    .getBytes(StandardCharsets.US_ASCII)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String generateJWT(int id, String firstName, String lastName, long iat, long exp) {
        return JWT.create()
                .withClaim("id", id)
                .withClaim("first_name", firstName)
                .withClaim("last_name", lastName)
                .withClaim("iat", iat)
                .withClaim("exp", exp)
                .sign(signingAlgorithm);
    }

    @Override
    public String generateUserToken(User user) {
        long currentTime = System.currentTimeMillis();

        return generateJWT(user.getId(), user.getFirstName(), user.getLastName(), currentTime,
                currentTime + TOKEN_TIME);
    }

    @Override
    public Optional<User> getTokenUser(String token) {
        try {
            String encoded_payload = token.split("\\.")[1];
            JSONObject payload = new JSONObject(new String(Base64.getDecoder().decode(encoded_payload)));

            int id = (int) payload.get("id");
            long iat = (long) payload.get("iat"),
                    exp = (long) payload.get("exp");
            String first_name = (String) payload.get("first_name"),
                    last_name = (String) payload.get("last_name");

            String newToken = generateJWT(id, first_name, last_name, iat, exp);
            long currentTime = System.currentTimeMillis();

            if (!newToken.equals(token) || exp < currentTime) {
                return Optional.empty();
            }

            return userRepository.findOne(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> login(String email, String password) {
        String hash = hashPassword(password);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && Objects.equals(user.get().getPasswordHash(), hash)) {
            return user;
        }

        return Optional.empty();
    }

    @Override
    public ServiceEmptyResponse deleteMonthlyBudget(int budgetId, int userId) {
        ServiceEmptyResponse response = new ServiceEmptyResponse(200, "");

        Optional<MonthlyBudget> budgetToDelete = monthlyBudgetRepository.findOne(budgetId);

        if (budgetToDelete.isPresent()) {
            if (budgetToDelete.get().getUser().getId() != userId) {
                response.setStatus(403);
                response.setErrorMessage("Not allowed to delete this resource");
                return response;
            }

            Optional<MonthlyBudget> deletedBudget = monthlyBudgetRepository.delete(budgetId);

            if (!deletedBudget.isPresent()) {
                response.setStatus(500);
                response.setErrorMessage("Internal Server Error");
            }
        }
        return response;
    }

    @Override
    public ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException {
        Expense expense = Expense.fromExpenseDto(expenseDto);

        Optional<Expense> savedExpense = expenseRepository.save(expense);

        if (savedExpense.isPresent()) {
            throw new ServiceException("An error occurred while saving the expense.");
        }

        return ExpenseViewModel.fromExpense(expense);
    }


    @Override
    public ExpenseViewModel deleteExpense(int expenseId, int userId) throws ServiceException {

        Optional<Expense> expense = expenseRepository.findOne(expenseId);

        if (expense.isPresent()) {
            if (expense.get().getUser().getId() != userId) {
                throw new ServiceException("Forbidden access to this expense");
            }

            Optional<Expense> expenseToDelete = expenseRepository.delete(expenseId);

            if (!expenseToDelete.isPresent()) {
                throw new ServiceException("Internal server error");
            } else {
                return expenseToDelete.get().toExpenseViewModel();
            }
        } else {

            throw new ServiceException("This expense doesn't exist");
        }
    }

    public Iterable<Expense> getExpenses(int userId, String category, long startDate, long endDate) throws ServiceException {
        if (endDate < startDate)
            throw new ServiceException("Start date should be less than end date!");

        return expenseRepository.findByFilter(userId, category, startDate, endDate);

    }

    @Override
    public MonthlyBudgetViewModel addMonthlyBudget(MonthlyBudgetDto monthlyBudgetDto) throws ServiceException {
        MonthlyBudget monthlyBudget = MonthlyBudget.fromMonthlyBudgetDto(monthlyBudgetDto);

        Optional<MonthlyBudget> savedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

        if (savedMonthlyBudget.isPresent()) {
            throw new ServiceException("An error occurred while saving the expense.");
        }

        return MonthlyBudgetViewModel.fromMonthlyBudget(monthlyBudget);
    }

    @Override
    public ExpenseViewModel updateExpense(ExpenseDto expenseDto, int expenseId) throws ServiceException, AuthorizationException {
        Expense newExpense = Expense.fromExpenseDto(expenseDto);
        Optional<Expense> oldExpense = expenseRepository.findOne(expenseId);

        if (oldExpense.isEmpty()) {
            throw new ServiceException("Invalid expenseID");
        }

        if (newExpense.getUser().getId() != oldExpense.get().getUser().getId()) {
            throw new AuthorizationException(Constants.AuthorizationExceptionCode.FORBIDDEN);
        }

        newExpense.setId(expenseId);

        Optional<Expense> updatedExpense = expenseRepository.update(newExpense);
        if (updatedExpense.isPresent()) {
            throw new ServiceException("An error occurred while updating the expense.");
        }

        return ExpenseViewModel.fromExpense(newExpense);
    }

    @Override
    public Map<ExpenseCategory, Double> getExpenseTotalByCategory(int userId, LocalDateTime start, LocalDateTime end) {
        return expenseRepository.getTotalAmountByCategory(new User(userId), start, end);
    }

    @Override
    public Optional<UserRequest> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password) {
        // if the email is already used, another account with the same email cannot be created
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent())
            return Optional.of(new UserRequest());

        String passwordHash = hashPassword(password);
        String activationToken = generateJWT(0, email, firstName, 0,0);

        UserRequest newUserRequest = new UserRequest(email, firstName, lastName, dateOfBirth, passwordHash, activationToken);
        Optional<UserRequest> savedUserRequest = userRequestRepository.saveOrUpdate(newUserRequest);

        if (savedUserRequest.isPresent())
            return Optional.of(new UserRequest());

        ActivateAccountEmailModel emailModel = new ActivateAccountEmailModel(email, firstName, activationToken);
        emailService.sendMail(EmailType.ACTIVATE_ACCOUNT, emailModel);

        return Optional.empty();

    }

    /**
     * Activates the account corresponding to the UserRequest with the given activation token
     *
     * @param activationToken - activation token used to identify UserRequest
     * @return true if the activation is successful and false otherwise
     */
    @Override
    public boolean activateAccount(String activationToken) {
        Optional<UserRequest> existingUserRequestOptional = userRequestRepository.findByActivationToken(activationToken);
        if (existingUserRequestOptional.isEmpty())
            return false;

        UserRequest existingUserRequest = existingUserRequestOptional.get();
        Optional<User> savedUser = userRepository.save(new User(existingUserRequest.getEmail(),
                existingUserRequest.getFirstName(),
                existingUserRequest.getLastName(),
                existingUserRequest.getDateOfBirth(),
                existingUserRequest.getPasswordHash()));

        if (savedUser.isPresent())
            return false;

        Optional<UserRequest> deletedUserRequest = userRequestRepository.delete(existingUserRequest.getId());
        if (deletedUserRequest.isEmpty())
            return false;

        return true;
    }

    @Override
    public MonthlyBudgetViewModel updateMonthlyBudget(int budgetId, MonthlyBudgetDto monthlyBudgetDto) throws ServiceException {
        Optional<MonthlyBudget> budgetToUpdate = monthlyBudgetRepository.findOne(budgetId);

        if (budgetToUpdate.isPresent()) {
            if (budgetToUpdate.get().getUser().getId() != monthlyBudgetDto.getUserId()) {
                throw new AuthorizationException("Not allowed to modify this resource",
                        Constants.AuthorizationExceptionCode.FORBIDDEN);
            }

            MonthlyBudget monthlyBudget = MonthlyBudget.fromMonthlyBudgetDto(monthlyBudgetDto);
            monthlyBudget.setId(budgetId);
            Optional<MonthlyBudget> updatedBudget = monthlyBudgetRepository.update(monthlyBudget);

            if (updatedBudget.isPresent()) {
                throw new ServiceException("An error occurred while trying to modify this resource");
            }

            return MonthlyBudgetViewModel.fromMonthlyBudget(monthlyBudget);
        } else {
            throw new ServiceException("This resource doesn't exist");
        }
    }

    @Override
    public Iterable<MonthlyBudgetViewModel> getMonthlyBudgets(int userId, Date startDate, Date endDate) throws ServiceException {
        if (startDate.after(endDate)) {
            throw new ServiceException("Start date should be less than end date!");
        }

        return MonthlyBudgetViewModel.fromMonthlyBudgetList(
                monthlyBudgetRepository.findByFilter(userId, startDate, endDate)
        );
    }

    public Iterable<TotalExpensesDto> getTotalExpensesInTime(int userId, String granularity, LocalDate startDate, LocalDate endDate, String category) throws ServiceException {
        if (startDate.isAfter(endDate))
            throw new ServiceException("The start date should be before the end date");

        granularity = granularity.toUpperCase();
        if (!granularity.equals("YEAR") && !granularity.equals("MONTH") && !granularity.equals("DAY"))
            throw new ServiceException("The granularity should be year, month or day");

        return expenseRepository.findTotalExpensesInTimeByGranularity(userId, granularity, startDate, endDate, category);
    }

    @Override
    public WishlistItemViewModel addWishlistItem(WishlistItemDto wishlistItemDto) throws ServiceException {
        WishlistItem wishlistItem = WishlistItem.fromWishlistItemDto(wishlistItemDto);

        Optional<WishlistItem> savedWishlistItem = wishlistItemRepository.save(wishlistItem);

        if (savedWishlistItem.isPresent()) {
            throw new ServiceException("An error occurred while saving the wishlist.");
        }

        return WishlistItemViewModel.fromWishlistItem(wishlistItem);
    }

    @Override
    public Iterable<WishlistItemViewModel> getWishlistItems(int userId) {
        //TODO: set price dynamically by vendor

        return WishlistItemViewModel.fromWishlistItemList(
                wishlistItemRepository.findByUser(userId)
        );
    }

    @Override
    public Iterable<WishlistItemViewModel> getAffordableWishlistItems(int userId) {
        return WishlistItemViewModel.fromWishlistItemList(
                wishlistItemRepository.getAffordableWishlistItems(userId)
        );
    }

    @Override
    public ExpenseViewModel purchaseWishlistItem(int wishlistItemId, ExpenseDto expenseDto) throws ServiceException, AuthorizationException {
        Optional<WishlistItem> wishlistItemToBeDeleted = wishlistItemRepository.findOne(wishlistItemId);

        if (wishlistItemToBeDeleted.isEmpty()) {
            throw new ServiceException("The wishlist item does not exist");
        }
        if (wishlistItemToBeDeleted.get().getUser().getId() != expenseDto.getUserId()) {
            throw new AuthorizationException("Forbidden access to this wishlist item", Constants.AuthorizationExceptionCode.FORBIDDEN);
        }

        Expense expense = Expense.fromExpenseDto(expenseDto);
        Optional<Expense> savedExpense = expenseRepository.save(expense);
        if (savedExpense.isPresent()){
            throw new ServiceException("An error occurred while saving the expense");
        }

        Optional<WishlistItem> deletedWishlistItem = wishlistItemRepository.delete(wishlistItemId);
        if (deletedWishlistItem.isEmpty()){
            throw new ServiceException("An error occurred while deleting the wishlist item");
        }

        return ExpenseViewModel.fromExpense(expense);
    }

    public Iterable<WishlistItemViewModel> findProductsByKeywordAndVendor(String keyword, String vendor) throws ServiceException, IOException {
        if (vendor.equalsIgnoreCase("ALL")) {
            return WishlistItemViewModel.fromWishlistItemList(
                    Stream.concat(
                            altexParser.getProductsByKeyword(keyword).stream(),
                            celParser.getProductsByKeyword(keyword).stream()
                    ).collect(Collectors.toList())
            );
        }
        if (vendor.equalsIgnoreCase("CEL")) {
            return WishlistItemViewModel.fromWishlistItemList(celParser.getProductsByKeyword(keyword));
        }
        if (vendor.equalsIgnoreCase("ALTEX")) {
            return WishlistItemViewModel.fromWishlistItemList(altexParser.getProductsByKeyword(keyword));
        }

        throw new ServiceException("The vendor must be Cel, Altex or all!");
    }

    @Override
    public WishlistItemViewModel deleteWishlistItem(int wishlistItemId, int userId) throws ServiceException, AuthorizationException {
        Optional<WishlistItem> itemForDelete = wishlistItemRepository.findOne(wishlistItemId);

        if (itemForDelete.isEmpty()) {
            throw new ServiceException("The wishlist item does not exist");
        }
        if (itemForDelete.get().getUser().getId() != userId) {
            throw new AuthorizationException("Forbidden access to this wishlist item", Constants.AuthorizationExceptionCode.FORBIDDEN);
        }

        Optional<WishlistItem> deletedWishlistItem = wishlistItemRepository.delete(wishlistItemId);
        if (deletedWishlistItem.isEmpty()){
            throw new ServiceException("An error occurred while deleting the wishlist item");
        }

        return WishlistItemViewModel.fromWishlistItem(deletedWishlistItem.get());
    }
}
