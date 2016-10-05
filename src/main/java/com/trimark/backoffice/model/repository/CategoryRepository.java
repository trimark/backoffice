package com.trimark.backoffice.model.repository;

import java.util.List;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.Category;

/**
 * CRUD operations come from Base Repo but additional operations can be defined here.
 *
 */
public interface CategoryRepository extends BaseJPARepository<Category, Long> {
    /**
     * Finds a category with the given categoryName
     *
     * @param categoryName
     * @return
     */
    public Category findByCategoryName(String categoryName);

    /**
     * Finds a category with the given categoryPriority
     *
     * @param categoryPriority
     * @return
     */
    public Category findByCategoryPriority(Integer categoryPriority);

    /**
     * Finds sub categories with the given parentCategory
     *
     * @param parentCategory
     * @return
     */
    public List<Category> findSubCategories(Category parentCategory);
}
