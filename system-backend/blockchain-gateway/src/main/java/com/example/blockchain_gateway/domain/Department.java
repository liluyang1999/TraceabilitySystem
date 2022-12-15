package com.example.blockchain_gateway.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Each link of medical supply chain is based on department
 * Each department corresponds to one role
 * Table: ss_department
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Department extends BaseEntity {

    protected Long id;

    protected String name;

    protected String type;

    protected String leader;

    protected String email;

    protected String address;

    @Size(max = 30, message = "The department name should be within 20 characters")
    @NotBlank(message = "The department name can't be blank")
    public String getName() {
        return name;
    }

    @Size(max = 30, message = "The leader name should be within 20 characters")
    @NotBlank(message = "The department leader can't be blank")
    public String getLeader() {
        return leader;
    }

    @Size(max = 30, message = "The length of email should be within 20 characters")
    @Email(message = "The format of email is not correct")
    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leader='" + leader + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

