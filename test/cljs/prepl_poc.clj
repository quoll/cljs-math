(ns cljs.prepl-poc
  (:require [cljs.core.server]
            [cljs.repl.node]
            [clojure.core.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]))

;; run the tests with clojure -M:test -n cljs.prepl-poc

(def reader (atom nil))
(def writer (atom nil))

(t/use-fixtures :once
  (fn [f]
    (println "Launching test pREPL.")
    (server/start-server {:accept 'cljs.core.server/io-prepl  :address "127.0.0.1"  :port 6777 :name "cljs.math-repl" :args [:repl-env (cljs.repl.node/repl-env)]})
    (let [socket (java.net.Socket. "127.0.0.1" 6777)
          rdr (io/reader socket)
          wrtr (io/writer socket)]
      (reset! reader rdr)
      (reset! writer wrtr)
      (println "Executing tests")
      (f)
      (println "Tearing down test pREPL.")
      (.close @reader)
      (.close @writer)
      (.close socket))))

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
