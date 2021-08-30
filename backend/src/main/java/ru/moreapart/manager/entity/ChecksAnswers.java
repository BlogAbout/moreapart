package ru.moreapart.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checks_answers")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Long.class
)
public class ChecksAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_id")
    private Long check;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "date_created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    @Lob
    @Column(name = "answers")
    private String answers;

    @Column(name = "advanced")
    private Long advancedId;

    @Column(name = "comment", length = 256)
    private String comment;

    @Transient
    private Checks checksInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCheck() {
        return check;
    }

    public void setCheck(Long check) {
        this.check = check;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Long getAdvancedId() {
        return advancedId;
    }

    public void setAdvancedId(Long advancedId) {
        this.advancedId = advancedId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Checks getChecksInfo() {
        return checksInfo;
    }

    public void setChecksInfo(Checks checksInfo) {
        this.checksInfo = checksInfo;
    }

    @Override
    public String toString() {
        return "ChecksAnswers{" +
                "id=" + id +
                ", check=" + check +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", dateCreated=" + dateCreated +
                ", answers='" + answers + '\'' +
                ", advancedId=" + advancedId +
                ", comment=" + comment +
                '}';
    }
}