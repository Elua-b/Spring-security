    package com.eloi.example.v1.serviceImpls;

import com.eloi.example.v1.dto.requests.CreateUserDTO;
import com.eloi.example.v1.dto.requests.UpdateRoleDTO;
import com.eloi.example.v1.dto.requests.UserLoginDTO;
import com.eloi.example.v1.dto.responses.LoginResponse;
import com.eloi.example.v1.dto.responses.UserDTOMapper;
import com.eloi.example.v1.enums.Gender;
import com.eloi.example.v1.utils.Hash;
import com.eloi.example.v1.exceptions.LoginFailedException;
import com.eloi.example.v1.models.Role;
import com.eloi.example.v1.models.User;
import com.eloi.example.v1.payload.ApiResponse;
import com.eloi.example.v1.repositories.RoleRepository;
import com.eloi.example.v1.repositories.UserRepository;
import com.eloi.example.v1.security.jwt.JwtUtils;
import com.eloi.example.v1.security.user.UserAuthority;
import com.eloi.example.v1.security.user.UserSecurityDetails;
import com.eloi.example.v1.security.user.UserSecurityDetailsService;
import com.eloi.example.v1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final UserSecurityDetailsService userSecurityDetailsService;
    private final JwtUtils jwtUtils;

    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ApiResponse> getAllUsers() throws Exception{
      try {
          List<User> users = userRepository.findAll();
          return  ResponseEntity.ok().body(new ApiResponse(
                  true,
                  "Successfully fetched the users",
                  users.stream().map(userDTOMapper).collect(Collectors.toList())
          ));
      }catch (Exception e){
          return ResponseEntity.status(500).body(new ApiResponse(
                  false,
                  "Failed to fetch the users"
          ));
      }
    }

    @PreAuthorize("hasAuthority('Admin') or #user_id == authentication.principal.grantedAuthorities[0].userId")
    public ResponseEntity<ApiResponse> getUserById(Long user_id) throws Exception{
        if(userRepository.existsById(user_id)){
            try {
                Optional<User> user = userRepository.findById(user_id);
                return ResponseEntity.ok().body(new ApiResponse(
                        true,
                        "Successfully fetched the users",
                        user.map(userDTOMapper)
                ));
            }catch (Exception e){
                return ResponseEntity.status(500).body(new ApiResponse(
                        false,
                        "Failed to fetch the user"
                ));
            }
        }else{
            return ResponseEntity.status(404).body(new ApiResponse(
                    false,
                    "The user with the id:" + user_id + " does not exist"
            ));
        }
    }

    public ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO) throws Exception{
         Optional<User> user1 = userRepository.findUserByEmail(createUserDTO.getEmail());
        System.out.println(createUserDTO.getGender());
         if(!user1.isPresent()){
             User user = new User(
                     createUserDTO.getEmail(),
                     createUserDTO.getUsername(),
                     createUserDTO.getNational_id(),
                     createUserDTO.getGender(),
                     createUserDTO.getPassword()
             );

             Long id = 1l;
             Optional<Role> roleOptional = roleRepository.findById(id);
            if(roleOptional.isPresent()){
                user.setRole(roleOptional.get());
            }else{
                user.setRole(null);
            }

             System.out.println(createUserDTO.getGender());

             // setting a default avatar
             if(user.getGender().equals(Gender.MALE)){
                 user.setProfilePicture("https://www.google.com/imgres?imgurl=https%3A%2F%2Fus.123rf.comDdAQ");
             }else{
                 user.setProfilePicture("3clooP5UOInDwhJLVR8gnk7eFh5J/uomCPYWLsjLWQFuZ82f6gSwBAULJpOVytdZbH8JH");
             }

             Hash hash = new Hash();
             user.setPassword(hash.hashPassword(user.getPassword()));
             user.setLastLogin(null);
             try {
                 userRepository.save(user);
                 return ResponseEntity.ok().body(new ApiResponse(
                         true,
                         "Successfully saved the user",
                         user
                 ));
             }catch (HttpServerErrorException.InternalServerError e){
                 return ResponseEntity.status(500).body(new ApiResponse(
                         false,
                         "Failed to create the user"
                 ));
             }
         }else{
             return ResponseEntity.status(404).body(new ApiResponse(
                     false,
                     "The user with the email:" + createUserDTO.getEmail() + " already exists"
             ));
         }
    }

    public void updateUserMapper(Optional<User> user, CreateUserDTO createUserDTO){
        user.get().setEmail(createUserDTO.getEmail());
        user.get().setUsername(createUserDTO.getUsername());
        user.get().setNational_id(createUserDTO.getNational_id());
        user.get().setPassword(createUserDTO.getPassword());
    }

    @PreAuthorize("hasAuthority('Admin') or #user_id == authentication.principal.grantedAuthorities[0].userId")
    @Transactional
    public ResponseEntity<ApiResponse> updateUser(Long user_id ,  CreateUserDTO createUserDTO) throws Exception {
        if (userRepository.existsById(user_id)) {
            Optional<User> user = userRepository.findById(user_id);
            updateUserMapper(user, createUserDTO);
            return ResponseEntity.ok().body(new ApiResponse(
                    true,
                    "Successfully updated the user",
                    user.map(userDTOMapper)
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(
                    false,
                    "The user with the id:" + user_id + " does not exist"
            ));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user_id == authentication.principal.grantedAuthorities[0].userId")
    public ResponseEntity<ApiResponse> deleteUser(Long user_id) throws Exception{
        if (userRepository.existsById(user_id)) {
            Optional<User> user = userRepository.findById(user_id);
            userRepository.deleteById(user_id);
            return ResponseEntity.ok().body(new ApiResponse(
                    true,
                    "Successfully deleted the user",
                    user.map(userDTOMapper)
            ));
        }else {
            return ResponseEntity.status(404).body(new ApiResponse(
                    false,
                    "The user with the id:" + user_id + " does not exist"
            ));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> authenticateUser(UserLoginDTO userLoginDTO) throws BadCredentialsException , LoginFailedException{
            Optional<User> user = userRepository.findUserByEmail(userLoginDTO.getEmail());
            if(user.isPresent()){
                Hash hash = new Hash();
                if(hash.isTheSame(userLoginDTO.getPassword() , user.get().getPassword())){

                    // do the login stuff as usual
                    UserSecurityDetails userSecurityDetails = (UserSecurityDetails) userSecurityDetailsService.loadUserByUsername(userLoginDTO.getEmail());
                    List<GrantedAuthority> grantedAuthorities = userSecurityDetails.grantedAuthorities;
                    UserAuthority userAuthority = (UserAuthority) grantedAuthorities.get(0);
                    String role = userAuthority.getAuthority();
                    //updating the last login information
                    User userObject = user.get();
                    userObject.setLastLogin(new Date());
                    userRepository.save(userObject);
                    // Todo Add the last login parameter to the table and update it here to keep track of
                    //  the login userSecurityDetails
                    String token = jwtUtils.createToken(user.get().getUser_id(), userLoginDTO.getEmail() , role);
                    return ResponseEntity.ok().body(new ApiResponse(true , "Success in login" , new LoginResponse(token , user.map(userDTOMapper).get())));
                    }else{
                    return ResponseEntity.status(401).body(new ApiResponse(false , "Failed to Login" , new LoginFailedException("Incorrect Email or password").getMessage()));
                }
            }else{
                return ResponseEntity.status(401).body(new ApiResponse(false , "Failed to login" , new LoginFailedException("User does not exist!!").getMessage()));
            }
        }



    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse> updateUserRole(UpdateRoleDTO updateRoleDTO) throws Exception{
        if(userRepository.existsById(updateRoleDTO.getUserId())){
            Optional<User> user = userRepository.findById(updateRoleDTO.getUserId());
            if(roleRepository.existsById(updateRoleDTO.getRoleId())){
                Role role = roleRepository.findById(updateRoleDTO.getRoleId()).get();
                user.get().setRole(role);
                return ResponseEntity.ok().body(new ApiResponse(
                        true,
                        "Successfully updated the user role",
                        user.map(userDTOMapper)
                ));
            }else{
                return ResponseEntity.status(404).body(new ApiResponse(
                        false,
                        "The role with the id:" + updateRoleDTO.getRoleId() + " does not exist try 1,2,3,4"
                ));
            }
        }else{
            return ResponseEntity.status(404).body(new ApiResponse(
                    false,
                    "The user with the id:" + updateRoleDTO.getUserId() + " does not exist"
            ));
        }
    }

    public ResponseEntity<ApiResponse> updatePassword(Long user_id , String newPassword) throws Exception{
        try {
            Optional<User> optionalUser = userRepository.findById(user_id);
            if(optionalUser.isPresent()){
                Hash hash = new Hash();
                String hashedPassword = hash.hashPassword(newPassword);
                optionalUser.get().setPassword(hashedPassword);
                return ResponseEntity.status(200).body(new ApiResponse(
                        true,
                        "Password was reset successfully"
                ));
            }else{
                return ResponseEntity.status(404).body(new ApiResponse(
                        false,
                        "The user with the id:" + user_id + " does not exist"
                ));
            }
        }catch (RuntimeException e){
            return ResponseEntity.status(500).body(new ApiResponse(
                    false,
                "Failed!!"
            ));
        }
    }

}


