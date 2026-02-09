package com.kingdomeprotocol.service;

import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.repository.VouchersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {
    @Autowired
    private VouchersRepository vouchersRepository;

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
    public Vouchers validateVoucher(String voucherCode) {
		int updateStatus = vouchersRepository.increaseUsedCountWhenValid(voucherCode);
		if (updateStatus == 0 ) throw new RuntimeException("Reached max usage or invalid voucher");
		Vouchers voucher = vouchersRepository.getVoucherByVoucherCode(voucherCode);
		return voucher;
    }
	public int caculateVirtualTotal(virtualTotal data) {
		Vouchers voucherCode = getVoucherByVoucherCode(data.voucher);
		if (voucherCode == null) throw new RuntimeException("Not voucher has been found");
		if (voucherCode.getMaxDiscount() == 0) {
			return data.price*(voucherCode.getDiscountPercent()/100);
		}
		else {
			int discountVirtual = data.price*(voucherCode.getDiscountPercent());
			if (discountVirtual > voucherCode.getMaxDiscount()) {
				return voucherCode.getMaxDiscount();
			}
			else {
				return discountVirtual;
			}
		}
	}
    public void deleteVouchers(int id){
        vouchersRepository.deleteById(id);
    }
    
    public record virtualTotal(int price, String voucher) {}
}