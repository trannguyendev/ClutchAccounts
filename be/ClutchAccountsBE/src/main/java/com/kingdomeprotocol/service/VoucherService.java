package com.kingdomeprotocol.service;

import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.repository.UserRepository;
import com.kingdomeprotocol.repository.VouchersRepository;
import com.kingdomeprotocol.repository.VoucherusageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {
    
    private final VouchersRepository vouchersRepository;
    private final VoucherusageRepository usageRepo;
    private final UserRepository userRepo;
    private final UserService userServ;
    
    public List<Vouchers> getAllVouchers(){
        return vouchersRepository.findAll();
    }
    
    public Optional<Vouchers> getVoucherById(int id){
        return vouchersRepository.findById(id);
    }

    public Vouchers getVoucherByVoucherCode(String voucherCode){
        return vouchersRepository.getVoucherByVoucherCode(voucherCode);
    }

    public Vouchers addVouchers(Vouchers voucher){
        return vouchersRepository.save(voucher);
    }

    public Vouchers updateVouchers(int id, Vouchers voucher){
        voucher.setId(id);
        return vouchersRepository.save(voucher);
    }
@Transactional
    public Vouchers validateVoucher(String voucherCode, Authentication authen) {
		Vouchers voucher = vouchersRepository.getVoucherByVoucherCode(voucherCode);
		UserModel user = userServ.loadUserByEmail(authen.getName()).orElseThrow(() -> new RuntimeException("Not found user"));
		if (voucher == null) throw new RuntimeException("Invalid voucher");
		if (usageRepo.existsByUserAndVoucher(user, voucher)) throw new RuntimeException("You have used this voucher before");
		int updateStatus = vouchersRepository.increaseUsedCountWhenValid(voucherCode);
		if (updateStatus == 0 ) throw new RuntimeException("Reached max usage or invalid voucher");
		
		return voucher;
    }
	public int caculateVirtualTotal(virtualTotal data, Authentication authen) {
		UserModel user = userServ.loadUserByEmail(authen.getName()).orElseThrow(() -> new RuntimeException("Not found user"));
		int discountAmount = 0;
		Vouchers voucherCode = getVoucherByVoucherCode(data.voucher);
		if(usageRepo.existsByUserAndVoucher(user, voucherCode)) throw new RuntimeException("You have used this voucher before");
		if (voucherCode == null) throw new RuntimeException("No voucher has been found:))");
		if (voucherCode.getUsedCount() > voucherCode.getMaxUsage()) throw new RuntimeException("You have reached limit of this voucher");
		if (voucherCode.isActive() == false) throw new RuntimeException("Invalid voucher");
		if (voucherCode.getMaxDiscount() <= 0) {
			discountAmount = data.price*voucherCode.getDiscountPercent()/100;
			return discountAmount;
		}
		else{
			 discountAmount = data.price*voucherCode.getDiscountPercent()/100;
			if (discountAmount > voucherCode.getMaxDiscount()) {
				return voucherCode.getMaxDiscount();
			}
			else {
				return discountAmount;
			}
		}
	}
    public void deleteVouchers(int id){
        vouchersRepository.deleteById(id);
    }
    
    public record virtualTotal(int price, String voucher) {}
}