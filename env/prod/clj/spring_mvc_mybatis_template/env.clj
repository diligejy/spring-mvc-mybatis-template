(ns spring-mvc-mybatis-template.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[spring-mvc-mybatis-template started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[spring-mvc-mybatis-template has shut down successfully]=-"))
   :middleware identity})
