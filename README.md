# House Automator Comrade App for Android

_Unofficial fork of the Home Assistant companion app
that doesn't depend on Google services or other non-free network services._

Home Assistant and the companion app are awesome,
but they require proprietary services that are not available
on custom Android OSes like [LineageOS](https://lineageos.org/).
So, a version without those services is necessary.

The developers of the official app
[have no interest in maintaining such a version
themselves](https://github.com/home-assistant/home-assistant-android/issues/42#issuecomment-584372676),
and [have suggested forking the project](https://github.com/home-assistant/home-assistant-android/issues/42#issuecomment-560160840) instead.

This is such a fork.

## What works

* Registering your device with your Home Assistant instance.
* Full control of your Home Assistant instance.

## What doesn't work

* Tracking the location of your mobile device.
* Receiving push notifications on your mobile device.

This missing functionality should be replaced by FOSS alternatives,
like those mentioned in
<https://github.com/home-assistant/home-assistant-android/issues/42#issuecomment-581090179>.
Currently I have no plans to implement these myself.

## Contributing

The setup of this fork is a bit weird (see 'Branching').
This is done to make it easy to cherry-pick my patches
from version to version.

I don't think this scheme is adequate
when doing more serious work like implementing alternative push notifications.
So if you are interested in contributing such changes,
but are deterred by my git abuse,
please contact me so we can work out a better development model.

## Branching

The `master` branch of this fork is not based on the original repository
and only contains meta stuff like this README,
and some stuff not related to a specific version of the app,
like scripts and icons.

The actual app code is produced by creating a branch from an upstream version
tag and applying the necessary changes.
The following branches are available:

* [1.8.0](https://github.com/jleeuwes/home-assistant-android-without-google/tree/1.8.0-without-google)

