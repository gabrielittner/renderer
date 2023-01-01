Change Log
==========

## 0.12.0 *(2022-08-12)*

Added compose facilities to make a step by step migration from Renderer to Compose easier
- `Renderer` composable that allows showing a renderer inside compose
- `ComposeRenderer` can be used to create a `Renderer` that is implemented using composables
- `ViewRendererAdapter` supports `ComposeRenderer` for step by step migration of lists

Other changes:
- added `SimpleViewRendererAdapter` for lists that don't use diffing
- `ViewRendererAdapter` and `SimpleViewRendererAdapter` support using plain adapter delegates
- updated dependencies


## 0.11.0 *(2022-01-25)*

- remove `StateMachine` base classes
- remove all `connect` artifacts except for `connect-flow` which is now called `connect`


## 0.10.2 *(2021-10-24)*

- build against updated `StateMachine` interface


## 0.10.1 *(2021-06-18)*

- fix `connect()` method of `connect-flow` not emitting any actions


## 0.10.0 *(2021-06-18)*

- `SimpleStateMachine` and `RxSimpleStateMachine` now return a `StateFlow` and therefore require an `initialState` constructor parameter


## 0.9.0 *(2021-06-16)*

- add new `connect-flow` artifact
  - `connect` method to use Freeletics MAD `StateMachine` with `Renderer`
  - `SimpleStateMachine` implementation that is a `LiveDataStateMachine` like implementation for the above mentioned `StateMachine`
- add new `connect-flow-rx`
  - `RxSimpleStateMachine` implementation that is a `RxLiveDataStateMachine` like implementation for `StateMachine`


## 0.8.0 *(2021-06-10)*

- BREAKING: change `Renderer` to be `Flow` based
    - `actions` now returns a `Flow<Action>`
    - `viewActions` in `ViewRenderer` is still `Observable` based and will be deprecated in a future  release
    - to merge other `Flow<Action>` into `actions` you can call `addActionFlow`
    - `ViewRendererAdapter.actions()` also returns a `Flow<Action>` which can be used with the `addActionFlow` method mentioned above
- `renderer-list`: support `on` and `layoutInflater` parameters from adapter delegates
- removed deprecated `LiveDataViewModel` and `RxLiveDataViewModel` typealiases
- removed base `connect` module (all functionality was moved directly into the main `connect-livedata` artifact)
- removed experimental `renderer-delegation` artifact
- removed deprecated `connect-rx` artifact


## 0.7.0 *(2021-01-29)*

- removed `Factory`, `InflaterFactory` and `LayoutInflaterFactory`
- renamed `ViewBindingFactory` to `Factory`
- changed `ViewBindingFactory` signature from `<V : ViewBinding, State, Action> to <V : ViewBinding, R : ViewRenderer<State, Action>` to enable using Dagger's assisted inject
- `State` and `Action` generics now use `Any` as bound
- removed renderer-delegation module


## 0.6.0 *(2020-09-07)*

- rename all `...ViewModel` classes to `...StateMachine`
- add deprecated typealiases for old names
- add `ViewBinding` constructor overload to `ViewRenderer`


## 0.5.0 *(2020-05-10)*

- `Fragment.connect` is now callable from `onCreateView()`
- make `ViewRenderer.LayoutInflaterFactory` layout id a ctor parameter
- make `ViewRenderer.ViewBindingFactory` ViewBinding creator a ctor parameter
- remove `LayoutInflater` and `attachToRoot: Boolean` from `InflaterFactory`
- allow to map the type of actions in `DelegatingRenderer`


## 0.4.0 *(2020-05-03)*

- change `ViewRenderer.InflaterFactory` to an interface
- make the parent `ViewGroup` passed into `InflaterFactory` nullable
- add `LayoutInflater` and `attachToRoot: Boolean` to `InflaterFactory`
- add `ViewRenderer.LayoutInflaterFactory` which matches the old `ViewRenderer.InflaterFactory`
- add `ViewRenderer.ViewBindingFactory`
- deprecate `RxViewModel` and it's `connect` methods
- fix sources not getting published


## 0.3.0 *(2020-01-17)*

- BREAKING: subclasses of `ViewRenderer` now need to override `viewActions` instead of `actions`
- introduce `sendAction()` in `ViewRenderer` to avoid having to create subjects in implementations
- make `ViewRenderer.state` not nullable
- add `ViewRenderer.InflaterFactory` to tie a `ViewRenderer` to a XML layout
- cleaned up connect API, restructured modules

## 0.2.1 *(2019-10-26)*

- fix the used `LifecycleOwner` used to connect a `Fragment`


## 0.2.0 *(2019-10-20)*

- renamed Binder to Renderer (also includes artifact names)
- removed `connectors` artifact
- new `connect` artifact which has `LiveDataViewModel` as public interface
- new `connect-rx` artifact that contains `RxLiveDataViewModel`
- moved `DelegatingBinder` to it's own `binder-delegation` artifact
- various naming improvements/fixes


## 0.1.2 *(2019-09-26)*

- fix the used `LifecycleOwner` used to connect a `Fragment`


## 0.1.1 *(2019-09-17)*

- fix ViewBinder.state never returning the actual value


## 0.1.0 *(2019-09-15)*

- initial release
