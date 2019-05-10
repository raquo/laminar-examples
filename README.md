# Laminar TodoMVC & Examples 

This repo contains a TodoMVC implementation and an assortment of random components using my [Laminar](https://github.com/raquo/laminar) reactive UI library for Scala.js.

The **[TodoMVC](http://todomvc.com/)** example is decent Laminar code, except all its Scala code is deliberately put into one file for easier cross-reference. See `todomvc/TodoMvcApp.scala` for the implementation.

The stuff in `oldstuff` is old. Duh. I should probably delete most of it.

How to run:

1) git clone this repo to your local machine
2) open sbt, type `~fastOptJS`
3) open `index-fastopt.html` file in your browser (found in `target/scala-2.12/classes`)
4) open browser console to monitor for messages and errors

Now you have `App.scala` running in your browser, and any code changes you make will show up on screen after they get incrementally recompiled (see your sbt shell) and after you reload the page in the browser. Sorry, I don't have much time to spend on this, so there's no Hot / Live Reload or anything like that.

Note that this TodoMVC app requires a couple CSS files. If the styles are all broken when you run it locally, make sure the CSS is loaded properly using browser dev tools. Shouldn't be a problem, but mentioning this just in case, since there's no web server bundled with this project.


## Author

Nikita Gazarov – [raquo.com](http://raquo.com)



## License

Laminar Examples are provided under the [MIT license](https://github.com/raquo/laminar-examples/blob/master/LICENSE.md) except for the two CSS files that I borrowed from another TodoMVC project (linked in the files).
