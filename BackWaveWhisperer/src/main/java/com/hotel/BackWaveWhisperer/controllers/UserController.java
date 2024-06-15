package com.hotel.BackWaveWhisperer.controllers;

import com.hotel.BackWaveWhisperer.exceptions.PhotoRetrievalException;
import com.hotel.BackWaveWhisperer.exceptions.ResourceNotFoundException;
import com.hotel.BackWaveWhisperer.exceptions.UserNotFoundException;
import com.hotel.BackWaveWhisperer.models.User;
import com.hotel.BackWaveWhisperer.response.RoomResponse;
import com.hotel.BackWaveWhisperer.response.UserResponse;
import com.hotel.BackWaveWhisperer.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<User> users = userService.getUsers();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = getUserResponse(user);
            userResponses.add(userResponse);
        }
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
            Optional<User> user = userService.getUser(email);
            return user.map(theUser -> {
                UserResponse userResponse = getUserResponse(theUser);
                return ResponseEntity.ok(Optional.of(userResponse));
            }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    private UserResponse getUserResponse(User theUser) {
        byte[] photoBytes = null;
        Blob photoBlob = theUser.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        UserResponse userResponse = new UserResponse(theUser.getId(),
                theUser.getFirstName(),
                theUser.getLastName(),
                theUser.getEmail(),photoBytes);
        return userResponse;
    }
}
