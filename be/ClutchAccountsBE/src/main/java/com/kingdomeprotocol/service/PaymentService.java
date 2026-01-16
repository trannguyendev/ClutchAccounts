package com.kingdomeprotocol.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kingdomeprotocol.model.DTODepositModel;
import com.kingdomeprotocol.model.TransactionsModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final TransactionRepository transRepo;
	private final UserService userServ;
	private final UserRepository userRepo;
	
	public void addTransactionReq(DTODepositModel depositData) {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
		UserModel user = userServ.loadUserByEmail(depositData.getEmail()).orElseThrow(() -> new RuntimeException("Not found user"));
		TransactionsModel transaction = new TransactionsModel();
		transaction.setTransactionUser(user);
		transaction.setAmount(depositData.getAmount());
		transaction.setType("DEPOSIT");
		transaction.setDescrp(depositData.getDescrp());
		transaction.setCreated_at(currentLocalDateTime);
		transaction.setStatus("PENDING");
		transRepo.save(transaction);
	}
	public void addCancelledTransaction(DTODepositModel depositData) {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
		UserModel user = userServ.loadUserByEmail(depositData.getEmail()).orElseThrow(() -> new RuntimeException("Not found user"));
		TransactionsModel transaction = new TransactionsModel();
		transaction.setTransactionUser(user);
		transaction.setAmount(depositData.getAmount());
		transaction.setType("DEPOSIT");
		transaction.setDescrp(depositData.getDescrp());
		transaction.setCreated_at(currentLocalDateTime);
		transaction.setStatus("CANCELLED");
		transRepo.save(transaction);
	}
	public void addSuccessTransaction(DTODepositModel depositData) {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
		UserModel user = userServ.loadUserByEmail(depositData.getEmail()).orElseThrow(() -> new RuntimeException("Not found user"));
		TransactionsModel transaction = new TransactionsModel();
		transaction.setTransactionUser(user);
		transaction.setAmount(depositData.getAmount());
		transaction.setType("DEPOSIT");
		transaction.setDescrp(depositData.getDescrp());
		transaction.setCreated_at(currentLocalDateTime);
		transaction.setStatus("APPROVED");
		transRepo.save(transaction);
	}
	public void approveTransaction(int transaction_id) {
		TransactionsModel transForApprove = transRepo.findById(transaction_id).orElseThrow(() -> new RuntimeException("Not found transaction"));
		transForApprove.setStatus("APPROVED");
		UserModel user = transForApprove.getTransactionUser();
		user.setBalance(user.getBalance() + transForApprove.getAmount());
		transRepo.save(transForApprove);
		userRepo.save(user);
	}
	
	public void rejectTransaction(int transaction_id) {
		TransactionsModel transForApprove = transRepo.findById(transaction_id).orElseThrow(() -> new RuntimeException("Not found transaction"));
		transForApprove.setStatus("FAILED");
		transRepo.save(transForApprove);
	}
	
}
