package com.ElectricityAutomationInitiative.controller;
import com.ElectricityAutomationInitiative.payload.AdminLoginDTO;
import com.ElectricityAutomationInitiative.payload.JWTAuthResponse;
import com.ElectricityAutomationInitiative.payload.LoginDTO;
import com.ElectricityAutomationInitiative.security.JwtRequestFilter;
import com.ElectricityAutomationInitiative.security.JwtTokenUtil;
import com.ElectricityAutomationInitiative.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
            authenticate(loginDTO.getEmailOrConnectionId(), loginDTO.getPassword());

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmailOrConnectionId());

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JWTAuthResponse(token,userDetails.getAuthorities()));
        } catch (BadCredentialsException e) {
            // Handle incorrect credentials case
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> createAdmin(@RequestBody LoginDTO adminLoginDTO) throws Exception {
        try {
        authenticate(adminLoginDTO.getEmailOrConnectionId(), adminLoginDTO.getPassword());
        System.out.println("Test Runs ");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(adminLoginDTO.getEmailOrConnectionId());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JWTAuthResponse(token,userDetails.getAuthorities()));
        } catch (BadCredentialsException e) {
            // Handle incorrect credentials case
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}


