package com.tender.repository;

import com.tender.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempUsersRepository extends JpaRepository<TempUser,String> {

}
