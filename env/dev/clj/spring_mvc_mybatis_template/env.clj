(ns spring-mvc-mybatis-template.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [spring-mvc-mybatis-template.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[spring-mvc-mybatis-template started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[spring-mvc-mybatis-template has shut down successfully]=-"))
   :middleware wrap-dev})
