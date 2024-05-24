package isetn.dsi22.test;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

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

import jakarta.validation.Valid;

@Controller
public class CommandeController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@GetMapping("/login")
	public String loginView(Model model) {
		model.addAttribute("Auth",new Stock());
		return "login";
	}
	
	@GetMapping("/register")
	public String registerView(Model model) {
		model.addAttribute("Auth",new Stock());
		return "register";
	}
	
	@Autowired
    private CommandeProductRepository productRepository;
	@Autowired
    private CommandeSousStockRepository sousStockRepository;

	@GetMapping("/dashboard/{id}")
	public String dashboardView(@PathVariable("id") Long id, Model model) {
	    List<Produit> produits = productRepository.findByStockIdAndQteStockGreaterThanZero(id)
	                                              .stream()
	                                              .filter(produit -> produit.getStock() != null)
	                                              .collect(Collectors.toList());
	    
	    List<SousStock> sousStocks = sousStockRepository.findAllByStockId(id)
	                                                     .stream()
	                                                     .filter(sousStock -> sousStock.getStock() != null) // && sousStock -> sousStock.getStock().getId() = id
	                                                     .collect(Collectors.toList());
	    
	    model.addAttribute("produits", produits);
	    model.addAttribute("sousStocks", sousStocks);
	    model.addAttribute("id", id);
	    logger.debug("Model data: {}", model);
	    return "dashboard";
	}



	
	@Autowired
	CommandeAuthRepository CommandeRepository;
	@PostMapping("/registerForm")
	public String register(@Valid @ModelAttribute("Auth") Stock Auth, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
            return "register";
        }else {
        	Optional<Stock> existingAuth = CommandeRepository.findByEmailContaining(Auth.getEmail());
        	if(existingAuth.isPresent()) {
        		return "register";
        	}else {
				CommandeRepository.save(Auth);
				return "login";
        	}
        }
	}
	
	@PostMapping("/loginForm")
	public String login(@Valid @ModelAttribute("Auth") Stock auth, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
            return "login";
        }else {
        	Optional<Stock> existingAuth = CommandeRepository.findByEmailContaining(auth.getEmail());
            if (existingAuth.isPresent() && existingAuth.get().getPassword().equals(auth.getPassword())) {
                return "redirect:/dashboard/" + existingAuth.get().getId();
            } else {
                return "login";
            }
        }
	}
}
