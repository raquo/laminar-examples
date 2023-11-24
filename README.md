# Laminar TodoMVC & Examples 

## Status

> This repo contains a client-only, webpack build setup for Laminar, as well as a few Laminar code examples. I have replaced this repo with **[Laminar Full Stack Demo](https://github.com/raquo/laminar-full-stack-demo/)** – check it out, it is better in every way.

---

This repo contains a TodoMVC implementation and an assortment of random components using my [Laminar](https://github.com/raquo/laminar) reactive UI library for Scala.js.

The standard TodoMVC example is decent Laminar code, except all its Scala code is deliberately put into one file for easier cross-reference. See **[TodoMvcApp.scala](https://github.com/raquo/laminar-examples/blob/master/src/main/scala/todomvc/TodoMvcApp.scala)** for the implementation.

The stuff in `oldstuff` is old. Duh. I should probably delete most of it.

How to run:

1) git clone this repo to your local machine
2) open sbt, type `~fastOptJS::webpack`
3) open `index-fastopt.html` file in your browser (found in `src/main/resources`)
4) open browser console to monitor for messages and errors

Now you have `App.scala` running in your browser, and any code changes you make will show up on screen after they get incrementally recompiled (see your sbt shell) and after you reload the page in the browser. Sorry, I don't have much time to spend on this, so there's no Hot / Live Reload or anything like that.

Note that this TodoMVC app requires a couple CSS files. If the styles are all broken when you run it locally, make sure the CSS is loaded properly using browser dev tools. Shouldn't be a problem, but mentioning this just in case, since there's no web server bundled with this project.

These examples are published for Scala 3, however they do not use any new Scala 3 features, so it does not really matter – they'll work just as well in Scala 2. If you really want to run these examples in Scala 2, you can revert [this commit](https://github.com/raquo/laminar-examples/commit/f29829d44edbcc34ff9b7c1c1990686ebe65afda) locally.

## Author

Nikita Gazarov – [@raquo](https://twitter.com/raquo)



## License

Laminar Examples are provided under the [MIT license](https://github.com/raquo/laminar-examples/blob/master/LICENSE.md) except for the two CSS files that I borrowed from another TodoMVC project (linked in the files).
