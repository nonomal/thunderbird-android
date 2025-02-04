package app.k9mail.feature.navigation.drawer

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.k9mail.core.ui.theme.api.FeatureThemeProvider
import app.k9mail.feature.navigation.drawer.domain.entity.DisplayUnifiedFolderType
import app.k9mail.feature.navigation.drawer.domain.entity.createDisplayAccountFolderId
import app.k9mail.feature.navigation.drawer.ui.DrawerView
import app.k9mail.legacy.account.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal data class FolderDrawerState(
    val selectedAccountUuid: String? = null,
    val selectedFolderId: String? = null,
)

class FolderDrawer(
    override val parent: AppCompatActivity,
    private val openAccount: (accountId: String) -> Unit,
    private val openFolder: (folderId: Long) -> Unit,
    private val openUnifiedFolder: () -> Unit,
    private val openManageFolders: () -> Unit,
    private val openSettings: () -> Unit,
    createDrawerListener: () -> DrawerLayout.DrawerListener,
) : NavigationDrawer, KoinComponent {

    private val themeProvider: FeatureThemeProvider by inject()

    private val drawer: DrawerLayout = parent.findViewById(R.id.navigation_drawer_layout)
    private val drawerContent: ComposeView = parent.findViewById(R.id.navigation_drawer_content)

    private val drawerState = MutableStateFlow(FolderDrawerState())

    init {
        drawer.addDrawerListener(createDrawerListener())

        drawerContent.setContent {
            themeProvider.WithTheme {
                val state = drawerState.collectAsStateWithLifecycle()

                DrawerView(
                    drawerState = state.value,
                    openAccount = openAccount,
                    openFolder = openFolder,
                    openUnifiedFolder = openUnifiedFolder,
                    openManageFolders = openManageFolders,
                    openSettings = openSettings,
                    closeDrawer = { close() },
                )
            }
        }
    }

    override val isOpen: Boolean
        get() = drawer.isOpen

    override fun updateUserAccountsAndFolders(account: Account?) {
        // no-op
    }

    override fun selectAccount(accountUuid: String) {
        drawerState.update {
            it.copy(selectedAccountUuid = accountUuid)
        }
    }

    override fun selectFolder(accountUuid: String, folderId: Long) {
        drawerState.update {
            it.copy(
                selectedAccountUuid = accountUuid,
                selectedFolderId = createDisplayAccountFolderId(accountUuid, folderId),
            )
        }
    }

    override fun selectUnifiedInbox() {
        drawerState.update {
            it.copy(
                selectedFolderId = DisplayUnifiedFolderType.INBOX.id,
            )
        }
    }

    override fun deselect() {
        drawerState.update {
            it.copy(
                selectedFolderId = null,
            )
        }
    }

    override fun open() {
        drawer.openDrawer(GravityCompat.START)
    }

    override fun close() {
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun lock() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlock() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}
