package com.seocodes.spring_security.controller;

import com.seocodes.spring_security.controller.dto.CreateUserDTO;
import com.seocodes.spring_security.entities.Role;
import com.seocodes.spring_security.entities.User;
import com.seocodes.spring_security.repository.RoleRepository;
import com.seocodes.spring_security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

//NUMA BOA PRÁTICA, PARTE DISSO PODIA ESTAR NUMA SERVICE, MAS DEIXA ASSIM PARA SIMPLIFICAR
    @Transactional
    @PostMapping("/users")
        public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO dto){

            var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
            var userFromDb = userRepository.findByUsername(dto.username());

            if(userFromDb.isPresent()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            var user = new User();
            user.setUsername(dto.username());
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setRoles(Set.of(basicRole));

            userRepository.save(user);

            return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")  // Autorização - só admins podem fazer essa requisição
    public ResponseEntity<List<User>> listUsers(){
        var users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }
}
