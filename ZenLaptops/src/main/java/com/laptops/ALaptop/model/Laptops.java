package com.laptops.ALaptop.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "laptop_table")
@Data
public class Laptops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String model;
    private String price;
    private String details;
    private String cond;
    private String imageLink;
    private String RAM ;
    private String CPU;
    private String memory;
    private String memory_type;
    private String OS;
    private String GPU;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER )
    @JoinColumn(name = "account_id")
    private Account theAccount;


}
