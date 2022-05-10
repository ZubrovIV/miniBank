package com.example.superbank;

import com.example.superbank.model.TransferBalance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BankService {

    private final BalanceRepository repository;

    public BigDecimal getClass(Long accountId) {
        BigDecimal balance = repository.getBalanceFromID(accountId);
        if (balance==null){
            throw new IllegalArgumentException();
        }
        return balance;
    }

    public BigDecimal addMoney(Long to, BigDecimal amount) {
        BigDecimal currentBalance = repository.getBalanceFromID(to);
        if (currentBalance==null){
            repository.save(to,amount);
            return amount;
        }else {
            BigDecimal updateBalance = currentBalance.add(amount);
            repository.save(to,updateBalance);
            return updateBalance;
        }
    }

    public void makeTransfer(TransferBalance transferBalance) {
      BigDecimal fromBalance =   repository.getBalanceFromID(transferBalance.getFrom());
      BigDecimal toBalance =   repository.getBalanceFromID(transferBalance.getTo());
      if (fromBalance==null||toBalance==null) throw new IllegalArgumentException("no ids");
      if(transferBalance.getAmount().compareTo(fromBalance)>0) throw new IllegalArgumentException("no money");

      BigDecimal updatedFromBalance = fromBalance.subtract(transferBalance.getAmount());
      BigDecimal updatedToBalance = toBalance.add(transferBalance.getAmount());
      repository.save(transferBalance.getFrom(),updatedFromBalance);
      repository.save(transferBalance.getTo(),updatedToBalance);


      }

    }

