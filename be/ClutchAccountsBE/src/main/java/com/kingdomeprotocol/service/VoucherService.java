package com.kingdomeprotocol.service;

import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.repository.VouchersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Vouchers> getVoucherByVoucherCode(String voucherCode){
        return vouchersRepository.getVoucherByVoucherCode(voucherCode);
    }

    public Vouchers addVouchers(Vouchers voucher){
        return vouchersRepository.save(voucher);
    }

    public Vouchers updateVouchers(int id, Vouchers voucher){
        voucher.setId(id);
        return vouchersRepository.save(voucher);
    }

    public void deleteVouchers(int id){
        vouchersRepository.deleteById(id);
    }
}
