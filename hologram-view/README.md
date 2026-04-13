# HologramView Extension

An extension for MIT App Inventor 2 that brings a futuristic, highly animated "Holographic Profile Card" into your Android apps. This extension uses purely native Android views, including floating skewed projections and blinking projector light effects.

Created by: TechHamara  
Compiled by: FAST

## Features
- **100% Native Views**: Achieves smooth 60fps holographic projection animations using `ValueAnimator` and custom `Canvas Matrix` skews.
- **Customizable UI**: Set the profile name, title, statistics, button text, and even load an external Profile Image URL asynchronously.
- **Interactive Projections**: Click the top or bottom projector bars to slide-open the holographic projection.
- **Blinking Light Animations**: Built-in infinite loop animations on the top light base simulating a futuristic projector.
- **Built-in Events**: Receive callbacks when the hologram finishes opening/closing, or when the user clicks the "Follow" button.

## Properties
- `ProfileName` *(String)*: Text displayed as the main heading. (Default: TechHamara)
- `ProfileTitle` *(String)*: Text displayed as the sub-title heading. (Default: Developer)
- `ProfileStats` *(String)*: Text displayed detailing user statistics.
- `ButtonText` *(String)*: Text on the action button. (Default: FOLLOW ME)
- `ImageUrl` *(String)*: A direct URL link to an image or a local asset file name (e.g. "image.png").
- `HomePageColor` *(Color)*: Sets a solid background color for the main Hologram container.
- `HomePageGradientStartColor` *(Color)*: Primary background gradient color for the main container.
- `HomePageGradientEndColor` *(Color)*: Secondary background gradient color for the main container.
- `HomePageImage` *(String)*: A URL or local asset filename to use as the background image for the main container.
- `HologramImage` *(String)*: A direct URL link or local asset to replace the floating hologram projection ray with a custom image.
- `HologramStartColor` *(Color)*: Primary background gradient color of the floating hologram.
- `HologramEndColor` *(Color)*: Secondary background gradient color of the floating hologram.
- `ProjectorColor` *(Color)*: Base color of the top and bottom metallic projectors.
- `ButtonStartColor` *(Color)*: Primary inner gradient color of the follow button.
- `ButtonEndColor` *(Color)*: Secondary inner gradient color of the follow button.
- `TextColor` *(Color)*: Color to render the Profile Name and Title.
- `StatsColor` *(Color)*: Color to render the Profile statistics text.
- `HologramSpeed` *(Integer)*: Duration constraint of the holographic skew sweep in milliseconds. (Default: 15000)

## Blocks / Methods
- `Initialize(arrangement Component)`: Renders and places the Hologram into the specified target arrangement.
- `Open()`: Triggers the slide-down skew animation to reveal the Hologram.
- `Close()`: Retracts the Hologram back into the projector base.
- `Toggle()`: Switches between Open and Closed states automatically.

## Events
- `Opened()`: Triggered once the opening animation completely finishes.
- `Closed()`: Triggered once the closing animation completely finishes.
- `ButtonClicked()`: Triggered when the user taps on the main Action Button.
- `ProfileImageClicked()`: Triggered when the user taps directly on the center Profile Image avatar.
- `ProfileNameClicked()`: Triggered when the user taps on the Profile Name text.
- `ProfileTitleClicked()`: Triggered when the user taps on the Profile Title text.
- `ProfileStatsClicked()`: Triggered when the user taps on the Profile Stats text.

## Example Usage
1. Drag a `VerticalArrangement` into your UI.
2. Go to the Blocks editor, and in the `Screen1.Initialize` event, call `HologramView.Initialize` passing your arrangement component block.
3. Call `HologramView.Open()` on a Button click or let your users tap the dark grey top/bottom projector bases to toggle the hologram!
