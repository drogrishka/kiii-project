package mk.ukim.finki.wpvezbi.web.controller;

import mk.ukim.finki.wpvezbi.model.Category;
import mk.ukim.finki.wpvezbi.model.Manufacturer;
import mk.ukim.finki.wpvezbi.model.Product;
import mk.ukim.finki.wpvezbi.service.CategoryService;
import mk.ukim.finki.wpvezbi.service.ManufacturerService;
import mk.ukim.finki.wpvezbi.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;

    public ProductController(ProductService productService, CategoryService categoryService, ManufacturerService manufacturerService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public String getProductPage(@RequestParam(required = false) String error, Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        List<Product> products=this.productService.findAll();
        model.addAttribute("products",products);
        return "products";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        this.productService.deleteById(id);
        return "redirect:/products";
    }
    @GetMapping("/add-form")
    public String addProductPage(Model model){
        List<Category>categories=this.categoryService.listCategories();
        List<Manufacturer> manufacturers=this.manufacturerService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("manufacturers",manufacturers);
        return  "add product";
    }

    @GetMapping("/edit-form/{id}")
    public String editProductPage(@PathVariable Long id,Model model){
        if (this.productService.findById(id).isPresent()) {
            Product product=this.productService.findById(id).get();
            List<Category> categories = this.categoryService.listCategories();
            List<Manufacturer> manufacturers = this.manufacturerService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("manufacturers", manufacturers);
            model.addAttribute("product",product);
            return "add product";
        }
        return "redirect:/products?error=ProductNotFound";
    }

    @PostMapping("/add")
    public String saveProduct(@RequestParam(required = false) Long id,
                              @RequestParam String name,
                              @RequestParam Double price,
                              @RequestParam Integer quantity,
                              @RequestParam Long category,
                              @RequestParam Long manufacturer){
        if (id!=null){
            this.productService.edit(id, name, price, quantity, category, manufacturer);
        }else {
            this.productService.save(name, price, quantity, category, manufacturer);
        }
        return "redirect:/products";
    }




}
