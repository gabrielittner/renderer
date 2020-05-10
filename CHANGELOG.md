Change Log
==========

Version 0.5.0 *(2020-05-10)*
----------------------------

- `Fragment.connect` is now callable from `onCreateView()`
- make `ViewRenderer.LayoutInflaterFactory` layout id a ctor parameter
- make `ViewRenderer.ViewBindingFactory` ViewBinding creator a ctor parameter
- remove `LayoutInflater` and `attachToRoot: Boolean` from `InflaterFactory`
- allow to map the type of actions in `DelegatingRenderer`

Version 0.4.0 *(2020-05-03)*
----------------------------

- change `ViewRenderer.InflaterFactory` to an interface
- make the parent `ViewGroup` passed into `InflaterFactory` nullable
- add `LayoutInflater` and `attachToRoot: Boolean` to `InflaterFactory`
- add `ViewRenderer.LayoutInflaterFactory` which matches the old `ViewRenderer.InflaterFactory`
- add `ViewRenderer.ViewBindingFactory`
- deprecate `RxViewModel` and it's `connect` methods
- fix sources not getting published


Version 0.3.0 *(2020-01-17)*
----------------------------

- BREAKING: subclasses of `ViewRenderer` now need to override `viewActions` instead of `actions`
- introduce `sendAction()` in `ViewRenderer` to avoid having to create subjects in implementations
- make `ViewRenderer.state` not nullable
- add `ViewRenderer.InflaterFactory` to tie a `ViewRenderer` to a XML layout
- cleaned up connect API, restructured modules

Version 0.2.1 *(2019-10-26)*
----------------------------

- fix the used `LifecycleOwner` used to connect a `Fragment`

Version 0.2.0 *(2019-10-20)*
----------------------------

- renamed Binder to Renderer (also includes artifact names)
- removed `connectors` artifact
- new `connect` artifact which has `LiveDataViewModel` as public interface
- new `connect-rx` artifact that contains `RxLiveDataViewModel`
- moved `DelegatingBinder` to it's own `binder-delegation` artifact
- various naming improvements/fixes

Version 0.1.2 *(2019-09-26)*
----------------------------

- fix the used `LifecycleOwner` used to connect a `Fragment`

Version 0.1.1 *(2019-09-17)*
----------------------------

- fix ViewBinder.state never returning the actual value

Version 0.1.0 *(2019-09-15)*
----------------------------

- initial release
