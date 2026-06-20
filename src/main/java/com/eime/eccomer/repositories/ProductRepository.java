package com.eime.eccomer.repositories;

import com.eime.eccomer.model.Category;
import com.eime.eccomer.model.Product;
import com.eime.eccomer.payload.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
