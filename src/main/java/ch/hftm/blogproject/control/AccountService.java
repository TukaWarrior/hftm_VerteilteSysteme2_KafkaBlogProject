// package ch.hftm.blogproject.control;

// import ch.hftm.blogproject.model.entity.Account;
// import ch.hftm.blogproject.repository.AccountRepository;

// import java.util.List;

// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;

// @ApplicationScoped
// public class AccountService {

//     @Inject
//     AccountRepository accountRepository;

//     public List<Account> getAllAccounts() {
//         return accountRepository.listAll();
//     }

//     public Account getAccountById(Long id) {
//         return accountRepository.findById(id);
//     }

//     @Transactional
//     public Account addAccount(Account account) {
//         accountRepository.persist(account);
//         return account;
//     }

//     @Transactional
//     public void updateAccount(Long id, Account account) {
//         Account existingAccount = accountRepository.findById(id);
//         if (existingAccount != null) {
//             existingAccount.setName(account.getName());
//             existingAccount.setEmail(account.getEmail());
//             accountRepository.persist(existingAccount);
//         }
//     }

//     @Transactional
//     public void deleteAccount(Long id) {
//         Account account = accountRepository.findById(id);
//         if (account != null) {
//             accountRepository.delete(account);
//         }
//     }
// }