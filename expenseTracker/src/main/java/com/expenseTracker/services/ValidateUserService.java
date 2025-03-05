package com.expenseTracker.services;


import com.expenseTracker.DTO.UserDetailDTO;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateUserService {

    public Boolean validateUser(UserDetailDTO userDetailDTO){
        String email=userDetailDTO.getEmail();
        String pass=userDetailDTO.getPassword();

        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        String strongPasswordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Pattern patternPass=Pattern.compile(strongPasswordRegex);
        Matcher matcher = pattern.matcher(email);
        Matcher matcherPass=patternPass.matcher(pass);
        if (matcher.matches() && matcherPass.matches() ){
            return true;
        }else{
            System.out.println("Email Validated: "+matcher.matches());
            System.out.println("Password Validated: "+matcherPass.matches());
            return false;
        }
    }
}
