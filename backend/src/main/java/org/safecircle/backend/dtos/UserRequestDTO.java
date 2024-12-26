package org.safecircle.backend.dtos;

import java.time.LocalDate;

public class UserRequestDTO {

        private long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phone;
        private LocalDate dateOfBirth;

        public UserRequestDTO(String firstName, String lastName, String email, String password, String phone, LocalDate dateOfBirth)
            {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.password = password;
                this.phone = phone;
                this.dateOfBirth = dateOfBirth;
            }

        public UserRequestDTO(long userId, String firstName, String lastName, String email, String password, String phone, LocalDate dateOfBirth) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.dateOfBirth = dateOfBirth;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }