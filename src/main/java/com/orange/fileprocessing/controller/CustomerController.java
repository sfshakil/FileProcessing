package com.orange.fileprocessing.controller;

import com.orange.fileprocessing.entity.Customer;
import com.orange.fileprocessing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public void saveCustomer() throws IOException {

        System.out.println(new Date(System.currentTimeMillis()));

        // get all the lines from txt file
        List<String> fileLines = Files.lines(Paths.get("C:\\Users\\shakil.reja\\Desktop\\1M.txt")).collect(Collectors.toList());
        System.out.println(fileLines.size() + " - lines in original file.");

        // get without duplicate data
        List<String> listWithoutDuplicates = new ArrayList<>( new HashSet<>(fileLines));
        System.out.println(listWithoutDuplicates.size() + " - lines after removing duplicate.");
        System.out.println(fileLines.size() - listWithoutDuplicates.size() + " - duplicate lines.");

        //create separate txt file for performing different thread
        int j = 1;
        for(int i = 0; i<=listWithoutDuplicates.size(); i = i + (listWithoutDuplicates.size()/5) ){
            System.out.println(i);

            File file = new File("C:\\Users\\shakil.reja\\Desktop\\"+j+".txt");
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            try{
                for(String x :listWithoutDuplicates.subList(i, i+20000)){
                    pw.println(x);
                }
            }catch(Exception ex){
                for(String x :listWithoutDuplicates.subList(i, listWithoutDuplicates.size())){
                    pw.println(x);
                }
            }
            pw.close();
            j++;
        }

        // start all the thread with different created file
        for(int i = 1; i<j; i++){
            new MyThread("C:\\Users\\shakil.reja\\Desktop\\"+i+".txt",customerService);
        }

    }

}
class MyThread implements Runnable {

    private static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}"; // regex for email validation
    String thname;Thread t;CustomerService customerservice;
    MyThread (String name,CustomerService customerService){
        thname = name;
        customerservice = customerService;
        t = new Thread(this);
        t.start();  // starting thread
    }
    public void run() {
        String strLine = "";
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(thname), "UTF-8")); // getting all the lines from text file

            while (((strLine = reader.readLine()) != null)){
                String[] cus = strLine.split(","); // extract all comma separated data from a line
                try{
                    validCustomerSave(cus[5],cus[6],cus[0],cus[1],cus[2],cus[3],cus[4],cus[7]); // check validation and save customer to db
                }catch(Exception ex){

                }
                Thread.sleep(1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Unable to read the file.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void validCustomerSave(String mobNumber, String email, String firstName, String lastName, String city, String state, String zipCode, String ip){

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if( matcher.matches() && ((mobNumber.matches("\\d{10}") ? true:false) || (mobNumber.matches("[+\\.\\s]\\d{11}")?true:false) || (mobNumber.matches("\\(\\d{3}\\)[\\s]\\d{3}-\\d{4}")?true:false))){
            Customer customer = new Customer();
            customer.setFirst_name(firstName);
            customer.setLast_name(lastName);
            customer.setCity(city);
            customer.setState(state);
            customer.setZip_code(zipCode);
            customer.setIp_address(ip);
            customer.setMobile(mobNumber);
            customer.setMail(email);
            customerservice.saveCustomer(customer);
            System.out.println(new Date(System.currentTimeMillis()));
        }
    }
}
