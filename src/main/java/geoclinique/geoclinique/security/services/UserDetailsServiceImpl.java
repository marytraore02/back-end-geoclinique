package geoclinique.geoclinique.security.services;

import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Utilisateur user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  // Cette méthode est utilisée par JWTAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(Long userId){
    Utilisateur user = this.userRepository.findById(userId).orElseThrow(
            () -> new UsernameNotFoundException("User not found with id : " + userId)
    );
    return UserDetailsImpl.build(user);
  }

}
