package bigbank;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

@Qualifier("bankServiceScenario1Impl")
public class BankServiceScenario1Impl implements BankServiceScenario1 {
    private BankDao bankDao;

    // Not used unless you declare a <protect-pointcut>
    @Pointcut("execution(* bigbank.BankServiceImpl.*(..))")
    public void myPointcut() {}

    public BankServiceScenario1Impl(BankDao bankDao) {
        Assert.notNull(bankDao);
        this.bankDao = bankDao;
    }

    public Account[] findAccounts() {
        return this.bankDao.findAccounts();
    }

    public Account post(Account account, double amount) {
        Assert.notNull(account);

        // We read account bank from DAO so it reflects the latest balance
        Account a = bankDao.readAccount(account.getId());
        if (account == null) {
            throw new IllegalArgumentException("Couldn't find requested account");
        }

        a.setBalance(a.getBalance() + amount);
        bankDao.createOrUpdateAccount(a);
        return a;
    }

    public Account readAccount(Long id) {
        return bankDao.readAccount(id);
    }
}
