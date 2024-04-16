package org.example.blogenginemvc.services;

import java.util.List;
import java.util.Optional;

import org.example.blogenginemvc.models.Account;
import org.example.blogenginemvc.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Service
public class AccountService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private EntityManager entityManager;

    public List<Account> getAll() {
        return accountRepo.findAll();
    }

    public Optional<Account> getById(Long id) {
        return accountRepo.findById(id);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepo.findOneByEmail(email);
    }

    public Account save(Account account) {
        return accountRepo.save(account);
    }

    public List<Account> searchAccountsByEmail(String account) {
        String searchAccount = "SELECT a FROM Account a WHERE a.email LIKE :account";
        TypedQuery<Account> typedAccount = entityManager.createQuery(searchAccount, Account.class);
        typedAccount.setParameter("account", "%" + account + "%");
        return typedAccount.getResultList();
    }
}
