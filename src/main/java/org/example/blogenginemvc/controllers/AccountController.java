package org.example.blogenginemvc.controllers;

import java.util.*;

import org.example.blogenginemvc.models.Account;
import org.example.blogenginemvc.repos.AccountRepo;
import org.example.blogenginemvc.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired AccountRepo accountRepo;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }
    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute Account account) {
        List<String> encoded = hashPassword(account.getPassword());
        account.setPassword(encoded.get(0));
        account.setSalt(encoded.get(1));
        if (accountRepo.findByEmail(account.getEmail()).size() <= 0) {
            accountService.save(account);
        } else {
            return "404";
        }
        return "redirect:/login";
    }

    public static String generateRandomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
       return sb.toString();
    }

    public List<String> hashPassword(String password) {
        String salt = generateRandomString();
        String combinedString = password + salt;
        String hashedPassword = Base64.getEncoder().encodeToString(combinedString.getBytes());
        List<String> hashAndSalt = new ArrayList<>();
        hashAndSalt.add(hashedPassword);
        hashAndSalt.add(salt);
        return hashAndSalt;
    }
    
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Account account, HttpSession session) {
        Optional<Account> acc = accountService.findByEmail(account.getEmail());
        if (acc.isPresent() && verifyPassword(account.getPassword(), acc.get().getPassword(), acc.get().getSalt())) {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("account", acc.get());
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("isLoggedIn", false);
        session.setAttribute("account", null);
        return "redirect:/login";
    }

    public Boolean verifyPassword(String password, String hashedPassword, String salt) {
        String combinedString = password + salt;
        String verifyPassword = Base64.getEncoder().encodeToString(combinedString.getBytes());
        if (verifyPassword.equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/accounts/{id}")
    public String getAccount(@PathVariable Long id, Model model) {
        Optional<Account> optionalAccount = accountService.getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            return "account";
        } else {
            return "404";
        }
    }

    @PostMapping("/accounts/{id}/follow")
    public String startFollowing(@PathVariable Long id, HttpSession session) {
        Optional<Account> followingAccount = accountService.getById(id);
        Optional<Account> currentAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (followingAccount.get() != currentAccount.get()) {
            followingAccount.get().addFollowers(currentAccount.get());
            currentAccount.get().addFollowings(followingAccount.get());
            accountService.save(followingAccount.get());
            accountService.save(currentAccount.get());
        }
        return "redirect:/accounts/" + id;
    }

    @PostMapping("/accounts/{id}/unfollow")
    public String unFollowing(@PathVariable Long id, HttpSession session) {
        Optional<Account> followingAccount = accountService.getById(id);
        Optional<Account> currentAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (followingAccount.get() != currentAccount.get()) {
            followingAccount.get().removeFollowers(currentAccount.get());
            currentAccount.get().removeFollowings(followingAccount.get());
            accountService.save(followingAccount.get());
            accountService.save(currentAccount.get());
        }
        return "redirect:/accounts/" + id;
    }
    
}
