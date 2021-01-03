(ns com.phronemophobic.reveal-treemap
  (:require [treemap-clj.view :as tv]
            [vlaaad.reveal.ext :as rx]
            [treemap-clj.core :refer [max-depth
                                      make-rect
                                      rect-parent
                                      rect-keypath
                                      translate
                                      tree-depth
                                      keyed-treemap
                                      treemap
                                      treemap-options-defaults]]
            [membrane.cljfx :as mfx
             :refer [membrane-component]]
            [cljfx.api :as fx]))


(defn obj->fx-treemap [obj [w h]]
  (let [tm (keyed-treemap obj (make-rect w h))
        tm-render (tv/wrap-treemap-events
                     tm
                     [
                      (tv/render-depth tm 0.4)
                      (tv/render-keys tm)
                      ])]
    (mfx/->Cached tm-render)))

(rx/defaction ::treemap [obj]
  (fn []
    (let [app-state (atom {:tm-render (obj->fx-treemap obj [400 400])})]
      {:fx/type rx/observable-view
       :ref app-state
       :fn (fn [state]
             {:fx/type :scroll-pane
              :fit-to-width true
              :content
              (membrane-component (requiring-resolve 'treemap-clj.view/treemap-explore)
                                  state
                                  #(swap! app-state %))})})))



