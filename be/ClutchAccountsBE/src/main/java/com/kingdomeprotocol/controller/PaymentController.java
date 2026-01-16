package com.kingdomeprotocol.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kingdomeprotocol.model.DTODepositModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.service.PayOsService;
import com.kingdomeprotocol.service.PaymentService;
import com.kingdomeprotocol.service.UserService;
import com.kingdomeprotocol.utils.PayOsConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.webhooks.ConfirmWebhookResponse;
import vn.payos.model.webhooks.WebhookData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PayOsService payOsService;
	private final TransactionRepository transRepo;
	private final PaymentService payServ;
	private final UserService userServ;
	private final PayOS payOs;
//	@PostMapping("/request-deposit")
//	public ResponseEntity<?> depositUser(@Valid @RequestBody DTODepositModel depositData){
//		try {
//			payServ.addTransactionReq(depositData);
//			return ResponseEntity.ok(Map.of("message", "Waiting for admin approve"));
//		}
//		catch(RuntimeException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not found user"));
//		}
//		catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
//		}
//	}
	@PostMapping("/request-deposit")
	public ResponseEntity<?> createPayment(@Valid @RequestBody DTODepositModel depositData){
		try {
			return ResponseEntity.ok(payOsService.createQR(depositData.getEmail(), depositData.getAmount(), depositData.getDescrp()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@PutMapping("/cancel/{orderCode}")
	public ResponseEntity<?> cancelPayment(@PathVariable("orderCode") long orderCode){
		try {
			PaymentLink order = payOs.paymentRequests().cancel(orderCode, "User cancel payment");
			return ResponseEntity.ok(order);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/callback")
	public ResponseEntity<?> callBackFunc (@RequestBody Object body) throws JsonProcessingException, IllegalArgumentException{
		try {
			WebhookData webhookRes = (WebhookData) payOs.webhooks().verify(body);
			return ResponseEntity.ok(webhookRes);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/create/success-log")
	public void createSuccess(@RequestBody DTODepositModel data) {
		payServ.addSuccessTransaction(data);
	}
	@PostMapping("/create/cancelled-log")
	public void createCancelled(@RequestBody DTODepositModel data) {
		payServ.addCancelledTransaction(data);
	}
	@PostMapping("/approve/{id}")
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
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
	
	@GetMapping("/transaction-log")
	public ResponseEntity<?> getLogTransaction(Authentication authen){
		try {
			UserModel user = userServ.loadUserByEmail(authen.getName()).orElseThrow(() -> new RuntimeException("Not found user"));
			int user_id = user.getId();
			return ResponseEntity.ok(transRepo.getSelfPaymentLog(user_id));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("/transaction-pending")
	public ResponseEntity<?> getPendingTransaction(){
		try {
			return ResponseEntity.ok(transRepo.getPendingTransaction());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("/transactions-all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllTransaction(){
		try {
			return ResponseEntity.ok(transRepo.getAllTransaction());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
}
