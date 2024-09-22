package com.expenseTracker.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDetailDTO {

    private String userName;

     private String password;

    private Long phoneNumber;

    private String email;


}
