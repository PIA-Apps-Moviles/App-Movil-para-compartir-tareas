package com.jahir.pia_appsmov.Model.datosUser;

public class Usuario {

    String UserName, Correo, Password;

    public Usuario(){

    }
    public Usuario(String userName, String correo, String password) {
        UserName = userName;
        Correo = correo;
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
