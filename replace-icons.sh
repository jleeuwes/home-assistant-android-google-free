#!/usr/bin/env bash

set -Eeu -o pipefail

cd -- "$(dirname -- "$0")"

for img in app/src/*/res/mipmap-*/ic_launcher*.png \
           app/src/*/res/mipmap-*/ic_banner.png \
           app/src/*/res/drawable-*/ic_stat_ic_notification.png \
           app/src/*/res/drawable/app_icon.jpg \
; do
	# size=$(identify -format %[w]x%[h] "$img")
	w=$(identify -format %[w] "$img")
	h=$(identify -format %[h] "$img")
	# FIXME non-square images like ic_banner get stretched
	inkscape -z -w "$w" -h "$h" icon.svg --export-png="$img"

	case "$img" in
	*.jpg)
		mv "$img" "$img.png"
		convert "$img.png" -background white -alpha remove -quality 92 "$img"
		rm "$img.png"
	;;
	esac

	case "$img" in
	app/src/debug/*)
		mogrify -flip -negate "$img"
	;;
	esac
done

