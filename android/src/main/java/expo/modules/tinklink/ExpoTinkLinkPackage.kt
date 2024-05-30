package expo.modules.tinklink

import android.content.Context
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener

class ExpoTinkLinkPackage : Package {
    override fun createReactActivityLifecycleListeners(context: Context): List<ReactActivityLifecycleListener> {
        return listOf(ExpoTinkLinkActivityLifecycleListener())
    }
}
