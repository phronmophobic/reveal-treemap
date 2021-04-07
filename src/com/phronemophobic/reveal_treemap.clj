(ns com.phronemophobic.reveal-treemap
  (:require [treemap-clj.view :as tv]
            [membrane.ui :as ui]
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
            [cljfx.api :as fx])
  (:import javafx.geometry.BoundingBox))


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
  (when (seqable? obj)
   (fn []
     (let [app-state (atom {:tm-render (obj->fx-treemap obj [400 400])})]
       {:fx/type rx/observable-view
        :ref app-state
        :fn (fn [state]
              (let [treemap-view (membrane-component #'treemap-clj.view/treemap-explore
                                                     state
                                                     #(swap! app-state %))]
               {:fx/type rx/popup-view
                :select (fn [e]
                          (condp instance? e
                            javafx.scene.input.KeyEvent
                            (when-let [rect (-> state :select-rect :rect)]
                              (let [node (.getTarget e)
                                    local-bounds (BoundingBox. (:x rect)
                                                               (:y rect)
                                                               (:w rect)
                                                               (:h rect))
                                    bounds (.localToScreen node local-bounds)]
                                (when rect
                                  {:bounds bounds
                                   :value (:obj rect)})))

                            javafx.scene.input.ContextMenuEvent
                            (let [node (.getTarget e)
                                  x (.getX e)
                                  y (.getY e)
                                  rect (->> (ui/mouse-down (treemap-clj.view/treemap-explore state)
                                                           [x y])
                                            (some (fn [[type & args :as intent]]
                                                    (when (= type :treemap-clj.view/select-rect)
                                                      (nth intent 2)))))
                                  local-bounds (BoundingBox. x y 10 10)
                                  bounds (.localToScreen node local-bounds)]
                              (when rect
                                {:bounds bounds
                                 :value (:obj rect)}))))
                :desc {:fx/type :scroll-pane
                       :fit-to-width true
                       :content treemap-view}}))}))))



