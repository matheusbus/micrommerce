package com.esw.authservice.repository;

import com.esw.authservice.model.User;
import com.esw.authservice.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve salvar e recuperar o usuário por e-mail")
    void testSaveAndFindByEmail() {
        User user = new User(null, "matheusb", "matheus@email.com", "123456", "ADMIN", UserType.BUYER, true, new Date());
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("matheus@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("matheusb");
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar e-mail inexistente")
    void testFindByEmail_notFound() {
        Optional<User> result = userRepository.findByEmail("naoexiste@email.com");

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("Deve verificar se e-mail existe")
    void testExistsByEmail() {
        User user = new User(null, "joaosilva", "joao@email.com", "senha", "USER", UserType.BUYER, true, new Date());
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("joao@email.com");
        boolean notExists = userRepository.existsByEmail("fake@email.com");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
