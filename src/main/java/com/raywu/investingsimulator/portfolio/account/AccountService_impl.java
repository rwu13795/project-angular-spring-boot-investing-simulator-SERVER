package com.raywu.investingsimulator.portfolio.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService_impl implements AccountService {
    @Autowired
    private AccountRepository userRepository;

    @Override
//	@Transactional    // JPA-data includes the @Transactional, we don't need to add it anymore
    public List<Account> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Account findById(int id) {
        Optional<Account> result = userRepository.findById(id);

        if(result.isPresent()) {
            return result.get();
        } else {
            // could not find the employee with such id
            throw new RuntimeException("Could not find employee with id - " + id);
        }
    }

    @Override
    public Account findByEmail(String email) {

        Optional<Account> account = userRepository.findByEmail(email);
        if(account.isPresent()) {
            return account.get();
        }
        return null;
    }

    @Override
    public void save(Account user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }



    // test the customer query in the employeeRepository
//    @Override
//    public Account findByLastName(String lastName) {
//        Optional<Account> result = userRepository.findByLastName(lastName);
//
//        if(result.isPresent()) {
//            return result.get();
//        } else {
//            // could not find the employee with such id
//            throw new RuntimeException("Could not find employee with last name - " + lastName);
//        }
//    }
//
//    @Override
//    public List<Account> getUsersByPage(int pageNum) {
//        // limit = items_per_page
//        // offset = (pageNum - 1) * Limit
//        int limit = 2;
//        int offset = (pageNum - 1) * limit;
//
//        return userRepository.getUsersByPage(limit, offset);
//    }
}
