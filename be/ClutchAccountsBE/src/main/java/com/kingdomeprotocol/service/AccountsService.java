package com.kingdomeprotocol.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingdomeprotocol.controller.AccountsController.DTOnewAcc;
import com.kingdomeprotocol.model.AccForSellModel;
import com.kingdomeprotocol.model.AccountImageModel;
import com.kingdomeprotocol.model.BoughtLogModel;
import com.kingdomeprotocol.model.SubInfoModel;
import com.kingdomeprotocol.model.TransactionsModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.model.VoucherusageModel;
import com.kingdomeprotocol.repository.AccForSellRepository;
import com.kingdomeprotocol.repository.AccountImageRepository;
import com.kingdomeprotocol.repository.BoughtLogRepository;
import com.kingdomeprotocol.repository.SubInfoRepository;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.repository.UserRepository;
import com.kingdomeprotocol.repository.VoucherusageRepository;
import com.kingdomeprotocol.service.VoucherService.virtualTotal;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountsService {
	private final UserRepository userRepo;
	private final TransactionRepository transRepo;
	private final AccForSellRepository accRepo;
	private final BoughtLogRepository boughtRepo;
	private final AccountImageRepository imgRepo;
	private final SubInfoRepository subRepo;
	private final VoucherusageRepository usageRepo;
	private final VoucherService voucherServ;
@Transactional
public BuyRes buyAcc(String email, int idAcc, String voucherCode, Authentication authen) {
	
	UserModel user = userRepo.findUser4Upd(email).orElseThrow(() -> new RuntimeException("Not found user"));
	AccForSellModel acc = accRepo.findAccbyId4Lock(idAcc).orElseThrow(() -> new RuntimeException("Account is not existed"));
	
	if (acc.isSold()) {
		throw new RuntimeException("This account has been sold out");
	}
	if (voucherCode == null || voucherCode.isEmpty()) {
		//Process when no voucher
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
		tx.setType("BUY_ACC_"+acc.getAccount_type());
		tx.setDescrp("Buy account type: "+acc.getAccount_type());
		tx.setStatus("APPROVED");
		
		transRepo.save(tx);
	}
	else {
		Vouchers voucher = voucherServ.validateVoucher(voucherCode, authen);
		virtualTotal data = new virtualTotal(acc.getPrice(), voucherCode);
		int discountAmount = voucherServ.caculateVirtualTotal(data, authen);
		int priceAfterDiscount = acc.getPrice() - discountAmount;
		if (user.getBalance() < priceAfterDiscount) {
			throw new RuntimeException("Insufficient balance, pls charge");
		}
		
		user.setBalance(user.getBalance() - priceAfterDiscount);
		userRepo.save(user);
		
		VoucherusageModel usage = new VoucherusageModel();
		usage.setUser(user);
		usage.setVoucher(voucher);
		usageRepo.save(usage);
		
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
		tx.setAmount(-priceAfterDiscount);
		tx.setType("BUY_ACC_"+acc.getAccount_type());
		tx.setDescrp("Buy account type: "+acc.getAccount_type());
		tx.setStatus("APPROVED");
		
		transRepo.save(tx);
	}
	return new BuyRes(acc.getUsername(), acc.getEmail(), acc.getAccount_psw());
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
	tx.setType("BUY_ACC_"+acc.getAccount_type());
	tx.setDescrp("Buy account type: "+acc.getAccount_type());
	tx.setStatus("APPROVED");
	
	transRepo.save(tx);
	return new BuyRes(acc.getUsername(), acc.getEmail(), acc.getAccount_psw());
}

//Get 5 acc for random spin:)
public List<?> getList5RandomFA(){
	List<?> accRandomFA = new ArrayList<>();
	accRandomFA = accRepo.get5InfoAccFA("RANDOM_FA");
	if (accRandomFA.isEmpty()) {
		throw new RuntimeException("This kind of account has been sold out");
	}
	return accRandomFA;
}

//Create new account for sell (admin role)
public void createAccForSell(DTOnewAcc dto) {
	
	AccForSellModel account = new AccForSellModel();
    account.setEmail(dto.email());
    account.setUsername(dto.username());
    account.setAccount_psw(dto.password());
    account.setPrice(dto.price());  
    account.setListed_at(dto.listed_at() != null ? dto.listed_at() : LocalDateTime.now());
    account.setSold(dto.isSold() != null ? dto.isSold() : false);
    account.setLocked(dto.isLocked() != null ? dto.isLocked() : false);
    account.setLockedUntil(dto.lockedUntil());
    account.setAccount_type(dto.account_type());
    
    accRepo.save(account);
    
    if (dto.image_url() != null && !dto.image_url().isBlank()) {
    	AccountImageModel img = new AccountImageModel();
    	img.setImage_url(dto.image_url());
    	img.setAccId(account);
    	
    	imgRepo.save(img);
    }

	SubInfoModel subInfoData = new SubInfoModel();
	subInfoData.setAccSubInfo(account);
	subInfoData.setRank_info(dto.rank_info());
	subInfoData.setVp(dto.vp());
	subInfoData.setMelee_amount(dto.melee_amount());
	subInfoData.setGun_amount(dto.gun_amount());
	subInfoData.setBtp(dto.btp());
    
	subRepo.save(subInfoData);
}
public record BuyRes(String username, String email, String password) {}
}
