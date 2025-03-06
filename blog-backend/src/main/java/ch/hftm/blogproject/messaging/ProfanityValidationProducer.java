// package ch.hftm.blogproject.messaging;

// import org.eclipse.microprofile.reactive.messaging.Channel;
// import org.eclipse.microprofile.reactive.messaging.Emitter;

// import io.smallrye.mutiny.Uni;
// import io.smallrye.mutiny.infrastructure.Infrastructure;
// import  io.vertx.core.json.JsonObject ;
// import jakarta.enterprise.context.ApplicationScoped;

// @ApplicationScoped
// public class ProfanityValidationProducer {

//     @Channel("profanity-validation-out")
//     Emitter<String> emitter;

//     public Uni<Void> sendTextForValidation(String text) {
//         return Uni.createFrom().item(() -> 
//                 new JsonObject().put("text", text).toString()
//             )
//             .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
//             .invoke(message -> emitter.send(message))
//             .replaceWithVoid();
//     }
// }