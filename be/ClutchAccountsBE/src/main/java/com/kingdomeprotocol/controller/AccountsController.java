package com.kingdomeprotocol.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.AccForSellModel;
import com.kingdomeprotocol.model.AccountImageModel;
import com.kingdomeprotocol.model.DTOBuyRequest;
import com.kingdomeprotocol.model.SubInfoModel;
import com.kingdomeprotocol.repository.AccForSellRepository;
import com.kingdomeprotocol.repository.AccountImageRepository;
import com.kingdomeprotocol.repository.BoughtLogRepository;
import com.kingdomeprotocol.repository.SubInfoRepository;
import com.kingdomeprotocol.service.AccountsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountsController {
	private final AccountsService accServ;
	private final AccForSellRepository accRepo;
	private final BoughtLogRepository boughtRepo;
	private final AccountImageRepository imgRepo;
	private final SubInfoRepository subRepo;
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
			return ResponseEntity.ok(accServ.getList5RandomFA());
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
	@GetMapping("/list-acc")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> displayAccInfoAdmin(){
		try {
			return ResponseEntity.ok(accRepo.getAllCurrentAcc());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
//	//Update vlr accs
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateVlrAcc(@PathVariable int id,@RequestBody DTOupdateAcc data){
		try {
			return accRepo.findById(id).map(acc ->{
				if (data.email() != null) acc.setEmail(data.email());
				if (data.username() != null) acc.setUsername(data.username());
				if (data.password() != null) acc.setAccount_psw(data.password());
				
				if (data.price() != null && data.price()> 0) acc.setPrice(data.price());
				if (data.isSold() != null) acc.setSold(data.isSold());
				if (data.isLocked() != null) acc.setLocked(data.isLocked());
				if (data.lockedUntil() != null) acc.setLockedUntil(data.lockedUntil());
				if (data.account_type() != null) acc.setAccount_type(data.account_type());
				
				return ResponseEntity.ok(accRepo.save(acc));
			}).orElseThrow(() -> new RuntimeException("An error occurred while update"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
//	
//	//Delete vlr acc
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteAcc(@PathVariable int id){
		try {
			if(!accRepo.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Not found"));
			}
			accRepo.deleteById(id);
			return ResponseEntity.ok(Map.of("message","delete done"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	
	//Create new 
	@PostMapping("/new")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createNew(@RequestBody DTOnewAcc dto){
		try {
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
			return ResponseEntity.ok(Map.of("message","create done"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
		}
	}
	public record DTOnewAcc(
			String email,
			String username,
			String password,
			Integer price,
			LocalDateTime listed_at,
			Boolean isSold,
			Boolean isLocked,
			LocalDateTime lockedUntil,
			String account_type,
			String image_url, 
			String rank_info,
			int vp,
			int melee_amount,
			int gun_amount,
			int btp
			) {}
	public record DTOupdateAcc(
	        String email,
	        String username,
	        String password,
	        Integer price,
	        LocalDateTime listed_at,
	        Boolean isSold,
	        Boolean isLocked,
	        LocalDateTime lockedUntil,
	        String account_type
	    ) {}
	
}
