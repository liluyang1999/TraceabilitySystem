package com.example.blockchain_gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserContext implements User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String account;

    private String affiliation;

    private Enrollment enrollment;

    private String mspId;

    private Set<String> roles;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMspId() {
        return mspId;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }
}
