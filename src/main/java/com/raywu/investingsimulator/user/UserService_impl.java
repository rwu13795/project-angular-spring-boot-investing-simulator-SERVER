package com.raywu.investingsimulator.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService_impl implements UserService {
    private UserRepository userRepository;

    public UserService_impl () {}

    // Spring data JPA will use the "EmployeeRepository" interface to create the
    // "employeeRepository" object for us
    @Autowired
    public UserService_impl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
//	@Transactional    // JPA-data includes the @Transactional, we don't need to add it anymore
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        if(result.isPresent()) {
            return result.get();
        } else {
            // could not find the employee with such id
            throw new RuntimeException("Could not find employee with id - " + id);
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }


    // test the customer query in the employeeRepository
    @Override
    public User findByLastName(String lastName) {
        Optional<User> result = userRepository.findByLastName(lastName);

        if(result.isPresent()) {
            return result.get();
        } else {
            // could not find the employee with such id
            throw new RuntimeException("Could not find employee with last name - " + lastName);
        }
    }

    @Override
    public List<User> getUsersByPage(int pageNum) {
        // limit = items_per_page
        // offset = (pageNum - 1) * Limit
        int limit = 2;
        int offset = (pageNum - 1) * limit;

        return userRepository.getUsersByPage(limit, offset);
    }
}
