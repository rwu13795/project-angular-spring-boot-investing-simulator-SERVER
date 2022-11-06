package com.raywu.investingsimulator.portfolio.account;

import com.raywu.investingsimulator.exception.exceptions.BadRequestException;
import com.raywu.investingsimulator.exception.exceptions.InvalidTokenException;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
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
        }
        return null;
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

    @Override
    public void updateAccountFund
            (int id, double currentPrice, TransactionRequest tr, double realizedGainLoss) {
        Account account = findById(id);
        if(account == null) {
            throw new InvalidTokenException("Invalid ID found in the token");
        }

        double orderAmount = currentPrice * tr.getShares();
        switch (TransactionType.valueOf(tr.getType())) {
            case BUY: {
                account.setFund(account.getFund() - orderAmount);
                break;
            }
            case SELL: {
                account.setFund(account.getFund() + orderAmount);
                break;
            }
            case SELL_SHORT: break;

            case BUY_TO_COVER: {
                // since "sell short" does not modify the fund, the realizedGainLoss of "buy to cover"
                // will the exact amount that increase/decrease the fund
                account.setFund(account.getFund() + realizedGainLoss);
                break;
            }
            default: throw new BadRequestException("Invalid transaction type", "transaction");
        }

        save(account);
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
