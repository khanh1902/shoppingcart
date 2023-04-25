package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.config.PaymentConfig;
import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.request.payment.PaymentRequest;
import com.laptrinhjava.ShoppingCart.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Qualifier("paymentServiceImpl")
    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", paymentService.findAll())
        );
    }

    @PostMapping("/create-pay")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody PaymentRequest requestParams,
                                                        HttpServletRequest request) throws IOException {

        int amount = requestParams.getAmount() * 100;

        Map<String, String> vnp_params = new HashMap<>();
        vnp_params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_params.put("vnp_TmnCode", PaymentConfig.vnp_TmnCode);
        vnp_params.put("vnp_Amount", String.valueOf(amount));
        String bank_code = requestParams.getBankCode();
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_params.put("vnp_BankCode", bank_code);
        }

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = time.format(formatter);
        vnp_params.put("vnp_CreateDate", vnp_CreateDate);

        vnp_params.put("vnp_CurrCode", PaymentConfig.vnp_CurrCode);
        vnp_params.put("vnp_IpAddr", PaymentConfig.getIpAddress(request));
        vnp_params.put("vnp_Locale", PaymentConfig.vnp_Locale);
        vnp_params.put("vnp_OrderInfo", requestParams.getDescription());
        vnp_params.put("vnp_OrderType", PaymentConfig.vnp_OrderType);
        vnp_params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_params.put("vnp_TxnRef", PaymentConfig.getOTP(8));

        List fieldName = new ArrayList(vnp_params.keySet());
        Collections.sort(fieldName);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator iterator = fieldName.iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            String value = vnp_params.get(name);
            if ((value != null) && (value.length() > 0)) {

                hashData.append(name);
                hashData.append("=");
                hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                if (iterator.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", paymentUrl)
        );
    }
}
