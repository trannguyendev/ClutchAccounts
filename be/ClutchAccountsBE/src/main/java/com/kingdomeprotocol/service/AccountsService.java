package com.kingdomeprotocol.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingdomeprotocol.model.AccForSellModel;
import com.kingdomeprotocol.model.BoughtLogModel;
import com.kingdomeprotocol.model.TransactionsModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.AccForSellRepository;
import com.kingdomeprotocol.repository.BoughtLogRepository;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.repository.UserRepository;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountsService {
	private final UserRepository userRepo;
	private final TransactionRepository transRepo;
	private final AccForSellRepository accRepo;
	private final BoughtLogRepository boughtRepo;
	
@Transactional
public BuyRes buyAcc(String email, int idAcc) {
	
	UserModel user = userRepo.findUser4Upd(email).orElseThrow(() -> new RuntimeException("Not found user"));
	AccForSellModel acc = accRepo.findAccbyId4Lock(idAcc).orElseThrow(() -> new RuntimeException("Account is not existed"));
	
	if (acc.isSold()) {
		throw new RuntimeException("This account has been sold out");
	}
	if (user.getBalance() < acc.getPrice()) {
		throw new RuntimeException("Insufficient balance, pls charge");
	}
	
	user.setBalance(user.getBalance() - acc.getPrice());
	userRepo.save(user);
	
	//Create bought log
	BoughtLogModel boughtLog = new BoughtLogModel();
	boughtLog.setUser(user);
	boughtLog.setAccSold(acc);
	boughtRepo.save(boughtLog);
	
	acc.setBoughtLog(boughtLog);
	acc.setSold(true);
	accRepo.save(acc);
	
	TransactionsModel tx = new TransactionsModel();
	tx.setTransactionUser(user);
	tx.setAmount(-acc.getPrice());
	tx.setType("BUY_ACC_+"+acc.getAccount_type());
	tx.setDescrp("Buy account type: "+acc.getAccount_type());
	tx.setStatus("APPROVED");
	
	transRepo.save(tx);
	
	return new BuyRes(acc.getUsername(), acc.getAccount_psw(), "Purchased successfully");
}

@Transactional
public BuyRes buyRandomAcc(String email, String type) {
	UserModel user = userRepo.findUser4Upd(email).orElseThrow(() -> new RuntimeException("Not found user"));
	
	int totalAccRandomLeft = accRepo.countAccLeft(type);
	int skipRecord = ThreadLocalRandom.current().nextInt(0, totalAccRandomLeft - 1);
	
	if (totalAccRandomLeft == 0) {
		throw new RuntimeException("Not found any account matched with type: "+type);
	}
	
	AccForSellModel acc = accRepo.randomAccByType(type, skipRecord).orElseThrow(() -> new RuntimeException("An error occured while fetching data"));
	acc = accRepo.findAccbyId4Lock(acc.getAccount_id()).orElseThrow(() -> new RuntimeException("Account is not existed"));
	
	if (acc.isSold()) {
		throw new RuntimeException("Sorry, this account has been purchased XD");
	}
	 
	if (user.getBalance() < acc.getPrice()) {
		throw new RuntimeException("Insufficient balance, pls charge");
	}
	
	user.setBalance(user.getBalance() - acc.getPrice());
	userRepo.save(user);
	
	BoughtLogModel boughtLog = new BoughtLogModel();
	boughtLog.setUser(user);
	boughtLog.setAccSold(acc);
	boughtRepo.save(boughtLog);
	
	acc.setBoughtLog(boughtLog);
	acc.setSold(true);
	accRepo.save(acc);
	
	TransactionsModel tx = new TransactionsModel();
	tx.setTransactionUser(user);
	tx.setAmount(-acc.getPrice());
	tx.setType("BUY_ACC_+"+acc.getAccount_type());
	tx.setDescrp("Buy account type: "+acc.getAccount_type());
	tx.setStatus("APPROVED");
	
	transRepo.save(tx);
	return new BuyRes(acc.getUsername(), acc.getAccount_psw(), "Purchased successfully");
}
public record BuyRes(String username, String email, String password) {}
}
