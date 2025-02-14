package com.reactnativekeyboardcontroller.managers

import android.util.Log
import androidx.appcompat.widget.FitWindowsLinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativekeyboardcontroller.KeyboardAnimationCallback
import com.reactnativekeyboardcontroller.R
import com.reactnativekeyboardcontroller.extensions.requestApplyInsetsWhenAttached
import com.reactnativekeyboardcontroller.views.EdgeToEdgeReactViewGroup

class KeyboardControllerViewManagerImpl(private val mReactContext: ReactApplicationContext) {
  private val TAG = KeyboardControllerViewManagerImpl::class.qualifiedName
  private var isStatusBarTranslucent = false
  private var isNavigationBarTranslucent = false

  fun createViewInstance(reactContext: ThemedReactContext): ReactViewGroup {
    val view = EdgeToEdgeReactViewGroup(reactContext)
    val activity = reactContext.currentActivity

    if (activity == null) {
      Log.w(TAG, "Can not setup keyboard animation listener, since `currentActivity` is null")
      return view
    }

    val callback = KeyboardAnimationCallback(
      view = view,
      persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
      deferredInsetTypes = WindowInsetsCompat.Type.ime(),
      // We explicitly allow dispatch to continue down to binding.messageHolder's
      // child views, so that step 2.5 below receives the call
      dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE,
      context = reactContext,
      onApplyWindowInsetsListener = { v, insets ->
        val content =
          reactContext.currentActivity?.window?.decorView?.rootView?.findViewById<FitWindowsLinearLayout>(
            R.id.action_bar_root,
          )
        content?.setPadding(
          0,
          if (this.isStatusBarTranslucent) 0 else insets?.getInsets(WindowInsetsCompat.Type.systemBars())?.top ?: 0,
          0,
          if (this.isNavigationBarTranslucent) 0 else insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0,
        )

        insets
      },
    )
    ViewCompat.setWindowInsetsAnimationCallback(view, callback)
    ViewCompat.setOnApplyWindowInsetsListener(view, callback)
    view.requestApplyInsetsWhenAttached()

    return view
  }

  fun setStatusBarTranslucent(view: ReactViewGroup, isStatusBarTranslucent: Boolean) {
    this.isStatusBarTranslucent = isStatusBarTranslucent
  }

  fun setNavigationBarTranslucent(view: ReactViewGroup, isNavigationBarTranslucent: Boolean) {
    this.isNavigationBarTranslucent = isNavigationBarTranslucent
  }

  fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    val map: MutableMap<String, Any> = MapBuilder.of(
      "topKeyboardMove",
      MapBuilder.of("registrationName", "onKeyboardMove"),
      "topKeyboardMoveStart",
      MapBuilder.of("registrationName", "onKeyboardMoveStart"),
      "topKeyboardMoveEnd",
      MapBuilder.of("registrationName", "onKeyboardMoveEnd"),
      "topKeyboardMoveInteractive",
      MapBuilder.of("registrationName", "onKeyboardMoveInteractive"),
    )

    return map
  }

  companion object {
    const val NAME = "KeyboardControllerView"
  }
}
