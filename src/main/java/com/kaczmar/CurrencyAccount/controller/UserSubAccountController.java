package com.kaczmar.CurrencyAccount.controller;

import com.kaczmar.CurrencyAccount.dto.CreateUserSubAccountDto;
import com.kaczmar.CurrencyAccount.exceptions.AccountWithRemainingCurrencyNotExists;
import com.kaczmar.CurrencyAccount.model.UserAccount;
import com.kaczmar.CurrencyAccount.model.UserSubAccount;
import com.kaczmar.CurrencyAccount.model.UserSubAccountOutput;
import com.kaczmar.CurrencyAccount.repository.UserSubAccountRepository;
import com.kaczmar.CurrencyAccount.service.UserSubAccountService;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account/subaccount")
public class UserSubAccountController {

    private UserSubAccountService userSubAccountService;

    public UserSubAccountController(UserSubAccountService userSubAccountService) {
        this.userSubAccountService = userSubAccountService;
    }

    @PostMapping()
    public ResponseEntity<UserSubAccountOutput> createUser(@RequestBody CreateUserSubAccountDto userSubAccountDto){
        UserSubAccount userSubAccount = userSubAccountService.createUserSubAccount(userSubAccountDto);
        return new ResponseEntity<>((userSubAccount.convertFromUserSubAccountToOutput()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserSubAccountOutput>> getAllSubAccountsByPesel(@RequestParam String pesel){
        List<UserSubAccountOutput> allSubAccountsByPesel = userSubAccountService.getAllSubAccountsByPesel(pesel).stream()
                .map(e->e.convertFromUserSubAccountToOutput())
                .collect(Collectors.toList());
        return new ResponseEntity<>(allSubAccountsByPesel, HttpStatus.FOUND);
    }

    @GetMapping("/PLN/USD")
    public ResponseEntity<List<UserSubAccountOutput>> convertFromPlnToUsd(@RequestParam String pesel) throws IOException, AccountWithRemainingCurrencyNotExists, JSONException {
        List<UserSubAccountOutput> userSubAccounts = userSubAccountService.convertPlnToUsd(pesel).stream()
                .map(e->e.convertFromUserSubAccountToOutput())
                .collect(Collectors.toList());
        return new ResponseEntity<>(userSubAccounts,HttpStatus.OK);
    }

}
