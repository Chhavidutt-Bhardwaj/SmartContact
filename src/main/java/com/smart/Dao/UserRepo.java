package com.smart.Dao;

import com.smart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	/* @Query("select u from User where u.email = :email") */
	public User getUserByEmail(@Param("email") String email);

}
