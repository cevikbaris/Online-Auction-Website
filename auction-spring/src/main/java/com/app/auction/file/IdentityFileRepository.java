package com.app.auction.file;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityFileRepository extends JpaRepository<IdentityFile, Integer> {

	@Query(value="SELECT * FROM auction.identity_file "
			+ "where date<(  SELECT DATE_ADD( NOW(), INTERVAL -10 MINUTE) ) and isnull(user_id)",nativeQuery = true)
	List<IdentityFile> getFilesMoreThan10MinAndNullUser();
	
	@Query(value="SELECT * FROM auction.identity_file where user_id IS NOT NULL ",nativeQuery = true)
	List<IdentityFile> getAllIdentityFiles();

	IdentityFile findByIdNumber(long id);

	@Modifying
	@Transactional
	@Query(value="delete from identity_file where id_number=:userid ",nativeQuery=true)
	void deleteIdentityFile(@Param("userid") long userid);

}
