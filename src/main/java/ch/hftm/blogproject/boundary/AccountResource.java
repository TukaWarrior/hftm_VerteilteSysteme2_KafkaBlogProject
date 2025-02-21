// package ch.hftm.blogproject.boundary;

// import java.util.List;

// import org.eclipse.microprofile.openapi.annotations.Operation;
// import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
// import org.eclipse.microprofile.openapi.annotations.tags.Tag;
// import org.jboss.resteasy.reactive.RestPath;
// import java.util.stream.Collectors;
// import ch.hftm.blogproject.control.AccountService;
// import ch.hftm.blogproject.model.dto.AccountDTO;
// import ch.hftm.blogproject.model.entity.Account;
// import jakarta.annotation.security.DenyAll;
// import jakarta.annotation.security.PermitAll;
// import jakarta.annotation.security.RolesAllowed;
// import jakarta.inject.Inject;
// import jakarta.ws.rs.Consumes;
// import jakarta.ws.rs.DELETE;
// import jakarta.ws.rs.GET;
// import jakarta.ws.rs.POST;
// import jakarta.ws.rs.PUT;
// import jakarta.ws.rs.Path;
// import jakarta.ws.rs.PathParam;
// import jakarta.ws.rs.Produces;
// import jakarta.ws.rs.core.Context;
// import jakarta.ws.rs.core.Response;
// import jakarta.ws.rs.core.Response.Status;
// import jakarta.ws.rs.core.UriInfo;
// import jakarta.ws.rs.core.MediaType;

// @Path("/account")
// @Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
// @Tag(name = "Account Resource", description = "Account Management API")
// @DenyAll
// public class AccountResource {

//     @Inject
//     AccountService accountService;
    
//     @GET
//     @PermitAll
//     @Operation(summary = "Get all accounts", description = "Returns all accounts")
//     public List<AccountDTO> getAllAccounts() {
//         List<Account> accounts = accountService.getAllAccounts();
//         return accounts.stream().map(AccountDTO::new).collect(Collectors.toList());
//     }

//     @GET
//     @Path("/{id}")
//     @PermitAll
//     @Operation(summary = "Get an account by ID", description = "Returns an account by their ID")
//     public Response getAccountById(@PathParam("id") Long id) {
//         if (id == null) {
//             return Response.status(Status.BAD_REQUEST).build();
//         }
//         Account account = accountService.getAccountById(id);
//         if (account == null) {
//             return Response.status(Status.NOT_FOUND).build();
//         }
//         AccountDTO accountDTO = new AccountDTO(account);
//         return Response.status(Status.OK).entity(accountDTO).build();
//     }

//     @POST
//     @RolesAllowed("admin")
//     @Operation(summary = "Add a new account", description = "Creates a new account")
//     public Response addAccount(AccountDTO accountDTO) {
//         Account account = accountDTO.toEntity();
//         accountService.addAccount(account);
//         return Response.status(Response.Status.CREATED).build();
//     }


//     @PUT
//     @Path("/{id}")
//     @RolesAllowed("admin")
//     @Operation(summary = "Update an account", description = "Updates an existing account")
//     public Response updateAccount(@PathParam("id") Long id, AccountDTO  accountDTO) {
//         if (id == null) {
//             return Response.status(Status.BAD_REQUEST).build();
//         }
//         Account account = accountDTO.toEntity();
//         accountService.updateAccount(id, account);
//         return Response.ok().build();
//     }

//     @DELETE
//     @RolesAllowed("admin")
//     @Path("/{id}")
//     @Operation(summary = "Delete an account", description = "Deletes an account by their ID")
//     public Response deleteAccount(@PathParam("id") Long id) {
//         if (id == null) {
//             return Response.status(Status.BAD_REQUEST).build();
//         }
//         accountService.deleteAccount(id);
//         return Response.noContent().build();
//     }
// }
