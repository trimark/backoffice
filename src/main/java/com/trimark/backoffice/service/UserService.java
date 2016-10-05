package com.trimark.backoffice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.framework.exception.EmailNotFoundException;
import com.trimark.backoffice.model.entity.User;

/**
 *
 * Service class to have business logic operation on User entity
 *
 */
public interface UserService extends BaseService<User, Long> {

    /**
     * Register a new user into the system
     *
     * @param user
     * @param request
     *
     * @return
     */
    public User registerUser(User user, HttpServletRequest request);


    /**
     * Login a new user into the system
     *
     * @param user
     * @param request
     *
     * @return
     */
    public User loginUser(User user, HttpServletRequest request);


    /**
     * Method to validate whether the given password
     * is same as users password stored in the system
     *
     * @param user
     * @param pass
     *
     * @return
     */
    public boolean isValidPass(User user, String pass);


    /**
     * Validates whether the given email already
     * exists in the system.
     *
     * @param email
     *
     * @return
     */
    public boolean isEmailExists(String email);


    /**
     * Finds a user entity by the given email
     *
     * @param email
     * @return
     */
    public User findByEmail(String email) throws EmailNotFoundException;
    
    public List<User> getUsers(int pageNum, int countPerPage, Order order);
    
    public User changeUserPassword(User user) throws EmailNotFoundException;
}
