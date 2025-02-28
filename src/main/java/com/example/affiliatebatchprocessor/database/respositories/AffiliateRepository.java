package com.example.affiliatebatchprocessor.database.respositories;
import com.example.affiliatebatchprocessor.database.entities.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {
    @Query(value = "SELECT EXISTS (SELECT 1 FROM affiliates WHERE dni = :dni)", nativeQuery = true)
    boolean existsByDni(@Param(value = "dni") String dni);
}
