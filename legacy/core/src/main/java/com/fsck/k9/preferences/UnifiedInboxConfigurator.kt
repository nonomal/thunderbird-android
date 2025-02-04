package com.fsck.k9.preferences

import app.k9mail.legacy.account.AccountManager
import com.fsck.k9.K9

/**
 * Configures the unified inbox after an account has been added.
 */
class UnifiedInboxConfigurator(
    private val accountManager: AccountManager,
) {
    fun configureUnifiedInbox() {
        if (accountManager.getAccounts().size > 1) {
            K9.isShowUnifiedInbox = true
            K9.saveSettingsAsync()
        }
    }
}
