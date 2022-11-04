package com.raywu.investingsimulator.portfolio.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, AssetPK> {


// create a custom findByEmail method using native SQL
    @Query(value = "SELECT * FROM asset p WHERE p.user_id = ?1",
            nativeQuery = true)
    List<Asset> findAllAssets(int user_id);
        // NOTE - the last name is case-sensitive

        // The JpaRepository can automatically create some simple CRUD query for ue
        // such as findByColumnName(column data)
//        Optional<Account> findByEmail(String email);
//
//
//
//// create a custom getEmployeesByPage method using native SQL
//@Query(value = "SELECT * FROM EMPLOYEE LIMIT ?1 OFFSET ?2",
//        nativeQuery = true)
//    List<Account> getUsersByPage(int limit, int offset);
//        // Limit = items_per_page
//        // OFFSET = (pageNum - 1) * Limit
//

}