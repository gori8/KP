package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findOneById(Long id);
    Item findOneByUuid(UUID uuid);
}
