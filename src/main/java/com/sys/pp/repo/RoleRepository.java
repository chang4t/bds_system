package com.sys.pp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sys.pp.model.RolePK;
import com.sys.pp.model.Roles;

@Repository("RoleRepository")
public interface RoleRepository extends JpaRepository<Roles, RolePK> {

	@Query(value = "SELECT * FROM roles WHERE user_id = ?1", nativeQuery = true)
	List<Roles> findByUserId(String userId);
}
