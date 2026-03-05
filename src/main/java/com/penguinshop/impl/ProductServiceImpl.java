package com.penguinshop.impl;

// Java utility imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Spring Data classes used for pagination and sorting
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// Used for dynamic filtering of database queries
import org.springframework.data.jpa.domain.Specification;

// Marks this class as a Spring Service component
import org.springframework.stereotype.Service;

// Custom exception used when product related errors occur
import com.penguinshop.exceptions.ProductException;

// Entity classes (database models)
import com.penguinshop.model.Category;
import com.penguinshop.model.Product;
import com.penguinshop.model.Seller;

// Repository interfaces used to interact with the database
import com.penguinshop.repository.CategoryRepository;
import com.penguinshop.repository.ProductRepository;

// DTO used when creating a product
import com.penguinshop.request.CreateProductRequest;

// Service interface that this class implements
import com.penguinshop.service.ProductService;

// JPA Criteria classes used for building dynamic queries
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

// Lombok annotation that automatically generates a constructor
// for all final fields (dependency injection)
import lombok.RequiredArgsConstructor;

@Service // Marks this class as a service layer bean
@RequiredArgsConstructor // Lombok generates constructor for final fields
public class ProductServiceImpl implements ProductService {

    // Repository used for category database operations
    private final CategoryRepository categoryRepository;

    // Repository used for product database operations
    private final ProductRepository productRepository;


    // Method to create a new product
    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {

        // Try to find first level category in the database
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());

        // If category does not exist, create it
        if (category1 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory()); // set category name/id
            category.setLevel(1); // level 1 category
            category1 = categoryRepository.save(category); // save to DB
        }

        // Find second level category
        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());

        // If not found, create it
        if (category2 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2); // level 2 category
            category.setParentCategory(category1); // parent = level 1 category
            category2 = categoryRepository.save(category);
        }

        // Find third level category
        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());

        // If not found, create it
        if (category3 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3); // level 3 category
            category.setParentCategory(category2); // parent = level 2 category
            category2 = categoryRepository.save(category);
        }

        // Calculate discount percentage between MRP and selling price
        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        // Create new Product object
        Product product = new Product();

        // Set seller who owns this product
        product.setSeller(seller);

        // Assign category to the product
        product.setCategory(category3);

        // Set product description
        product.setDescription(req.getDescription());

        // Set the time when the product is created
        product.setCreatedAt(LocalDateTime.now());

        // Set product title
        product.setTitle(req.getTitle());

        // Set product color
        product.setColor(req.getColor());

        // Set selling price
        product.setSellingPrice(req.getSellingPrice());

        // Set product images
        product.setImages(req.getImage_urls());

        // Set MRP (original price)
        product.setMrpPrice(req.getMrpPrice());

        // Set available sizes
        product.setSizes(req.getSizes());

        // Set calculated discount percentage
        product.setDiscountPercent(discountPercentage);

        // Save product to database and return saved product
        return productRepository.save(product);
    }


    // Helper method to calculate discount percentage
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        // Validate that MRP is greater than zero
        if (mrpPrice <= 0)
            throw new IllegalArgumentException("Actual price must be greater than 0");

        // Calculate discount amount
        double discount = mrpPrice - sellingPrice;

        // Calculate discount percentage
        double discountPercentage = (discount / mrpPrice) * 100;

        // Convert to integer and return
        return (int) discountPercentage;
    }


    // Delete product by ID
    @Override
    public void deleteProduct(Long productId) throws ProductException {

        // Find product first (throws exception if not found)
        Product product = findProductById(productId);

        // Delete the product from the database
        productRepository.delete(product);
    }


    // Update an existing product
    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        // Ensure the product exists
        findProductById(productId);

        // Set ID to ensure correct product is updated
        product.setId(productId);

        // Save updated product
        return productRepository.save(product);
    }


    // Find a product by ID
    @Override
    public Product findProductById(Long productId) throws ProductException {

        // findById returns Optional<Product>
        // If product does not exist, throw custom exception
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with id " + productId));
    }


    // Search products by query (name/keyword)
    @Override
    public List<Product> searchProducts(String query) {

        // Calls custom repository search method
        return productRepository.searchProduct(query);
    }


    // Get products with filtering, sorting, and pagination
    @Override
    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber) {

        // Create dynamic query specification
        Specification<Product> spec = (root, query, criteriaBuilder) -> {

            // List to store filtering conditions
            List<Predicate> predicates = new ArrayList<>();

            // Filter by category
            if (category != null) {

                // Join Product table with Category table
                Join<Product, Category> categoryJoin = root.join("category");

                // Add category filter condition
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            // Filter by colors (NOTE: logic may be incorrect here)
            if (category != null && !colors.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), colors));
            }

            // Filter by size
            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }

            // Filter by minimum price
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            // Filter by maximum price
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            // Filter by minimum discount
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }

            // Filter by stock availability
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            // Combine all conditions with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };


        // Pagination and sorting setup
        Pageable pageable;

        if (sort != null && !sort.isEmpty()) {

            // Switch expression for sorting options
            pageable = switch (sort) {

                // Sort by lowest price first
                case "price_low" -> PageRequest.of(
                        pageNumber != null ? pageNumber : 0,
                        10,
                        Sort.by("sellingPrice").ascending());

                // Sort by highest price first
                case "price_high" -> PageRequest.of(
                        pageNumber != null ? pageNumber : 0,
                        10,
                        Sort.by("sellingPrice").descending());

                // Default case: no sorting
                default -> PageRequest.of(
                        pageNumber != null ? pageNumber : 0,
                        10,
                        Sort.unsorted());
            };

        } else {

            // If no sort provided, return unsorted results
            pageable = PageRequest.of(
                    pageNumber != null ? pageNumber : 0,
                    10,
                    Sort.unsorted());
        }

        // Execute query with filters and pagination
        return productRepository.findAll(spec, pageable);
    }


    // Get all products belonging to a specific seller
    @Override
    public List<Product> getProductBySellerId(Long sellerId) {

        // Calls repository method to find products by seller ID
        return productRepository.findBySellerId(sellerId);
    }

}
