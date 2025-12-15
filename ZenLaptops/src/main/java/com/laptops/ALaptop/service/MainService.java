package com.laptops.ALaptop.service;

import com.laptops.ALaptop.model.Account;
import com.laptops.ALaptop.model.Laptops;
import com.laptops.ALaptop.repository.AccountRepository;
import com.laptops.ALaptop.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Value;
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
public class MainService {

    private final AccountRepository accountRepository;
    private final LaptopRepository laptopRepository;
    private final String imageBasePath;

    public MainService(AccountRepository accountRepository,
                       LaptopRepository laptopRepository,
                       @Value("${image.base.path:src/main/resources/static/image_base}") String imageBasePath) {
        this.accountRepository = accountRepository;
        this.laptopRepository = laptopRepository;
        this.imageBasePath = imageBasePath;
    }

    @Transactional
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Transactional
    public void addLaptop(Laptops theLaptop, MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account ownerAccount = getAccountByUsername(authentication.getName());
        theLaptop.setTheAccount(ownerAccount);

        String imageName = multipartFile.getOriginalFilename() + "_" + UUID.randomUUID();

        File userFolder = new File(imageBasePath + "/user_" + ownerAccount.getId());
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }

        Path path = Paths.get(userFolder.getPath(), imageName);
        try {
            multipartFile.transferTo(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file: " + imageName, e);
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
        Account ownerAccount = getAccountByUsername(authentication.getName());
        List<Laptops> laptopList = new ArrayList<>();
        for (Laptops laptop : laptopRepository.findAll()) {
            if (laptop.getTheAccount().equals(ownerAccount)) {
                laptopList.add(laptop);
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
