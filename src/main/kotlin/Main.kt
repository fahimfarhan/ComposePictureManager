import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.App


fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    state = rememberWindowState(size = DpSize(1600.dp, 800.dp))
  ) {
    val mApp = App()
    mApp.appHome()
  }
}
