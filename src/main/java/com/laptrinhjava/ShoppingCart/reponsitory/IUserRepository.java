package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    List<Users> findAllByEmail(String email);
    Users findByEmail(String email);
    Boolean existsByEmail(String email);
    List<Users> findByRoles_Id(Long roles_id);
}
