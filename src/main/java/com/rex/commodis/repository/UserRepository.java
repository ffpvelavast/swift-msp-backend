/** Spring Data JPA repository for User entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.User;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
