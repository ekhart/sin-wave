(ns sin-wave.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn root-component [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/h1 nil (:text app))))))

(defn render []
  (om/root
   root-component
   app-state
   {:target (js/document.getElementById "app")}))

(.log js/console "hello clojurescript")

(def canvas (.getElementById js/document "myCanvas"))
(.log js/console canvas)
(def ctx (.getContext canvas "2d"))
(.log js/console ctx)

;; Clear canvas before doing anything else
(.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
