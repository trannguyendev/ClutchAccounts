package com.kingdomeprotocol.controller;

import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController { // 1. Đã có public (Tốt)
    @Autowired
    VoucherService voucherService;

    @GetMapping("/getAllVouchers")
    public List<Vouchers> getAllVouchers(){
        return voucherService.getAllVouchers();
    }


    @PostMapping("/addVoucher")
    public Vouchers addVoucher(@RequestBody Vouchers voucher){
        return voucherService.addVouchers(voucher);
    }

    @PutMapping("/updateVoucher/{id}")
    public Vouchers updateVoucher(@PathVariable int id, @RequestBody Vouchers voucher){
        return voucherService.updateVouchers(id, voucher);
    }

    @DeleteMapping("/deleteVoucher/{id}")
    public void deleteVoucher(@PathVariable int id){
        voucherService.deleteVouchers(id);
    }

    @GetMapping("/getVoucherById/{id}")
    public Optional<Vouchers> getOneVoucherById(@PathVariable int id){
        return voucherService.getVoucherById(id);
    }

    @GetMapping("/getVoucherByVoucherCode")
    public List<Vouchers> getOneVoucherByVoucherCode(@RequestParam("voucherCode") String voucherCode){
        return voucherService.getVoucherByVoucherCode(voucherCode);
    }
}