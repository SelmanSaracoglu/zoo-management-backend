package com.zoo.auth;

import com.zoo.security.JwtService;
import com.zoo.user.Role;
import com.zoo.user.UserEntity;
import com.zoo.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth", description = "Kayıt ve giriş işlemleri")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    @Operation(summary = "Yeni kullanici kaydi", security = {}) // Swagger'da JWT isteme
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kayıt başarılı"),
            @ApiResponse(responseCode = "400", description = "Doğrulama hatası"),
            @ApiResponse(responseCode = "409", description = "Kullanıcı zaten var")
    })

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<?> register(@jakarta.validation.Valid @RequestBody RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            return  ResponseEntity.status(409).body("Username already exist");
        }

        UserEntity u = new UserEntity();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setRoles( (req.roles()==null || req.roles().isEmpty()) ? java.util.Set.of(Role.USER) : req.roles() );
        users.save(u);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Giriş yap ve JWT al", security = {}) // Swagger'da JWT isteme
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Giriş başarılı (JWT döner)"),
            @ApiResponse(responseCode = "400", description = "Doğrulama hatası"),
            @ApiResponse(responseCode = "401", description = "Kimlik bilgileri geçersiz")
    })
    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<?> login(@jakarta.validation.Valid @RequestBody LoginRequest req) {
        var userOpt = users.findByUsername(req.username());
        if (userOpt.isEmpty()) return  ResponseEntity.status(401).body("Invalid Credentials");
        var user = userOpt.get();
        if (!encoder.matches(req.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
        var claims = java.util.Map.<String,Object>of("roles",
                user.getRoles().stream().map(Enum::name).toList());
        String token = jwt.generate(user.getUsername(), claims);        // <-- token üret
        return ResponseEntity.ok(new AuthResponse(token));              // <-- token döndür
    }
}
