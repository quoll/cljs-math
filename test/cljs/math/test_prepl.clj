(ns cljs.math.test-prepl
  (:require [cljs.core.server]
            [cljs.repl.node]
            [clojure.core.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]))

;; run the tests with clojure -M:test -n cljs.math.test-prepl

(def reader (atom nil))
(def writer (atom nil))

(t/use-fixtures :once
  (fn [f]
    (println "Launching test pREPL.")
    (let [server (server/start-server {:accept 'cljs.core.server/io-prepl
                                       :address "127.0.0.1"
                                       :port 0
                                       :name "cljs.math-repl"
                                       :args [:repl-env (cljs.repl.node/repl-env)]})
          port (-> server (.getLocalPort))]
      (println "Server opened on port" port)
      (with-open [socket (java.net.Socket. "127.0.0.1" port)
                  rdr (io/reader socket)
                  wrtr (io/writer socket)]
        (reset! reader rdr)
        (reset! writer wrtr)
        (println "Executing tests")
        (f)
        (println "Tearing down test pREPL.")))))

(defn cljs-eval [expr]
  (-> (binding [*out* @writer
                *in* @reader]
        (println expr)
        (read-line))
      edn/read-string
      :val))

(deftest sanity-test
  (is (= "6" (cljs-eval "(+ 1 2 3)"))))

(deftest cljs-match-sanity-test
  (is (= "nil"  (cljs-eval "(require 'cljs.math)")))
  (is (= "666" (cljs-eval "(cljs.math/abs -666)"))))
