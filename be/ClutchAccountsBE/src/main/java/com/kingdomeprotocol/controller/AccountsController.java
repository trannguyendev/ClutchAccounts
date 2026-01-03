package com.kingdomeprotocol.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.DTOBuyRequest;
import com.kingdomeprotocol.repository.AccForSellRepository;
import com.kingdomeprotocol.repository.BoughtLogRepository;
import com.kingdomeprotocol.service.AccountsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountsController {
	private final AccountsService accServ;
	private final AccForSellRepository accRepo;
	private final BoughtLogRepository boughtRepo;
	@PostMapping("/buy")
	public ResponseEntity<?> buyAcc(@RequestBody DTOBuyRequest dtoReq){
		try {
			return ResponseEntity.ok(accServ.buyAcc(dtoReq.getEmail(), dtoReq.getAccId()));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	
	@PostMapping("/random/acc-fa")
	public ResponseEntity<?> randomFA(@RequestBody DTOBuyRequest dtoBuyReq){
		try {
			return ResponseEntity.ok(accServ.buyRandomAcc(dtoBuyReq.getEmail(), "RANDOM_FA"));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/random/acc-nfa")
	public ResponseEntity<?> randomNFA(@RequestBody DTOBuyRequest dtoBuyReq){
		try {
			return ResponseEntity.ok(accServ.buyRandomAcc(dtoBuyReq.getEmail(), "RANDOM_NFA"));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@GetMapping("/random/acc-fa/list-acc")
	public ResponseEntity<?> getInfoAccRandFA(){
		try {
			return ResponseEntity.ok(accRepo.getInfoAcc("RANDOM_FA"));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@GetMapping("/random/acc-nfa/list-acc")
	public ResponseEntity<?> getInfoAccRandNFA(){
		try {
			return ResponseEntity.ok(accRepo.getInfoAcc("RANDOM_NFA"));
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
	@GetMapping("/bought-log/me")
	public ResponseEntity<?> displaySelfBoughtLog(Authentication authen){
		try {
			return ResponseEntity.ok(boughtRepo.getSelfBoughtLogs(authen.getName()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
}
