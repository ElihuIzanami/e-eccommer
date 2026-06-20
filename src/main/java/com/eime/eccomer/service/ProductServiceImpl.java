package com.eime.eccomer.service;

import com.eime.eccomer.exceptions.ResourceNotFoundException;
import com.eime.eccomer.model.Category;
import com.eime.eccomer.model.Product;
import com.eime.eccomer.payload.ProductDTO;
import com.eime.eccomer.payload.ProductResponse;
import com.eime.eccomer.repositories.CategoryRepository;
import com.eime.eccomer.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Product product = modelMapper.map(productDTO,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                             .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        /***/
        List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        // Get the existing product from DB
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO,Product.class);
        //Update the product info white the one in request body
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setSpecialPrice(product.getSpecialPrice());
        //Save to database
        Product saveProduct = productRepository.save(productFromDB);
        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);
    }


}
