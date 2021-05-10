package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum Currency {
    CZK, USD, EUR
}

public class Main {

    public static void main(String[] args) throws Exception {
        Person franta = new Person("Franta", "Hovorka", "1414142");
        Person pepa = new Person("Pepa", "Vajčko", "1414515");
        Bank banka = new Bank("Komercka", "85248");
        Account Pepos = banka.CreateAccount(pepa, Currency.CZK);
        Account Frantík = banka.CreateAccount(franta, Currency.CZK);
        pepa.Deposit(55555, Pepos);
        banka.Transfer(Pepos, Frantík, 50000F);
        franta.Withdraw(5555, Frantík);
    }//DEBUG
}

class Person {
    private final String firstName, lastName, birthNumber;

    private final List<Account> accounts;

    public Person(String firstName, String lastName, String birthNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthNumber = birthNumber;
        this.accounts = new ArrayList<>();

    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void Deposit(float amount, Account account) {
        account.Deposit(amount);
    }

    public void Withdraw(float amount, Account account) throws Exception {
        account.Withdraw(amount);
    }

    public Account CreateAccount(Bank bank, Currency currency) {
        Account account = bank.CreateAccount(this, currency);
        accounts.add(account);
        return account;
    }

    public void DeleteAccount(Account account) {
        accounts.remove(account);
        account.getBank().DeleteAccount(account);
    }
}

class Bank {
    private final String name, code;
    private final List<Account> accounts;

    public Bank(String name, String code) {
        this.name = name;
        this.code = code;
        this.accounts = new ArrayList<>();
    }

    public void Deposit(Account account, Float amount) {
        account.setBalance(account.getBalance() + amount);
    }

    public void Withdraw(Account account, Float amount) throws Exception {
        if (account.getBalance() - amount < 0) {
            throw new Exception("Nemáš peníze :(");
        }
        account.setBalance(account.getBalance() - amount);
    }

    public String getCode() {
        return code;
    }

    public void Transfer(Account from, Account to, Float amount) throws Exception {
        if (from.getCurrency() != to.getCurrency())
            throw new Exception("Nemáš stejnou měnu!");
        from.Withdraw(amount);
        to.Deposit(amount);

    }

    private String getAccountNumber() {
        Random random = new Random();
        random.nextDouble();
        long min = 100000000;
        long max = 999999999;
        Long accountNumber;
        do {
            accountNumber = min + Math.round((max - min) * random.nextDouble());
        }
        while (accountExist(accountNumber.toString()));
        return accountNumber.toString();
    }

    boolean accountExist(String accountNumber) {
        for (Account current : this.accounts) {
            if (accountNumber == current.getAccountNumber())
                return true;
        }
        return false;
    }

    public Account CreateAccount(Person owner, Currency currency) {
        Account account = new Account(getAccountNumber(), currency, owner, this);
        accounts.add(account);
        owner.getAccounts().add(account);
        return account;
    }

    public void DeleteAccount(Account account) {
        accounts.remove(account);
        account.getOwner().getAccounts().remove(account);
    }
}

class Account {
    private final String accountNumber, bankCode;
    private final Currency currency;
    private final Person owner;
    private final Bank bank;
    private float balance;

    public Account(String accountNumber, Currency currency, Person owner, Bank bank) {
        this.accountNumber = accountNumber;
        this.bankCode = bank.getCode();
        this.currency = currency;
        this.balance = 0;
        this.owner = owner;
        this.bank = bank;
    }

    public void Deposit(float amount) {
        bank.Deposit(this, amount);
    }

    public void Withdraw(float amount) throws Exception {
        bank.Withdraw(this, amount);
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float value) {
        balance = value;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Bank getBank() {
        return bank;
    }

    public Person getOwner() {
        return owner;
    }

    public Currency getCurrency() {
        return currency;
    }
}
