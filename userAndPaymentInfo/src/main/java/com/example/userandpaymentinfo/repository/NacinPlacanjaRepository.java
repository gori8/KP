package com.example.userandpaymentinfo.repository;

import com.example.userandpaymentinfo.model.Casopis;
import com.example.userandpaymentinfo.model.NacinPlacanja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NacinPlacanjaRepository extends JpaRepository<NacinPlacanja, Long> {

    NacinPlacanja findOneById(Long id);

    @Query("SELECT np FROM NacinPlacanja np LEFT OUTER JOIN np.casopisList c WHERE  np.id=?1 AND c.id=?2")
    NacinPlacanja findByIdIfInCasopis(Long id, Long casopisId);
}
