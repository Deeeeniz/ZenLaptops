package com.laptops.ALaptop.service;

import com.laptops.ALaptop.model.Account;
import com.laptops.ALaptop.model.AccountUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.laptops.ALaptop.security.Role.GUEST;

@Service
@AllArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {
    private final MainService mainService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getAccountDetails(username);
    }
    private AccountUserDetails getAccountDetails(String username){
        Account theAccount = mainService.getAccountByUsername(username);

        AccountUserDetails accountUserDetails = new AccountUserDetails(
            theAccount.getUsername(),
            passwordEncoder.encode(theAccount.getPassword()),
            true,
            true,
            true,
            true,
                GUEST.getSimpleGrantedAuthority());
        return accountUserDetails;

    }
}
