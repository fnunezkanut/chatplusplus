package ie.nuigalway.sd3.entities;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String pass;
    private Date dt_created;
    private boolean is_support;

    //constructors
    public User() {
    }
    //constructors


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDt_created() {
        return dt_created;
    }

    public void setDt_created(Date dt_created) {
        this.dt_created = dt_created;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean getIsSupport() {
        return is_support;
    }

    public void setIsSupport(boolean is_support) {
        this.is_support = is_support;
    }
    //getters and setters


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User thread = (User) o;

        return getId().equals(thread.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
