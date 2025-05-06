package com.esw.productservice.service;

import com.esw.productservice.dto.category.CategoryCreateRequest;
import com.esw.productservice.dto.category.CategoryUpdateRequest;
import com.esw.productservice.exception.AlreadyExistsException;
import com.esw.productservice.exception.NotFoundException;
import com.esw.productservice.model.Category;
import com.esw.productservice.repository.CategoryRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public CategoryService(CategoryRepository categoryRepository, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    public Category findById(Long id) {
        return getCategoryOrThrows(id);
    }

    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found");
        }

        return categoryRepository.findAll();
    }

    @Transactional
    public Category save(CategoryCreateRequest request) {
        validateIfCategoryExistsByName(request.name());

        Category category = new Category();
        category.setName(request.name());

        validate(category);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryUpdateRequest request) {
        Category category = getCategoryOrThrows(id);
        category.setName(request.name());

        validate(category);

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getCategoryOrThrows(id);
        categoryRepository.deleteById(category.getId());
    }

    private Category getCategoryOrThrows(Long id) throws NotFoundException {
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isEmpty()) {
            throw new NotFoundException(Category.class, "id", id.toString());
        }

        return category.get();
    }

    private void validateIfCategoryExistsByName(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new AlreadyExistsException(Category.class, "name", name);
        }
    }

    private void validate(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed", violations);
        }
    }
}
