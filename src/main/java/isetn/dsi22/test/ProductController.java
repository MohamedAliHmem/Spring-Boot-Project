package isetn.dsi22.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CommandeProductRepository productRepository;

    @GetMapping("/addProduct/{id}")
    public String addProduct(@PathVariable("id") int id, Model model) {
        model.addAttribute("Produit", new Produit());
        model.addAttribute("id", id);
        return "addProduct";
    }

    @PostMapping("/addProductForm/{id}")
    public String Add(@PathVariable("id") Long id, @Valid @ModelAttribute("Produit") Produit produit, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "addProduct";
        } else {
        	Stock s = new Stock();
        	s.setId(id);
        	produit.setStock(s);
            logger.debug("Produit data: {}", produit);
            productRepository.save(produit);
            List<Produit> produits = productRepository.findAllByStockId(id);
            model.addAttribute("produits", produits);
            
            //return "redirect:/dashboard/" + produit.getStock().getId();
            return "dashboard";
        }
    }
    
    @GetMapping("/dashboard/{id}/product/edit/{idProduct}")
    public String editProduct(@PathVariable("idProduct") Long productId, @PathVariable("id") Long id, Model model) {
        Produit produit = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        model.addAttribute("produit", produit);
        return "editProduct";
    }

    @PostMapping("/dashboard/{id}/product/update/{idProduct}")
    public String updateProduct(@PathVariable("idProduct") Long productId, @PathVariable("id") Long id, @Valid Produit produit, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editProduct";
        }
        produit.setId(productId);
        Stock s = new Stock();
    	s.setId(id);
    	produit.setStock(s);
        productRepository.save(produit);
        return "redirect:/dashboard/" + id;
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        Produit produit = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        productRepository.delete(produit);
        return "redirect:/dashboard/" + produit.getStock().getId();
    }

    @GetMapping("/product/share/{id}")
    public String shareProduct(@PathVariable("id") Long id) {
        // Logique pour partager le produit
        // Vous pouvez ajouter du code pour partager le produit ici
        return "listSousStock";
    }
    
    @PostMapping("/dashboard/{id}/product/updateSousStock/{idProduct}")
    public String updateProductSousStock(@PathVariable("idProduct") Long productId, @PathVariable("id") Long id,@RequestParam("newQteSousStock") int newQteSousStock, @Valid Produit produit, BindingResult bindingResult, Model model) {
    	produit = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        
        produit.setQteSousStock(newQteSousStock);
        productRepository.save(produit);
        return "redirect:/dashboard/" + id;
    }
}
