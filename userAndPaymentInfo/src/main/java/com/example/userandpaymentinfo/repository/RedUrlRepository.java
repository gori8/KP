package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.RedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RedUrlRepository extends JpaRepository<RedUrl,Long> {

    RedUrl findOneByUuid(UUID uuid);

}
