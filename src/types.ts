import type { NativeSyntheticEvent, ViewProps } from 'react-native';

// DirectEventHandler events declaration

export type NativeEvent = {
  progress: number;
  height: number;
};
export type EventWithName<T> = {
  eventName: string;
} & T;

// native View/Module declarations

export type KeyboardControllerProps = {
  onKeyboardMoveStart?: (
    e: NativeSyntheticEvent<EventWithName<NativeEvent>>
  ) => void;
  onKeyboardMove?: (
    e: NativeSyntheticEvent<EventWithName<NativeEvent>>
  ) => void;
  onKeyboardMoveEnd?: (
    e: NativeSyntheticEvent<EventWithName<NativeEvent>>
  ) => void;
  onKeyboardMoveInteractive?: (
    e: NativeSyntheticEvent<EventWithName<NativeEvent>>
  ) => void;
  // fake prop used to activate reanimated bindings
  onKeyboardMoveReanimated?: (
    e: NativeSyntheticEvent<EventWithName<NativeEvent>>
  ) => void;
  statusBarTranslucent?: boolean;
  navigationBarTranslucent?: boolean;
} & ViewProps;

export type KeyboardGestureAreaProps = {
  interpolator: 'ios' | 'linear';
  /**
   * Whether to allow to show a keyboard from dismissed state by swipe up.
   * Default to `false`.
   */
  showOnSwipeUp?: boolean;
  /**
   * Whether to allow to control a keyboard by gestures. The strategy how
   * it should be controlled is determined by `interpolator` property.
   * Defaults to `true`.
   */
  enableSwipeToDismiss?: boolean;
} & ViewProps;

export type KeyboardControllerModule = {
  // android only
  setDefaultMode: () => void;
  setInputMode: (mode: number) => void;
  // native event module stuff
  addListener: (eventName: string) => void;
  removeListeners: (count: number) => void;
};

// Event module declarations

export type KeyboardControllerEvents =
  | 'keyboardWillShow'
  | 'keyboardDidShow'
  | 'keyboardWillHide'
  | 'keyboardDidHide';
export type KeyboardEventData = {
  height: number;
};

// package types

export type Handlers<T> = Record<string, T | undefined>;
export type KeyboardHandler = Partial<{
  onStart: (e: NativeEvent) => void;
  onMove: (e: NativeEvent) => void;
  onEnd: (e: NativeEvent) => void;
  onInteractive: (e: NativeEvent) => void;
}>;
export type KeyboardHandlers = Handlers<KeyboardHandler>;
