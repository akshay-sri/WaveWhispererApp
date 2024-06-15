package com.hotel.BackWaveWhisperer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String photo;

    public UserResponse(Long id, String firstName, String lastName, String email, byte[] photoBytes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
    }
}
