package com.expenseTracker.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {


    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
