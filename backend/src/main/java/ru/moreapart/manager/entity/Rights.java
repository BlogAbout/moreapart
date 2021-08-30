package ru.moreapart.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "rights")
public class Rights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date_created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    @Column(name = "date_modify")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModify;

    @Column(name = "count_persons")
    private int countPersons;

    @Column(name = "granted")
    private String granted;

    @ElementCollection
    private Set<Long> siblings;

    @Transient
    @ManyToMany(mappedBy = "users")
    private Set<User> users;

    public Rights() { }

    public Rights(Map<String, String> data) {
        Set<Long> siblings = new HashSet<>();

        if (data.containsKey("siblings") && data.get("siblings").trim().length() > 2) {
            String strSiblings = data.get("siblings");
            String[] arrSiblings = strSiblings.substring(1, strSiblings.length() - 1).split(",");
            for (String s : arrSiblings) {
                siblings.add(Long.parseLong(s));
            }
        }

        setTitle(data.get("title"));
        setDescription(data.get("description"));
        setGranted(data.get("granted"));
        setSiblings(siblings);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException();

        this.title = title.substring(0, Math.min(title.length(), 100));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException();

        this.description = description.substring(0, Math.min(description.length(), 256));
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateModify() {
        return dateModify;
    }

    public void setDateModify(LocalDateTime dateModify) {
        this.dateModify = dateModify;
    }

    public int getCountPersons() {
        return countPersons;
    }

    public void setCountPersons(int countPersons) {
        this.countPersons = countPersons;
    }

    public String getGranted() {
        return granted;
    }

    public void setGranted(String granted) {
        this.granted = granted;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Long> getSiblings() {
        return siblings;
    }

    public void setSiblings(Set<Long> siblings) {
        this.siblings = siblings;
    }

    @Override
    public String toString() {
        return "Rights{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", dateCreated=" + dateCreated +
                ", countPersons=" + countPersons +
                ", granted='" + granted + '\'' +
                ", siblings=" + siblings +
                ", users=" + users +
                '}';
    }
}