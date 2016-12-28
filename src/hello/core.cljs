(ns hello.core
  (:require ))

(enable-console-print!)

(defn by-id [id]
  (js/document.getElementById id))

(.addEventListener (by-id "query_form") "submit"
  (fn [event]
    (do
      (js/alert (.-value (by-id "query")))
      (.preventDefault event))))

