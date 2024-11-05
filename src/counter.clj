(ns counter
  (:require [seesaw.core :as s]
            [seesaw.mig :as sm]
            [seesaw.bind :as b])
  (:import com.formdev.flatlaf.FlatLightLaf
           javax.swing.UIManager))

; Nice styles

(UIManager/setLookAndFeel (new FlatLightLaf))

(def counter (atom 0))

(defn update-counter-text! [counter-widget counter-atom]
  (s/config! counter-widget
             :text (str "Pushed " @counter-atom " times")))

(def counter-text (s/label
                   :halign :center))

(b/bind
 counter
 (b/transform #(str "Pushed " % " times"))
 (b/property counter-text :text))

(def counter-button (s/button :text "Click Me!"))

(s/listen counter-button :action
          (fn [x]
            (swap! counter inc)))

(def reset-button (s/button :text "Reset"))

(s/listen reset-button :action
          (fn [x]
            (reset! counter 0)))

(def panel (s/grid-panel
            :columns 1
            :hgap 15
            :vgap 15
            :items [counter-text
                    counter-button
                    reset-button]))

(def frame (s/frame :title "Counter" :content panel))

(-> frame s/show! s/pack!)
