package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    List<Address> findALlByUsers_Id(Long usersId);
    Address findAddressById(Long id);

}
