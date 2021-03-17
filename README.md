# wacc-plugin

![Build](https://github.com/danieldeng2/wacc-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/16301.svg)](https://plugins.jetbrains.com/plugin/16301)

<!-- Plugin description -->

WACC (pronounced “whack”) is a simple variant on the While family of languages .  

It features all of the common language constructs you would expect of a While-like language, such as program variables, simple expressions, conditional branching, looping and no-ops.
It also features a rich set of extra constructs, such as simple types, functions, arrays and basictuple creation on the heap.

This plugin aims to provide support for the WACC language in Intellij. 

*Please ensure that `gcc`, `gcc-multilib`/`glibc-devel.i686`, and `nasm` are all
installed in order to compile wacc programs for x86.*

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "wacc-plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/danieldeng2/wacc-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## Features
- [x] Syntax highlighting support
- [x] Syntax and semantic checks
- [x] Code completion and brace matching
- [x] Commenting out code
- [x] Run code on x86
- [x] Integrated WACC shell
