package com.kingdomeprotocol.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.TransactionRepository;
import com.kingdomeprotocol.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.webhooks.Webhook;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PayOsService {
	private final PayOS payOs;
	private final UserRepository userRepo;
	private final TransactionRepository transactionRepo;
	
	@Value("${payos.return-url}")
	private String returnUrl;
	@Value("${payos.cancel-url}")
	private String cancelUrl;
	@Value("${payos.checksum-key}")
	private String checkSum;
	//generate QR for FE:)
	public Map<String, Object> createQR(String email, int amount, String descrp){
		UserModel user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found user"));
		
		long transactionCode =System.currentTimeMillis() / 1000;
		CreatePaymentLinkRequest payData = CreatePaymentLinkRequest.builder().orderCode(transactionCode).amount(Long.parseLong(String.valueOf(amount))).description(descrp).cancelUrl(cancelUrl).returnUrl(returnUrl).build();
		CreatePaymentLinkResponse resPayment = payOs.paymentRequests().create(payData);
		
		Map<String, Object> resData = new HashMap<>();
		resData.put("orderCode", resPayment.getOrderCode());
		resData.put("qrUrl", resPayment.getQrCode());
		resData.put("checkoutUrl", resPayment.getCheckoutUrl());
		
		return resData;
	}
}
