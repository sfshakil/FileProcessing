package com.orange.fileprocessing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String first_name;
    private String last_name;
    private String city;
    private String state;
    private String zip_code;
    private String mobile;
    private String mail;
    private String ip_address;
}
