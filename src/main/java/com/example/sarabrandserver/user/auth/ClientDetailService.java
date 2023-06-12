package com.example.sarabrandserver.user.auth;

import com.example.sarabrandserver.user.repository.ClientRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "clientDetailService")
public class ClientDetailService implements UserDetailsService {

    private final ClientRepo clientRepo;

    public ClientDetailService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.clientRepo.findByPrincipal(username).map(ClientzDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

}