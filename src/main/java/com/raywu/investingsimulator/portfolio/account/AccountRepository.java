package com.raywu.investingsimulator.portfolio.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
// JpaRepository will implement all the basic CRUD for you, all you need to do is
// plug in the entity class (User) and the primary id type (Integer)
// watch Spring tutorial #541

// You can also create custom method by using native SQL or JPQL
// read https://www.baeldung.com/spring-data-jpa-query
// JpaRepository will automatically build the method for us by using the query we supply

// create a custom findByEmail method using native SQL
    @Query(value = "SELECT * FROM EMPLOYEE u WHERE u.last_name = ?1",
        nativeQuery = true)
    Optional<Account> findByLastName(String lastName);
        // NOTE - the last name is case-sensitive

    // The JpaRepository can automatically create some simple CRUD query for us
    // such as findByColumnName(column data)
    Optional<Account> findByEmail(String email);

    // create a custom getEmployeesByPage method using native SQL
    @Query(value = "SELECT * FROM EMPLOYEE LIMIT ?1 OFFSET ?2",
            nativeQuery = true)
    List<Account> getUsersByPage(int limit, int offset);
    // Limit = items_per_page
    // OFFSET = (pageNum - 1) * Limit

}
