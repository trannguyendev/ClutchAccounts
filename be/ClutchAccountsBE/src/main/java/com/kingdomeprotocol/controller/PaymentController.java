package com.kingdomeprotocol.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.DTODepositModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.service.PaymentService;
import com.kingdomeprotocol.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final TransactionRepository transRepo;
	private final PaymentService payServ;
	private final UserService userServ;
	@PostMapping("/request-deposit")
	public ResponseEntity<?> depositUser(@Valid @RequestBody DTODepositModel depositData){
		try {
			payServ.addTransactionReq(depositData);
			return ResponseEntity.ok(Map.of("message", "Waiting for admin approve"));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not found user"));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	@PostMapping("/approve/{id}")
	public ResponseEntity<?> approveTrans(@PathVariable int id){
		try {
			payServ.approveTransaction(id);
			return ResponseEntity.ok(Map.of("message", "Approved transaction with id: "+id));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not found transaction"));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	@PostMapping("/reject/{id}")
	public ResponseEntity<?> rejectTrans(@PathVariable("id") int id){
		try {
			payServ.rejectTransaction(id);
			return ResponseEntity.ok(Map.of("message", "Rejected transaction with id: "+id));
		}
		catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not found transaction"));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("/transaction-log/{username}")
	public ResponseEntity<?> getLogTransaction(@PathVariable("username") String email){
		try {
			UserModel user = userServ.loadUserByEmail(email).orElseThrow(() -> new RuntimeException("Not found user"));
			int user_id = user.getId();
			return ResponseEntity.ok(transRepo.getSelfPaymentLog(user_id));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
}
