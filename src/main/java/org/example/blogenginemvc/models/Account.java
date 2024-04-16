package org.example.blogenginemvc.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;
    
    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @OneToMany(mappedBy = "account")
    private List<Comment> comments;

    @Column(name = "authority")
    private String authority;

    @ManyToMany(mappedBy = "followers")
    private Set<Account> followings;

    @ManyToMany
    @JoinTable(name = "follow", joinColumns = @JoinColumn(name = "following_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<Account> followers;

    public Account() {}

    public Account(Long id, String firstName, String lastName, String email, String password, String salt, List<Post> posts, List<Comment> comments, String authority, Set<Account> followings, Set<Account> followers) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.posts = posts;
        this.authority = authority;
        this.comments = comments;
        this.followings = followings;
        this.followers = followers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<Account> getFollowings() {
        return followings;
    }

    public void addFollowings(Account following) {
        this.followings.add(following);
    }

    public void removeFollowings(Account following) {
        this.followings.remove(following);
    }

    public Set<Account> getFollowers() {
        return followers;
    }

    public void addFollowers(Account follower) {
        this.followers.add(follower);
    }

    public void removeFollowers(Account follower) {
        this.followers.remove(follower);
    }
}