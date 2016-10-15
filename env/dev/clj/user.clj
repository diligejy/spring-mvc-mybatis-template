(ns user
  (:require [mount.core :as mount]
            spring-mvc-mybatis-template.core))

(defn start []
  (mount/start-without #'spring-mvc-mybatis-template.core/repl-server))

(defn stop []
  (mount/stop-except #'spring-mvc-mybatis-template.core/repl-server))

(defn restart []
  (stop)
  (start))


