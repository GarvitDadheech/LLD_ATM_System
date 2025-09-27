package com.atm.service;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.InvalidCardException;
import com.atm.model.Account;
import com.atm.model.Card;
import com.atm.service.validation.BlacklistValidationStrategy;
import com.atm.service.validation.CardFormatValidationStrategy;
import com.atm.service.validation.CardValidationStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealBankService implements BankService {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, String> cardPinMap = new HashMap<>();
    private final List<CardValidationStrategy> validationStrategies;

    public RealBankService() {
        initializeBankDatabase();
        
        // Initialize validation strategies
        this.validationStrategies = Arrays.asList(
            new CardFormatValidationStrategy(),
            new BlacklistValidationStrategy()
        );
    }
    
    private void initializeBankDatabase() {
        // Premium customer accounts
        Account premiumAccount1 = new Account("ACC001234567890", 25000.0);
        Account premiumAccount2 = new Account("ACC001234567891", 15000.0);
        
        // Standard customer accounts
        Account standardAccount1 = new Account("ACC002345678901", 3500.0);
        Account standardAccount2 = new Account("ACC002345678902", 1200.0);
        Account standardAccount3 = new Account("ACC002345678903", 850.0);
        
        // Business accounts
        Account businessAccount1 = new Account("ACC003456789012", 50000.0);
        Account businessAccount2 = new Account("ACC003456789013", 25000.0);
        
        // Map cards to accounts
        accounts.put("4532-1234-5678-9012", premiumAccount1);
        accounts.put("4532-1234-5678-9013", premiumAccount2);
        accounts.put("5555-4444-3333-2222", standardAccount1);
        accounts.put("4111-1111-1111-1111", standardAccount2);
        accounts.put("4000-0000-0000-0002", standardAccount3);
        accounts.put("3782-822463-10005", businessAccount1);
        accounts.put("3714-496353-98431", businessAccount2);
        
        // Map cards to PINs (in real system, these would be hashed)
        cardPinMap.put("4532-1234-5678-9012", "1234");
        cardPinMap.put("4532-1234-5678-9013", "5678");
        cardPinMap.put("5555-4444-3333-2222", "1111");
        cardPinMap.put("4111-1111-1111-1111", "2222");
        cardPinMap.put("4000-0000-0000-0002", "3333");
        cardPinMap.put("3782-822463-10005", "9999");
        cardPinMap.put("3714-496353-98431", "8888");
        
        System.out.println("Bank database initialized with " + accounts.size() + " active accounts.");
    }

    private void runAllValidations(Card card) throws InvalidCardException {
        for (CardValidationStrategy strategy : validationStrategies) {
            strategy.validate(card);
        }
    }

    @Override
    public Account getAccount(Card card) throws InvalidCardException {
        runAllValidations(card); // Run validations first
        System.out.println("BankService: Processing account lookup for card ending in " + 
                          card.getCardNumber().substring(card.getCardNumber().length() - 4));
        
        Account account = accounts.get(card.getCardNumber());
        if (account == null) {
            System.out.println("BankService: Card not found in our database.");
            throw new InvalidCardException("This card is not registered with our bank.");
        }
        
        System.out.println("BankService: Account found - " + account.getAccountNumber());
        return account;
    }

    @Override
    public boolean authenticate(Card card, String pin) throws InvalidCardException {
        runAllValidations(card); 
        System.out.println("BankService: Verifying PIN for card ending in " + 
                          card.getCardNumber().substring(card.getCardNumber().length() - 4));
        
        if (!cardPinMap.containsKey(card.getCardNumber())) {
            System.out.println("BankService: Authentication failed - card not registered.");
            throw new InvalidCardException("This card is not registered with our bank.");
        }
        
        boolean isValid = cardPinMap.get(card.getCardNumber()).equals(pin);
        if (isValid) {
            System.out.println("BankService: PIN verification successful.");
        } else {
            System.out.println("BankService: PIN verification failed.");
        }
        return isValid;
    }

    @Override
    public double getBalance(Account account) {
        System.out.println("BankService: Retrieving current balance for account " + account.getAccountNumber());
        double balance = account.getBalance();
        System.out.println("BankService: Current balance: $" + String.format("%.2f", balance));
        return balance;
    }

    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        System.out.println("BankService: Processing withdrawal request of $" + String.format("%.2f", amount) + 
                          " from account " + account.getAccountNumber());
        
        double currentBalance = account.getBalance();
        System.out.println("BankService: Current balance before withdrawal: $" + String.format("%.2f", currentBalance));
        
        if (amount <= 0) {
            throw new InsufficientFundsException("Invalid withdrawal amount.");
        }
        
        if (currentBalance < amount) {
            System.out.println("BankService: Withdrawal declined - insufficient funds.");
            throw new InsufficientFundsException("Insufficient funds. Available balance: $" + String.format("%.2f", currentBalance));
        }
        
        if (!account.withdraw(amount)) {
            throw new InsufficientFundsException("Transaction failed due to account restrictions.");
        }
        
        System.out.println("BankService: Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
    }
}
