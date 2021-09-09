# Map Marks

Map Marks is a WIP Slay the Spire mod for map node highlighting. It is a spiritual successor and an attempt to improve upon two previous mods: "Highlight Paths" (by reina - [Steam workshop link](https://steamcommunity.com/sharedfiles/filedetails/?id=1611047977)) and "Better Paths" (the world's worst titled mod by me - [Github link](https://github.com/casey-c/sts_betterpaths)).

![overview](.github/overview.png)

## Features

There are a number of improvements over the pre-existing highlighting mods:

### Highlights

Starting a

    right-click + hold + mouse move 

on top of a node will enter highlighting mode. While retaining right click, you can move your mouse around on top of other nodes to activate their highlights. You can also **single right-click** a node to toggle just that one node.

If you start a **right-click + drag** on an unhighlighted node, you will highlight all nodes you pass over with the mouse, setting their color to the currently selected one. If you start dragging on an already highlighted node, one of two things will happen: a repaint, if the color of the highlighted node is *different* than the currently selected color (this will just change the color of the node underneath but leave it highlighted, and will continue to act as if you're in highlighting mode for other nodes you pass over); or an unhighlight, if the node you start on already has the same color as the selected one.

E.g. if your current color is red and you start **right-click + drag** on a red node, you'll clear the highlight on the node you started on, and any node you pass over afterwards will also be cleared. If instead the starting node was *green*, you'll repaint that node to red and anything you pass over while still right click dragging will be set to red. It's much more intuitive than it sounds from this poorly written description.

![highlights](.github/highlight.png)

### Quick Clear-All

You can 

    right-click

the color indicator to the left of the "Legend" text to remove all current highlighting. In the future, this color indicator will also have a tool-tip describing how to work the mod, and perhaps have some more visual cues for which mode you're currently using.

### Multicolor Highlighting
Use a radial selection menu to choose the current color. This menu can be used by 

    right-click + hold + mouse move

anywhere on the screen outside a node or the legend items. Pulling your mouse towards a color while the right-click is held will activate that color. You'll know if you've gone far enough along a particular direction when the other colors dim and the selected color is pulled to the top. Release right click to select that color. 

The current highlight color is displayed to the left of the "Legend" text on the right hand side of the screen (which can be right-clicked to clear all).

![radial menu](.github/radial.png)

### Quick Category Toggle

Quickly enable / disable the highlights of all nodes of a certain type by **right-clicking** the corresponding legend item.

![legend toggle](.github/legend.png)

## Planned Features

This is an on-going project (hopefully), so there are a few features I'm still working on for the initial real release:

* Better graphics (the initial map highlight tiles are unfinished visually)
* In-game help (a tool-tip for the clear button on the Legend)
* Multiple visual modes (a square tile, a surrounding circle like "Highlight Paths", a surrounding square like "Better Paths", etc.) beyond just the default
* A "free-pen" mode, inspired by the "Spire with Friends" mod. You'll be able to draw directly on the map in addition to highlighting nodes.
* Automatically hiding "unreachable" nodes (no need to highlight an unreachable floor behind you or impossible to get to without wing boots - unless you have wing boot charges, of course)

Lower priority features (perhaps after the workshop release):
* Customizable colors
* Better config options (being able to change the behavior of when to start repainting, hiding unreachable nodes flag, ...???)

## Installation

This is a rough pre-alpha of the mod. Don't use yet unless you're willing to encounter bugs / crashes / a slightly more painful installation.

Head to the [releases page](https://github.com/casey-c/mapmarks/releases) and download **both** the "easel" JAR file and the "MapMarks" JAR file. Easel is my currently unreleased UI library project (which once it's more stable will make its way to a more formal Github repo and the workshop). Because Easel is not even ready for an alpha release, I'm including the most up to date build of it so far alongside the releases of this repository. 

When this mod requires an update, you will want to redownload **both** of these JAR files to make sure everything is working. Eventually these both will be on the workshop to make things easy, but we're not there yet as there's a lot of work to do first.

Put the downloaded JAR files into your *SlayTheSpire/mods* directory, and be sure to enable them both once you launch Slay the Spire with mods. 

**IMPORTANT:** if you have installed any other highlight paths mod (e.g. the one from the workshop, or my other paths mod), **please disable and uninstall** them before trying this mod. They're probably not compatible.
