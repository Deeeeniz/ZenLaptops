package com.laptops.ALaptop.model;

import lombok.Data;
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "account_table")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Empty input field")
    @Size(min = 8, max = 20 , message="Input must contain min 8 max 20 characters")
    private String username;
    @NotEmpty(message = "Empty password field")
    private String password;
    @NotEmpty(message = "Empty full name field")
    private String full_name;
    @NotEmpty(message = "Empty email field")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" , message = "must match example@gmail.com")
    private String email;
    @NotEmpty(message = "Empty phone number field")
    @Pattern(regexp = "^(0[0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])" , message = "must match 0**-***-**-**")
    private String phone_number;
    @OneToMany(mappedBy = "theAccount")
    private List<Laptops> laptopsList;


}

