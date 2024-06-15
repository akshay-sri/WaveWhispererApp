package com.hotel.BackWaveWhisperer.services;

import com.hotel.BackWaveWhisperer.exceptions.AlreadyExistsException;
import com.hotel.BackWaveWhisperer.models.Role;
import com.hotel.BackWaveWhisperer.models.User;
import com.hotel.BackWaveWhisperer.repositories.RoleRepository;
import com.hotel.BackWaveWhisperer.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(MultipartFile file, String firstName, String lastName, String email, String password) throws IOException, SQLException {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(email + " already exists");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes); //converts bytes to blob
            user.setPhoto(photoBlob);
        }
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        Optional<User> user = getUser(email);
        if (user.isPresent()) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public Optional<User> getUser(String email) {
        return Optional.of(userRepository.findByEmail(email).get());
    }
}
