# Laminar Examples & TodoList Mini-App

This repo contains some very basic components using my [Laminar](https://github.com/raquo/laminar) reactive UI library for Scala.js.

A few things to keep in mind:
* Everything in this repo is half-baked, so far I didn't have any time for polishing things up
* Nothing is consistent in terms of style. I am deliberately showcasing various Laminar features and patterns instead of using the same pattern over and over
* Some things are rather contrived for the purpose of showing the harder way of doing things

So in short, this is NOT a demonstration of how to write great Laminar app, it is merely a bunch of Laminar code that compiles and that you can easily work with to try out Laminar features and ideas.  

How to run:

1) git clone this repo to your local machine
2) open sbt, type `~fastOptJS::webpack`
3) open `index-fastopt.html` file in your browser
4) open browser console to monitor for messages and errors

Now you have `App.scala` running in your browser, and any code changes you make will show up on screen after they get incrementally recompiled (see your sbt shell) and after you reload the page in the browser. Sorry, i don't have much time to spend on this, so there's no Hot / Live Reload or anything like that.



## Author

Nikita Gazarov – [raquo.com](http://raquo.com)



## License

Laminar Examples are provided under the [MIT license](https://github.com/raquo/laminar-examples/blob/master/LICENSE.md).
