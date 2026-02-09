package com.kingdomeprotocol.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kingdomeprotocol.model.AccForSellModel;
import com.kingdomeprotocol.model.AccountImageModel;
import com.kingdomeprotocol.model.DTOBuyRequest;
import com.kingdomeprotocol.model.SubInfoModel;
import com.kingdomeprotocol.repository.AccForSellRepository;
import com.kingdomeprotocol.repository.AccForSellRepository.customExportInfoAccAdmin;
import com.kingdomeprotocol.repository.AccountImageRepository;
import com.kingdomeprotocol.repository.BoughtLogRepository;
import com.kingdomeprotocol.repository.SubInfoRepository;
import com.kingdomeprotocol.service.AccountsService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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
			return ResponseEntity.ok(accServ.buyAcc(dtoReq.getEmail(), dtoReq.getAccId(), dtoReq.getVoucherCode()));
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
			accServ.createAccForSell(dto);
			return ResponseEntity.ok(Map.of("message","create done"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
		}
	}
	
	//Import/Export to csv file
	@PostMapping("/import")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> importCSV(@RequestParam("file") MultipartFile file){
		try {
			if (file.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","File is empty"));
			}
			CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
			String header[] = reader.readNext();
			List<String[]> rows = reader.readAll();
			
			int successCount = 0;
			for (String[] row : rows) {
				DTOnewAcc newAcc = new DTOnewAcc(row[0], 
						row[1], 
						row[2], 
						Integer.parseInt(row[3]), 
						row[4].isBlank() ? null : LocalDateTime.parse(row[4]), 
						row[5].isBlank() ? null : Boolean.parseBoolean(row[5]), 
						row[6].isBlank() ? null : Boolean.parseBoolean(row[6]), 
						row[7].isBlank() ? null : LocalDateTime.parse(row[7]), 
						row[8], 
						row[9], 
						row[10], 
						Integer.parseInt(row[11]),
						Integer.parseInt(row[12]),
						Integer.parseInt(row[13]),
						Integer.parseInt(row[14]));
				accServ.createAccForSell(newAcc);
				successCount++;
				
			}
			return ResponseEntity.ok("Import success "+successCount+" accounts");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
		}
	}
	
	@GetMapping("/export")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> exportCSV(){
		try {
			List<customExportInfoAccAdmin> lstExport = accRepo.customExportAccForAdmin();
			//create csv in-memory
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos));
			
			writer.writeNext(new String[]{
					"accountId",
					"email",
					"username",
					"password",
					"listedAt",
					"isSold",
					"isLocked",
					"lockedUntil",
					"price",
					"accountType",
					"imageUrl",
					"rankInfo",
					"vp",
					"meleeAmount",
					"gunAmount",
					"btp"
		        });
			for (customExportInfoAccAdmin customExportInfoAccAdmin : lstExport) {
				writer.writeNext(new String[] {
						String.valueOf(customExportInfoAccAdmin.getAccount_id()),
						customExportInfoAccAdmin.getEmail(),
						customExportInfoAccAdmin.getUsername(),
						customExportInfoAccAdmin.getPassword(),
						customExportInfoAccAdmin.getListed_at() != null ?  String.valueOf(customExportInfoAccAdmin.getListed_at()) : "",
						String.valueOf(customExportInfoAccAdmin.getIsSold()),
						String.valueOf(customExportInfoAccAdmin.getIsLocked()),
						customExportInfoAccAdmin.getLockedUntil() != null ? String.valueOf(customExportInfoAccAdmin.getLockedUntil()) : "",
						String.valueOf(customExportInfoAccAdmin.getPrice()),
						customExportInfoAccAdmin.getAccount_type(),
						String.valueOf(customExportInfoAccAdmin.getImage_url()),
						customExportInfoAccAdmin.getRank_info(),
						String.valueOf(customExportInfoAccAdmin.getVp()),
						String.valueOf(customExportInfoAccAdmin.getMelee_amount()),
						String.valueOf(customExportInfoAccAdmin.getGun_amount()),
						String.valueOf(customExportInfoAccAdmin.getBtp())
				});
			}
			writer.close();
			ByteArrayResource res = new ByteArrayResource(baos.toByteArray());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=valorant_accounts.csv").contentType(MediaType.parseMediaType("text/csv")).body(res);
		}
		catch (Exception e) {
			// TODO: handle exception
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
