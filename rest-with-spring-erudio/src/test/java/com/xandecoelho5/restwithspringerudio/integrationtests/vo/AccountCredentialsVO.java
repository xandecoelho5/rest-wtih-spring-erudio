package com.xandecoelho5.restwithspringerudio.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement
public class AccountCredentialsVO {

    private String username;
    private String password;

    public AccountCredentialsVO() {
    }

    public AccountCredentialsVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountCredentialsVO that = (AccountCredentialsVO) o;

        if (!Objects.equals(username, that.username)) return false;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
