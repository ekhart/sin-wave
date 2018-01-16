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

(defn log [x]
  (.log js/console x))

(log "hello clojurescript")

(def canvas (.getElementById js/document "myCanvas"))
(log canvas)
(def ctx (.getContext canvas "2d"))
(log ctx)

;; Clear canvas before doing anything else
(.clearRect ctx 0 0 (.-width canvas) (.-height canvas))


;; Creating time
(def interval js/Rx.Observable.interval)
(def my-time (interval 10))

(-> my-time
    (.take 5)
    (.subscribe #(log %)))

(defn deg-to-rad [n]
  (* (/ Math/PI 180) n))

(defn sine-coord [x]
  (let [sin (Math/sin (deg-to-rad x))
        y (- 100 (* sin 90))]
    {:x x
     :y y
     :sin sin}))

(log (str (sine-coord 50)))

(def sine-wave (.map my-time sine-coord))

(-> sine-wave
    (.take 5)
    (.subscribe #(log (str %))))

(defn fill-rect [x y colour]
  (set! (.-fillStyle ctx) colour)
  (.fillRect ctx x y 2 2))

(-> sine-wave
    (.take 600)
    (.subscribe (fn [{:keys [x y]}]
                  (fill-rect x y "orange"))))

;; More colors
(def colour (.map sine-wave
                  (fn [{:keys [sin]}]
                    (if (< sin 0)
                      "red"
                      "blue"))))

(-> (.zip sine-wave colour #(vector % %2))
    (.take 600)
    (.subscribe (fn [[{:keys [x y]} colour]]
                  (fill-rect x y colour))))
