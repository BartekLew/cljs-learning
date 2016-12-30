(ns hello.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            ))

(enable-console-print!)

(defn by-id [id]
  (js/document.getElementById id))

;(def endpoint "https://api.fablo.pl/api/2/frisco.pl/products/query" )
;due to unresolved CORS problem, i continue using mocked responce on local
(def endpoint "http://127.0.0.1:8080/query.json")

(defn search [phrase]
  (go (let [response (<! (http/get endpoint
                          { :with-credentials? true
                            :query-params { "results" 5
                                            "search-string" phrase }} ))]
        (aset (by-id "output") "innerHTML"
              (reduce str (map (fn [r]
                                 (str "<li><a href=\"" (:url r) "\">" (:name r) "</a></li>" ))
                               (:results (:products (:body response))) 
              ))))))

(.addEventListener (by-id "query_form") "submit"
  (fn [event]
    (do
      (search (aget (by-id "query") "value"))
      (.preventDefault event))))

