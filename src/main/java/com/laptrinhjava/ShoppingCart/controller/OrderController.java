package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getAllOrderForUser(@RequestParam(name = "status", required = false) String status) {
        try {
            List<OrderResponse> orderResponses = orderService.getAllOrderForUser(status);
            if(orderResponses.isEmpty()) throw new Exception("orders is null!");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully!", orderResponses)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/get-one")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getOneOrderForUser(@RequestParam(name = "orderId") Long orderId) {
        try {
            OrderResponse orderResponse = orderService.getOneOrderForUser(orderId);
            if(orderResponse == null) throw new Exception("Order is null");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully!", orderResponse)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getAllOrderForAdmin(@RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
                                                              @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit) {
        try {
            PageRequest pageRequest = PageRequest.of(offset, limit);
            List<OrderResponse> orderResponses = orderService.getAllOrderForAdmin();
            if(orderResponses.isEmpty()) throw new Exception("List order is empty!");
            int start = (int) pageRequest.getOffset();
            int end = Math.min(start + pageRequest.getPageSize(), orderResponses.size());
            Page<OrderResponse> orderResponsePage = new PageImpl<>(orderResponses.subList(start, end), pageRequest, orderResponses.size());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", orderResponsePage)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }

    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> save(@RequestBody OrderRequest orderRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Your order is being confirmed!", orderService.save(orderRequest))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateStatusOrder(@RequestParam(name = "orderId") Long orderId,
                                                            @RequestParam(name = "status") String newStatus) {
        try {
            UpdateStatusResponse updateStatus = orderService.updateStatusOrder(orderId, newStatus);
            if(updateStatus == null) throw new Exception("Update failed");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully!", updateStatus)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

}
