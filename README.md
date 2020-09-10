# House Automator Comrade App for Android

_Unofficial fork of the Home Assistant companion app
that doesn't depend on Google services or other non-free network services._

Home Assistant and the companion app are awesome,
but they require proprietary services that are not available
on custom Android OSes like [LineageOS](https://lineageos.org/).
So, a version without those services is necessary.

__This version now exists in the form of [a minimal build flavor of the official Home Assistant app](https://github.com/home-assistant/android/pull/682), which makes this fork obsolete. That app will soon be in the F-Droid repository, see https://gitlab.com/fdroid/fdroiddata/-/merge_requests/7387 :partying_face:__

## But what about push notifications an location tracking?

This missing functionality should be replaced by FOSS alternatives,
like those mentioned in
<https://github.com/home-assistant/home-assistant-android/issues/42#issuecomment-581090179>.

Currently I have no plans to implement this.
Also, it might be better to implement these as a separate flavor upstream, if upstream is open to that. 

## Branching

The `master` branch of this fork is not based on the original repository
and only contains meta stuff like this README,
and some stuff not related to a specific version of the app,
like scripts and icons.

The actual app code is produced by creating a branch from an upstream version
tag and applying the necessary changes.
The following branches are available:

* [1.8.0](https://github.com/jleeuwes/home-assistant-android-without-google/tree/1.8.0-without-google)
