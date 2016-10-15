(ns spring-mvc-mybatis-template.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [spring-mvc-mybatis-template.layout :refer [error-page]]
            [spring-mvc-mybatis-template.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [spring-mvc-mybatis-template.env :refer [defaults]]
            [mount.core :as mount]
            [spring-mvc-mybatis-template.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
