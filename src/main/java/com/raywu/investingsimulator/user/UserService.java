package com.raywu.investingsimulator.user;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int id);

    void save(User user);

    void deleteById(int id);

    User findByLastName(String lastName);

    List<User> getUsersByPage(int pageNum);
}
