package com.app.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
	
	Page<User> findByUsernameNot(String username, Pageable page);
	
	User findById(long id);

	@Modifying
	@Transactional
	@Query(value="UPDATE identity_file\r\n"
			+ "left join auction.user \r\n"
			+ "on auction.user.id = identity_file.user_id\r\n"
			+ "set user.is_approved=1\r\n"
			+ "where identity_file.id_number=:id"  ,nativeQuery = true)
	void updateUserApproved(@Param("id") long id );  //make is approved true with user id
	
}
