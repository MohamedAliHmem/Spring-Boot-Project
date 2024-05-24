package isetn.dsi22.test;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeSousStockRepository  extends JpaRepository<SousStock, Long>{

	List<SousStock> findAllByStockId(Long stockId);
	
	
}
