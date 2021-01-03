# reveal-treemap

A treemap visualizer action for reveal

For a treemap demo, check out <https://blog.phronemophobic.com/treemap/treemap-demo.html>.

![treemap example](hover-keypath-shrunk.gif?raw=true)

## Usage

Add the dependency:

Leiningen/Boot
```
[com.phronemophobic/reveal-treemap "0.1.1"]
```

deps.edn
```
com.phronemophobic/reveal-treemap {:mvn/version "0.1.1"}
```

Load the `treemap` reveal action

```
(require 'com.phronemophobic.reveal-treemap)
```

Build a deployable jar of this library:

    $ clojure -M:jar

Install it locally:

    $ clojure -M:install

Deploy it to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment variables:

    $ clojure -M:deploy

## License

Copyright © 2021 Adrian

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
