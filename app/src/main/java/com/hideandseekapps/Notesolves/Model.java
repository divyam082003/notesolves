package com.hideandseekapps.Notesolves;

public class Model {

  public String phone;
  public String email;
  public String name;
  public String college;
  public Boolean active;

    public Model() {
    }

    public Model(String email, String name, String phone,String college) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
