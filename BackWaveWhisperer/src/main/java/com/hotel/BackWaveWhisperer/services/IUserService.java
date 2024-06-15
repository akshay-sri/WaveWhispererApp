package com.hotel.BackWaveWhisperer.services;

import com.hotel.BackWaveWhisperer.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    void registerUser(MultipartFile photo, String firstName, String lastName, String email, String password) throws IOException, SQLException;

    List<User> getUsers();

    void deleteUser(String email);

    Optional<User> getUser(String email);
}
