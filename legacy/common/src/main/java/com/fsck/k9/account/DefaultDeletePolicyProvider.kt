package com.fsck.k9.account

import app.k9mail.core.common.mail.Protocols
import app.k9mail.legacy.account.Account.DeletePolicy

class DefaultDeletePolicyProvider : DeletePolicyProvider {
    override fun getDeletePolicy(accountType: String): DeletePolicy {
        return when (accountType) {
            Protocols.IMAP -> DeletePolicy.ON_DELETE
            Protocols.POP3 -> DeletePolicy.NEVER
            "demo" -> DeletePolicy.ON_DELETE
            else -> throw AssertionError("Unhandled case: $accountType")
        }
    }
}
