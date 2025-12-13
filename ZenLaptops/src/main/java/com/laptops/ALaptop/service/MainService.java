package com.laptops.ALaptop.service;

import com.laptops.ALaptop.model.Account;
import com.laptops.ALaptop.model.Laptops;
import com.laptops.ALaptop.repository.AccountRepository;
import com.laptops.ALaptop.repository.LaptopRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MainService {
    private final AccountRepository accountRepository;
    private final LaptopRepository laptopRepository;
@Transactional
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
@Transactional
    public void addLaptop(Laptops theLaptop, MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account owner_account = getAccountByUsername(authentication.getName());
        theLaptop.setTheAccount(owner_account);

        String imageName = multipartFile.getOriginalFilename() + UUID.randomUUID();
        File file = new File("src/main/resources/static/image_base/user_" + owner_account.getId());

        if(!file.exists()){
            file.mkdir();
        }
    Path path= Paths.get(file.getPath(),"/",imageName);
    try {
        multipartFile.transferTo(path);
    }catch (IOException e){
        e.printStackTrace();
    }
    theLaptop.setImageLink(imageName);
    laptopRepository.save(theLaptop);

    }
@Transactional
    public void saveAccount(Account theAccount) {
        accountRepository.save(theAccount);
    }
@Transactional
    public List<Laptops> getLaptopList() {
        return laptopRepository.findAll();
    }
    @Transactional
    public List<Laptops> getMyLaptopsList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account owner_account = getAccountByUsername(authentication.getName());
        List<Laptops> laptopList = new ArrayList<>();
        for (Laptops laptops : laptopRepository.findAll()) {
            if (laptops.getTheAccount().equals(owner_account)) {
                laptopList.add(laptops);
            }
        }
return laptopList;
    }
    public Laptops getLaptopById(long id) {
        return laptopRepository.getLaptopById(id);
    }
    public void deleteLaptopById(long id) {
    laptopRepository.deleteById(id);
    }


}
