package isetn.dsi22.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommandeProductRepository  extends JpaRepository<Produit, Long> {

	List<Produit> findAllByStockId(Long stockId);
	List<Produit> findByStockId(Long stockId);
	List<Produit> findAllBySousStockId(Long SousStocktockId);
	
	@Query("SELECT p FROM Produit p group by p.nom")
    List<Produit> findByStockIdAndQteStockGreaterThanZero(Long stockId);
}
