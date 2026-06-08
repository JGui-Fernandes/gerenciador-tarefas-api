package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.auth.LoginRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.ListUserResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.auth.LoginResponse;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.InvalidCredentialsException;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import br.com.unisinos.gerenciador_tarefas.config.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            TokenBlacklistService tokenBlacklistService
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmailAndIsActiveTrue(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha inválidos");
        }

        String token = jwtUtil.generateToken(user);
        ListUserResponse userResponse = new ListUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return new LoginResponse(token, userResponse);
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }
}
