(ns main
  (:require [seesaw.core :as s]
            [seesaw.bind :as b])
  (:import com.formdev.flatlaf.FlatLightLaf
           javax.swing.UIManager))

(def head-tail-template "The coin lands %s")

(defn transform-display-value [value]
  (if (int? value)
    (format "The dice lands on %d" value)
    (case value
      :first "Press either of the buttons."
      :heads (format head-tail-template "heads")
      :tails (format head-tail-template "tails"))))

(defn dice-roll []
  (-> (rand-int 6) inc))

(defn coin-flip []
  (rand-nth [:heads :tails]))

(defn make-coin-dice-panel []
  (let [current-display-value (atom :first)
        display-value-label (s/label
                             :halign :center
                             :text (transform-display-value @current-display-value))
        dice-button (s/button :text "Roll a Dice"
                              :size [1 :by 200])
        coin-button (s/button :text "Flip a Coin")]
    (b/bind
     current-display-value
     (b/transform transform-display-value)
     (b/property display-value-label :text))
    (s/listen dice-button
              :mouse-pressed
              (fn [e]
                (reset! current-display-value (dice-roll))))
    (s/listen coin-button
              :mouse-pressed
              (fn [e]
                (reset! current-display-value (coin-flip))))
    (s/border-panel
     :center display-value-label
     :south (s/grid-panel :columns 2
                          :hgap 15
                          :vgap 15
                          :items [dice-button coin-button]))) )

(defn -main []
  (UIManager/setLookAndFeel (new FlatLightLaf))
  (FlatLightLaf/setup)


  (let [frame (s/frame :title "Coin Dice"
                       :height 500
                       :width 300
                       :content (make-coin-dice-panel))]
    (-> frame s/show! s/pack!)))

(comment
  (-main))
