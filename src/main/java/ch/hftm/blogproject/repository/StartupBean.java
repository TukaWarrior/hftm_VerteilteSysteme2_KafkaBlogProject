// package ch.hftm.blogproject.repository;

// import ch.hftm.blogproject.control.AccountService;
// import ch.hftm.blogproject.control.BlogPostService;
// import ch.hftm.blogproject.model.dto.BlogPostDTO;
// import ch.hftm.blogproject.model.dto.CommentDTO;
// import ch.hftm.blogproject.model.entity.Account;
// import ch.hftm.blogproject.model.entity.BlogPost;
// import jakarta.annotation.PostConstruct;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;
// import io.quarkus.logging.Log;
// import java.time.ZonedDateTime;
// import io.quarkus.runtime.Startup;

// // This class serves testing purposes. It adds entities to the database upon quarkus startup / refresh. 
// // This makes testing GET requests easier. 

// @ApplicationScoped
// @Startup 
// public class StartupBean {

//     @Inject
//     BlogPostService blogPostService;
//     @Inject
//     AccountService accountService;
//     // @Inject
//     // CommentService commentService;

//     @PostConstruct
//     public void init() {
//         Log.info("Initializing database with example Accounts");
//         Account acc1 = accountService.addAccount(new Account(null, "Alice", "alice@email.com", "admin", ZonedDateTime.now()));
//         Account acc2 = accountService.addAccount(new Account(null, "Brian", "brian@email.com", "moderator", ZonedDateTime.now()));
//         Account acc3 = accountService.addAccount(new Account(null, "Carl", "carl@email.com", "user", ZonedDateTime.now()));

//         Log.info("Initializing database with example BlogPosts");
//         // BlogPost blogPost1 = blogPostService.addBlogPost(new BlogPostDTO("Initial BlogPost 1", "Content of initial BlogPost 1"), acc1.getId());
//         // BlogPost blogPost2 = blogPostService.addBlogPost(new BlogPostDTO("Initial BlogPost 2", "Content of initial BlogPost 2"), acc2.getId());
//         // BlogPost blogPost3 = blogPostService.addBlogPost(new BlogPostDTO("Initial BlogPost 3", "Content of initial BlogPost 3"), acc3.getId());

//         Log.info("Initializing database with example Comments");
//         // commentService.addComment(new CommentDTO("Comment 1 on BlogPost 1"), blogPost1.getId(), acc1.getId());
//         // commentService.addComment(new CommentDTO("Comment 2 on BlogPost 1"), blogPost1.getId(), acc2.getId());
//         // commentService.addComment(new CommentDTO("Comment 3 on BlogPost 1"), blogPost1.getId(), acc3.getId());
//         // commentService.addComment(new CommentDTO("Comment 1 on BlogPost 2"), blogPost2.getId(), acc1.getId());
//         // commentService.addComment(new CommentDTO("Comment 2 on BlogPost 2"), blogPost2.getId(), acc2.getId());
//         // commentService.addComment(new CommentDTO("Comment 3 on BlogPost 2"), blogPost2.getId(), acc3.getId());
//         // commentService.addComment(new CommentDTO("Comment 1 on BlogPost 3"), blogPost3.getId(), acc1.getId());
//         // commentService.addComment(new CommentDTO("Comment 2 on BlogPost 3"), blogPost3.getId(), acc2.getId());
//         // commentService.addComment(new CommentDTO("Comment 3 on BlogPost 3"), blogPost3.getId(), acc3.getId());

//         Log.info("Example Accounts, BlogPosts, Comments created.");
//     }
// }