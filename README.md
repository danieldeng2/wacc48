**This is the git repository for the WACC compilers lab. This implementation uses antlr as the frontend parser and Kotlin as the implementation language.**

## Files / Directories

#### `build`

This folder is generated every time the project is compiled. Contents of this folder will be overwritten every clean build. 

The final `jar` file will be placed in this folder. 

#### `src`

This folder contains all the source code for the project, structured as follows: 

* `main`
  * `antlr` - Configuration files for antlr, will generate Java source code inside the `build` folder.
  * `kotlin` - Kotlin source code to generate and validate the AST, as well as ARM code generation. 
* `test`
  * `kotlin` - Tests written for the project, including unit and integration tests for the frontend and the backend.
  * `resources` - `.wacc` files used for the integration tests, as well as `.input` files to provide stdin for tests that require input. This folder will be automatically copied into `build/resources/test` and compiled `*.s` files for tests will be placed into that folder for debugging purposes. 

#### `build.gradle.kts`

Configuration file for gradle. All the dependencies for the project are managed via gradle, including but not limited to: 

* **antlr 4** - To act as lexer and parser for the frontend. 
* **Retrofit 2 & Moshi** - Libraries used to contact and parse the reference compiler and emulator via API for integration tests


#### `plugin`

The plugin folder is added as a git subtree in this project, to update from original repository, run: 

```
git remote add -f wacc-plugin git@github.com:danieldeng2/wacc-plugin.git
git fetch wacc-plugin main
git subtree pull --prefix plugin wacc-plugin main --squash
```

To contribute upstream, run:

```
git subtree push --prefix plugin wacc-plugin main --squash
```

## Scripts

The following scripts can be run in the root directory of the project. 

#### `./gradlew jar`

Compiles the project and generates an executable `.jar` file inside `build/libs`.

#### `./gradlew clean`

Deletes the `build` directory for a clean build.

#### `./gradlew test`

Runs unit and integration tests written for the project.

#### `./run shell <optional.wacc>`

Run an interactive wacc shell.

#### `./compile <*.wacc>`

Runs the WACC compiler generated by `./gradlew jar` and generates a `.s` in the root directory of this project.

#### `./compile -a x86 <*.wacc>`

Compiles the wacc source to x86 architecture.

#### `make pluginlib`

Compiles the wacc compiler and uses it as a library for the plugin
