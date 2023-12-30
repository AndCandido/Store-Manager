package io.github.AndCandido.storemanager.services.restTemplates;

import io.github.AndCandido.storemanager.domain.enums.Role;
import io.github.AndCandido.storemanager.domain.models.User;
import io.github.AndCandido.storemanager.domain.repositories.IUserRepository;
import io.github.AndCandido.storemanager.services.creators.UserCreator;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceTestRestTemplate<T, ID> {

    private final TestRestTemplate restTemplate;
    private final IUserRepository userRepository;

    public ResourceTestRestTemplate(TestRestTemplate restTemplate, IUserRepository userRepository) {
        this.userRepository = userRepository;
        User user = UserCreator.createModel("admin", "12345", Role.ADMIN);
        saveUser(user);
        this.restTemplate = restTemplate.withBasicAuth(user.getUsername(), user.getPassword());
    }

    private void saveUser(User user) {
        var userToSave = UserCreator.createModel(
            user.getUsername(),
            new BCryptPasswordEncoder().encode(user.getPassword()),
            user.getRole()
        );

        userRepository.save(userToSave);
    }

    public ResponseEntity<T> post( String uri, T resource, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(
            uri,
            HttpMethod.POST,
            new HttpEntity<>(resource),
            responseType
        );
    }

    public List<ResponseEntity<T>> postAll(String uri, List<T> resources, ParameterizedTypeReference<T> responseType) {
        List<ResponseEntity<T>> responses = new ArrayList<>(resources.size());

        for (T resource : resources) {
            ResponseEntity<T> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity<>(resource),
                responseType
            );

            responses.add(response);
        }

        return responses;
    }

    public ResponseEntity<Void> delete(String uri, ID id) {
        return restTemplate.exchange(
            uri + "/" + id,
            HttpMethod.DELETE,
            null,
            Void.class
        );
    }

    public ResponseEntity<T> getById(String uri, ID id, ParameterizedTypeReference<T> responseType) {
            return restTemplate.exchange(
            uri + "/" + id,
            HttpMethod.GET,
            null,
            responseType
        );
    }

    public ResponseEntity<List<T>> getAll(String uri, ParameterizedTypeReference<List<T>> responseType) {
        return restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            responseType
        );
    }

    public ResponseEntity<T> put(String uri, ID id, T resourceToUpdate, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return restTemplate.exchange(
            uri + "/" + id,
            HttpMethod.PUT,
            new HttpEntity<>(resourceToUpdate),
            parameterizedTypeReference
        );
    }
}
