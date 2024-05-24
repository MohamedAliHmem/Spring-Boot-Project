package isetn.dsi22.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommandeAuthRepository  extends JpaRepository<Stock, Long> {
	Optional<Stock> findByEmailContaining(String email);
}
