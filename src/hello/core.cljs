(ns hello.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [rum.core :as rum]
            ))

(enable-console-print!)

(defn by-id [id]
  (js/document.getElementById id))

;(def endpoint "https://api.fablo.pl/api/2/frisco.pl/products/query" )
;due to unresolved CORS problem, i continue using mocked responce on local
(def endpoint "http://127.0.0.1:8080/query.json")

(rum/defc item [url text]
  [:li [:a { :href url } text]])

(rum/defc results [list]
  [:ul (map (fn [r] (item (:url r) (:name r))) list )])

(defn search [phrase]
  (go (let [response (<! (http/get endpoint
                          { :with-credentials? true
                            :query-params { "results" 5
                                            "search-string" phrase }} ))]
        (rum/mount (results (:results (:products (:body response))))
                   (by-id "output"))
        )))

(.addEventListener (by-id "query_form") "submit"
  (fn [event]
    (do
      (search (aget (by-id "query") "value"))
      (.preventDefault event))))

