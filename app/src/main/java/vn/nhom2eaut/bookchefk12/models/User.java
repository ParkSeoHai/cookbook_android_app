package vn.nhom2eaut.bookchefk12.models;

public class User {
    private String userId;
    private String name_display;
    private String username;
    private String email;
    private String password;
    private String profile_picture;
    private String bio; // Mô tả ngắn

    public User() {}

    public User(String userId, String name_display, String username,
                String email, String password, String profile_picture,
                String bio) {
        this.userId = userId;
        this.name_display = name_display;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.bio = bio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName_display() {
        return name_display;
    }

    public void setName_display(String name_display) {
        this.name_display = name_display;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
