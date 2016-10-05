package com.trimark.backoffice.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.trimark.backoffice.auth.AuthenticationFailedException;
import com.trimark.backoffice.auth.JWTTokenAuthFilter;
import com.trimark.backoffice.framework.api.APIResponse;
import com.trimark.backoffice.framework.controller.BaseController;
import com.trimark.backoffice.model.dto.UserDTO;
import com.trimark.backoffice.model.entity.Organization;
import com.trimark.backoffice.model.entity.Role;
import com.trimark.backoffice.model.entity.User;
import com.trimark.backoffice.service.MailJobService;
import com.trimark.backoffice.service.RoleService;
import com.trimark.backoffice.service.UserDetailsService;
import com.trimark.backoffice.service.UserService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.RandomStringUtils;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Referred: https://github.com/mpetersen/aes-example, http://niels.nu/blog/2015/json-web-tokens.html
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    private @Autowired UserService userService;
    private @Autowired UserDetailsService userDetailsService;
    private @Autowired RoleService roleService;
    private @Autowired MailJobService mailJobService;

    /**
     * Authenticate a user
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse authenticate(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request, HttpServletResponse response) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, AuthenticationFailedException {
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEmail()), "Email is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEncryptedPassword()), "Encrypted password is blank");
        String password = decryptPassword(userDTO);

        LOG.info("Looking for user by email: "+userDTO.getEmail());
        User user = userService.findByEmail(userDTO.getEmail());

        HashMap<String, Object> authResp = new HashMap<>();
        if(userService.isValidPass(user, password)) {
            LOG.info("User authenticated: "+user.getEmail());
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);            
            userService.loginUser(user, request);
            createAuthResponse(user, authResp);
        } else {
            throw new AuthenticationFailedException("Invalid username/password combination");
        }

        return APIResponse.toOkResponse(authResp);
    }

	/**
     * Register new user
     * POST body expected in the format - {"user":{"displayName":"Display Name", "email":"something@somewhere.com"}}
     *
     * @param userDTO
     * @return
	 * @throws Exception 
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse register(@RequestBody UserDTO userDTO,
                                              HttpServletRequest request) throws Exception {
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEmail()), "Email is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEncryptedPassword()), "Encrypted password is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getDisplayName()), "Display name is blank");
        String password = decryptPassword(userDTO);

        LOG.info("Looking for user by email: "+userDTO.getEmail());
        if(userService.isEmailExists(userDTO.getEmail())) {
            return APIResponse.toErrorResponse("Email is taken");
        }

        LOG.info("Creating user: "+userDTO.getEmail());
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setDisplayName(userDTO.getDisplayName());
        user.setPassword(password);
        user.setEnabled(true);
        user.setRole(getUserRole("ROLE_USER"));
        userService.registerUser(user, request);

        HashMap<String, Object> authResp = new HashMap<>();
        createAuthResponse(user, authResp);

        return APIResponse.toOkResponse(authResp);
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> resetPassword(@FormParam("email") String email)  {

    	LOG.info("Reset password email: "+email);

        String password = getRandomPassword();
    	LOG.info("Reset password: "+password);
        /*
        User user = userService.findByEmail(email);
        user.setPassword(password);
        userService.changeUserPassword(user);
        */
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public ResponseEntity<Void> changePassword(@RequestBody UserDTO userDTO) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        LOG.info("Change password email: "+userDTO.getEmail());

        User user = userService.findByEmail(userDTO.getEmail());
        String password = decryptPassword(userDTO);
        user.setPassword(password);
        userService.changeUserPassword(user);
        
        return ResponseEntity.ok().build();
    }
    
    private void createAuthResponse(User user, HashMap<String, Object> authResp) {
        String token = Jwts.builder().setSubject(user.getEmail())
                .claim("role", user.getRole().getRolename()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, JWTTokenAuthFilter.JWT_KEY).compact();
        authResp.put("token", token);
        authResp.put("user", user);
    }

    /**
     * Logs out a user by deleting the session
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("Rest logout");
       if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }        
        //return APIResponse.toOkResponse("success");
       return ResponseEntity.ok().build();
    }

    //-------------------Retrieve All Users--------------------------------------------------------
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    //@PreAuthorize("hasAnyAuthority('CTRL_USER_READ')")
    public ResponseEntity<List<UserDTO>> listAllUsers(HttpServletRequest request) throws Exception {
        List<User> users = (List<User>) userService.getUsers(1, 1000, Order.asc("displayName"));
        if(users.isEmpty()){
            return new ResponseEntity<List<UserDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        List<UserDTO> out = new ArrayList<UserDTO>();
        for (User user : users) {
            out.add(mapper.map(user, UserDTO.class));
        }
        return new ResponseEntity<List<UserDTO>>(out, HttpStatus.OK);
    }

    //-------------------Retrieve Single User--------------------------------------------------------
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching User with id " + id);
    	User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
 
    //-------------------Create a User--------------------------------------------------------
    @RequestMapping(value = "/add/", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO,    UriComponentsBuilder ucBuilder) throws Exception {
    	LOG.info("Creating User " + userDTO.getEmail());
    	String password = decryptPassword(userDTO);
    	
        if (userService.isEmailExists(userDTO.getEmail())) {
        	LOG.info("A User with email " + userDTO.getEmail() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setDisplayName(userDTO.getDisplayName());
        user.setPassword(password);
        user.setEnabled(user.getEnabled());
        //user.setRole(getUserRole("ROLE_USER"));
        user.setRole(mapper.map(user.getRole(), Role.class));
        user.setOrganization(mapper.map(user.getOrganization(), Organization.class));
        userService.insert(user);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    //------------------- Update a User --------------------------------------------------------
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id, @RequestBody UserDTO user) throws Exception {
    	LOG.info("Updating User " + id);
         
        User currentUser = userService.findById(id);
         
        if (currentUser==null) {
        	LOG.info("User with id " + id + " not found");
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }
 
        currentUser.setEmail(user.getEmail());
        currentUser.setDisplayName(user.getDisplayName());
        currentUser.setEnabled(user.getEnabled());
        currentUser.setRole(mapper.map(user.getRole(), Role.class));
        currentUser.setOrganization(mapper.map(user.getOrganization(), Organization.class));
         
        userService.update(currentUser);
        return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
    }
    
    //------------------- Delete a User --------------------------------------------------------
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching & Deleting User with id " + id);
 
    	User user = userService.findById(id);
        if (user == null) {
        	LOG.info("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }
 
        userService.delete(user);
        return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
    }
    
    private String decryptPassword(UserDTO userDTO) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String passPhrase = "biZndDtCMkdeP8K0V15OKMKnSi85";
        String salt = userDTO.getSalt();
        String iv = userDTO.getIv();
        int iterationCount = userDTO.getIterations();
        int keySize = userDTO.getKeySize();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), hex(salt), iterationCount, keySize);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(hex(iv)));
        byte[] decrypted = cipher.doFinal(base64(userDTO.getEncryptedPassword()));

        return new String(decrypted, "UTF-8");
    }

    private String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    private String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    private byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    private Role getUserRole(String rolename) throws Exception {
    	Role role = roleService.findByRolename(rolename);
    	if (role==null) {
	        role = new Role();
	        role.setRolename(rolename);
	        role.setRoledesc("User");
	        roleService.insert(role);
    	}
        return role;
    }
    
    private String getRandomPassword() {
    	return RandomStringUtils.randomAlphanumeric(8);
    }
}
