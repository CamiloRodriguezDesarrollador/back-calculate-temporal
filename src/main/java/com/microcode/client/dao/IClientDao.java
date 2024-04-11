package com.microcode.client.dao;

import com.microcode.client.entity.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IClientDao extends CrudRepository<Client, Long> {

    @Query("SELECT p.cliId, p.cliName FROM Client p WHERE p.cliStatus = :cliStatus ORDER BY p.cliName ASC")
    List<Object> findByAll(String cliStatus);
    Client findByCliIdAndCliStatus(Integer cliId , String cliStatus);
    Client findByCliCode(String code);
    Client findByCliNameAndCliStatus(String cliName, String cliStatus);
    @Query("SELECT p FROM Client p WHERE ( (lower(p.cliNd) LIKE %:text% or lower(p.cliName) LIKE %:text%   " +
            " or lower(p.cliNdGroup) LIKE %:text% or lower(p.paiName) LIKE %:text%  " +
            " or lower(p.dptName) LIKE %:text% or lower(p.ciuName) LIKE %:text% " +
            " or lower(p.cliCellPhone) LIKE %:text%  ) " +
            " AND p.cliStatus = :cliStatus ) ORDER BY p.cliId DESC ")
    List<Client> findTableData(String cliStatus, String text, Pageable pageable);

    @Query("SELECT count(p) FROM Client p WHERE ( (lower(p.cliNd) LIKE %:text% or lower(p.cliName) " +
            "LIKE %:text% or lower(p.cliNdGroup) LIKE %:text% or lower(p.paiName) LIKE %:text%" +
            " or lower(p.dptName) LIKE %:text% or lower(p.dptName) LIKE %:text% " +
            " or lower(p.cliCellPhone) LIKE %:text% ) " +
            "AND p.cliStatus = :cliStatus) ")
    Integer findTableQuantity(String cliStatus, String text);

    @Query("SELECT p FROM Client p WHERE ( (lower(p.cliNd) LIKE %:text% or lower(p.cliName) " +
            "LIKE %:text% or lower(p.cliNdGroup) LIKE %:text% ) " +
            "AND p.cliStatus = :cliStatus) order by p.cliName ASC")
    List<Client> findWithFilter(String cliStatus, String text);

}
