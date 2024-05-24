package isetn.dsi22.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/dashboard/{id}")
public class SousStockController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CommandeSousStockRepository sousStockRepository;
    @Autowired
    private CommandeProductRepository productRepository;
    @Autowired
    private CommandeAuthRepository stockRepository;

    @GetMapping("/{id}/SousStock/{SousStockId}")
    public String listSousStocks(@PathVariable("id") Long id, @PathVariable("SousStockId") Long SousStockId, Model model) {
    	List<Produit> produits = productRepository.findAllBySousStockId(SousStockId)
                .stream()
                .filter(produit -> produit.getSousStock() != null)
                .collect(Collectors.toList());


        model.addAttribute("produits", produits);
        model.addAttribute("id", id);
        model.addAttribute("SousStockId", SousStockId);
        return "listSousStock";
    }

    @GetMapping("/addSousStock/{id}")
    public String showAddForm(@PathVariable("id") Long id, Model model) {
        SousStock sousStock = new SousStock();
        model.addAttribute("sousStock", sousStock);
        model.addAttribute("id", id);
        return "addSousStock";
    }

    @PostMapping("/addSousStock/{id}")
    public String addSousStock(@PathVariable("id") Long id, @Valid SousStock sousStock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "addSousStock";
        }
        sousStock.setStock(stockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid stock Id:" + id)));
        sousStockRepository.save(sousStock);
        return "redirect:/dashboard/" + id;
    }

    @GetMapping("/{id}/edit/{sousStockId}")
    public String showUpdateForm(@PathVariable("id") Long id, @PathVariable("sousStockId") Long sousStockId, Model model) {
        SousStock sousStock = sousStockRepository.findById(sousStockId).orElseThrow(() -> new IllegalArgumentException("Invalid sous-stock Id:" + sousStockId));
        model.addAttribute("sousStock", sousStock);
        model.addAttribute("id", id);
        model.addAttribute("stockId", id);
        return "editSousStock";
    }

    @PostMapping("/{id}/edit/{sousStockId}")
    public String updateSousStock(@PathVariable("id") Long id, @PathVariable("sousStockId") Long sousStockId, @Valid SousStock sousStock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("stockId", id);
            return "updateSousStock";
        }
        sousStock.setId(sousStockId);
        sousStock.setStock(stockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid stock Id:" + id)));
        sousStockRepository.save(sousStock);
        return "redirect:/dashboard/" + id ;
    }

    @GetMapping("/{id}/delete/{sousStockId}")
    public String deleteSousStock(@PathVariable("id") Long id, @PathVariable("sousStockId") Long sousStockId, Model model) {
        SousStock sousStock = sousStockRepository.findById(sousStockId).orElseThrow(() -> new IllegalArgumentException("Invalid sous-stock Id:" + sousStockId));
        List<Produit> produits = productRepository.findAllBySousStockId(sousStockId);
        for (Produit produit : produits) {
            productRepository.delete(produit);
        }
        sousStockRepository.delete(sousStock);
        return "redirect:/dashboard/" + id;
    }
    
    @GetMapping("/{id}/SousStock/{SousStockId}/transfer/")
    public String showTransferForm(@PathVariable("id") Long id, @PathVariable("SousStockId") Long sousStockId, Model model) {
    	logger.debug("Accessing transfer form for Stock ID: {} and SousStock ID: {}", id, sousStockId);
    	List<Produit> produits = productRepository.findByStockIdAndQteStockGreaterThanZero(id);
        model.addAttribute("produits", produits);
        model.addAttribute("id", id);
        model.addAttribute("SousStockId", sousStockId);
        return "transferProduct";
    }
    
    @PostMapping("/{id}/SousStock/{sousStockId}/transfer")
    public String transferProduct(@PathVariable("id") Long id, 
                                  @PathVariable("sousStockId") Long sousStockId,
                                  @RequestParam("produitId") Long produitId, 
                                  @RequestParam("qteSousStock") int qteSousStock,
                                  RedirectAttributes redirectAttributes) {
    	
        Produit produit = productRepository.findById(produitId).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + produitId));
        if(produit.getQteStock() - qteSousStock >= 0) {
        produit.setQteStock(produit.getQteStock() - qteSousStock);
        productRepository.save(produit);
        
        Produit sousStockProduit = new Produit();
        sousStockProduit.setNom(produit.getNom());
        sousStockProduit.setQteStock(0);
        sousStockProduit.setQteSousStock(qteSousStock);
        sousStockProduit.setStock(produit.getStock());

        SousStock sousStock = new SousStock();
        sousStock.setId(sousStockId);
        sousStockProduit.setSousStock(sousStock);

        productRepository.save(sousStockProduit);

        return "redirect:/dashboard/" + id ;
        }else
        	return "redirect:/" + id+ "/SousStock/" +sousStockId+ "/transfer/";
    }

}
