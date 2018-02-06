(ns demo1.core
  (:gen-class :extends javafx.application.Application)
  (:import (javafx.beans.value ChangeListener ObservableValue)
           (javafx.concurrent Worker$State)
           (javafx.event ActionEvent EventHandler)
           (javafx.scene Scene)
           (javafx.scene.control Button)
           (javafx.scene.layout StackPane)
           (javafx.stage Stage)
           (javafx.scene.web WebView)))

(defn -start [app ^Stage stage]
  (let [root (StackPane.)
        btn (Button.)
        web-view (WebView.)
        state-prop (.stateProperty (.getLoadWorker (.getEngine web-view)))
        url "http://clojure.org"]

    ;; Add a WebView (headless browser)
    (.add (.getChildren root) web-view)
    ;; Register listener for WebView state changes
    (.addListener state-prop
                  (proxy [ChangeListener] []
                    (changed [^ObservableValue ov
                              ^Worker$State old-state
                              ^Worker$State new-state]
                      (println (str "Current state:" (.name new-state)))
                      (if (= new-state Worker$State/SUCCEEDED)
                        (println (str "URL '" url "' load completed!"))))))
    ;; Load a URL
    (.load (.getEngine web-view) url)

    ;; add a Button with a click handler class floating on top of the WebView
    (.setTitle stage "JavaFX app with Clojure")
    (.setText btn "Just a button")
    (.setOnAction btn
                  (proxy [EventHandler] []
                    (handle [^ActionEvent event]
                      (println "The button was clicked"))))
    (.add (.getChildren root) btn)

    ;; Set scene and show stage
    (.setScene stage (Scene. root 800 600))
    (.show stage)))

(defn -stop
  "Stop method is called when the application exits."
  [app]
  (println "Exiting application!")
  )

(defn launch
  "Launch a JavaFX Application using class clj.jfx.App"
  [args]
  (javafx.application.Application/launch demo1.core (into-array String [])))

(defn -main [& args]
  (launch args))

