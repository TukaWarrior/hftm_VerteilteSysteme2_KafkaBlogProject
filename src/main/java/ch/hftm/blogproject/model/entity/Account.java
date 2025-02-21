// package ch.hftm.blogproject.model.entity;

// import java.time.ZonedDateTime;
// import java.util.List;

// import jakarta.json.bind.annotation.JsonbTransient;
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Account {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     private String name;
//     private String email;
//     private String role;
//     private ZonedDateTime createdAt;


//     // @JsonbTransient // Ignored for JSON serialization because account links to blogPost,blogPost links to account. Loop!
//     // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//     // private List<BlogPost> blogPosts;

//     // @JsonbTransient // Ignored for JSON serialization because account links to blogPost,blogPost links to account. Loop!
//     // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//     // private List<Comment> comments;

//     public Account(String name, String email, String role) {
//         this.name = name;
//         this.email = email;
//         this.role = role;
//     }
// }