(ns demo1.core
  (:gen-class :extends javafx.application.Application)
  (:import (javafx.beans.value ChangeListener ObservableValue)
           (javafx.concurrent Worker$State)
           (javafx.event ActionEvent EventHandler)
           (javafx.scene Scene)
           (javafx.scene.control Button)
           (javafx.scene.layout StackPane)
           (javafx.stage Stage)
           (javafx.scene.web WebView)
           (javafx.application Platform)))

(defn -start [app ^Stage stage]
  (let [root       (StackPane.)
        btn        (Button.)
        web-view   (WebView.)
        state-prop (.stateProperty (.getLoadWorker (.getEngine web-view)))
        url        "http://clojure.org"]

    ;; Get app param args
    ;; https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.Parameters.html#getNamed--
    ;;
    (println "raw params: " (.getRaw (.getParameters app)))
    (println "named JNLP params: " (.getNamed (.getParameters app)))
    (println "un-named params: " (.getUnnamed (.getParameters app)))

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

    (doto btn
      (.setText "Just a button")
      (.setOnAction
        (proxy [EventHandler] []
          (handle [^ActionEvent event]
            (println "The button was clicked")))))
    (.add (.getChildren root) btn)

    ;; Set scene and show stage
    (doto stage
      (.setTitle "JavaFX app with Clojure")
      (.setScene (Scene. root 800 600))
      (.show))))

(defn -stop
  "Stop method is called when the application exits."
  [app]
  (println "Exiting application!")
  )

(defn launch
  "Launch a JavaFX Application using class clj.jfx.App"
  [args]
  ;; Print command line arg values that were given as cmd-line named args with -D flag
  ;; -Dfoo=5 -Dbar="Sam"
  (println (System/getProperty "foo"))
  (println (System/getProperty "bar"))

  (javafx.application.Application/launch demo1.core (into-array String args)))

(defn -main [& args]
  (println "ARGS passed in: " args)
  (launch args))

