(ns spring-mvc-mybatis-template.routes.home
  (:require [spring-mvc-mybatis-template.layout :as layout]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [spring-mvc-mybatis-template.db.core :refer [*db*] :as db]
            [clojure.java.jdbc :as jdbc]
            [camel-snake-kebab.core :refer :all]
            [selmer.filters :as filters]
            ))

;(defn home-page []
;  (layout/render
;    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))
;
;(defn about-page []
;  (layout/render "about.html"))
;
;(defroutes home-routes
;  (GET "/" [] (home-page))
;  (GET "/about" [] (about-page)))


(defn get-schemas []
  (jdbc/with-db-connection
   [pool *db*]
   (let
    [meta (-> pool :datasource .getConnection .getMetaData)]
     (clojure.core/resultset-seq (.getSchemas meta)))))


(defn get-table-list [schema table]
  (jdbc/with-db-metadata
   [md *db*]
   (-> md
       (.getTables nil schema table (into-array ["TABLE"]))
       clojure.core/resultset-seq
       doall)))

(defn tag-is_char [r]
  (assoc r :is_char (if (re-find #"char" (or (r :type_name) "")) true false)))

(defn get-table-cols [schema table]
  (jdbc/with-db-metadata
   [md *db*]
   ;(map tag-is_char
   (-> md
       (.getColumns nil schema table nil)
       clojure.core/resultset-seq
       ((partial map tag-is_char))
       ;)
       )))

(defn get-primary-keys [schema table]
  (jdbc/with-db-metadata
   [md *db*]
   (-> md
       (.getPrimaryKeys nil schema table)
       clojure.core/resultset-seq
       ;doall
       ((partial map tag-is_char))
       )))

(defn home-page []
  (layout/render
   "home.html"
   {}))

(def default-package-name "com.company1.project1")

(defn filter-package-name [name]
  (do
    (println (str "name=>" name "<"))
  (if (empty? name) default-package-name name)))

;(defn table-page [schema table-name]
;  (let [
;        table-all-cols (get-table-cols schema table-name)
;        table-pks (get-primary-keys schema table-name)
;        pk-set (into #{} (map :column_name table-pks))
;        table-cols (remove #(pk-set (:column_name %)) table-all-cols)
;        ]
;    (layout/render "template-mybatis.xml"
;                   {:schema schema
;                    :table-name table-name
;                    :class-name (->PascalCase table-name)
;                    :table-cols table-cols
;                    :table-all-cols table-all-cols
;                    :table-pks table-pks})))

(defn table-mybatis [schema table-name package-name]
  (let [
        table-all-cols (get-table-cols schema table-name)
        table-pks (get-primary-keys schema table-name)
        pk-set (into #{} (map :column_name table-pks))
        table-cols (remove #(pk-set (:column_name %)) table-all-cols)
        ]
    {:schema schema
     :table-name table-name
     :class-name (->PascalCase table-name)
     :table-cols table-cols
     :table-all-cols table-all-cols
     :table-pks table-pks
     :package-name (filter-package-name package-name)
     }))

(defn tables-work [schema table-name package-name]
  (let [
        tables (get-table-list schema table-name)
        ]
    (do
      (println tables)
      {:tables tables :schema schema
       :package-name package-name
       }
      )))

(defn tables-sql [schema]
  (let [
        tables (get-table-list schema nil)
        ]
    {:tables tables :schema schema}
    ))

(defn class-dao [schema table-name package-name]
  {:schema schema
   :table-name table-name
   :class-name (->PascalCase table-name)
   :package-name (filter-package-name package-name)
   }
  )
(defn class-service [schema table-name package-name]
  {:schema schema
   :table-name table-name
   :class-name (->PascalCase table-name)
   :package-name (filter-package-name package-name)
   }
  )
(defn class-serviceImpl [schema table-name package-name]
  {:schema schema
   :table-name table-name
   :class-name (->PascalCase table-name)
   :property-name (->camelCase table-name)
   :package-name (filter-package-name package-name)
   }
  )
(defn class-entity [schema table-name package-name]
  {:schema schema
   :table-name table-name
   :class-name (->PascalCase table-name)
   :table-all-cols (get-table-cols schema table-name)
   :package-name (filter-package-name package-name)
   }
  )

(filters/add-filter! :PascalCase #(->PascalCase %))
(filters/add-filter! :date_column? #(#{"create_dt" "update_dt"} %))
(filters/add-filter! :non-updatable-column? #(#{"create_dt" "update_dt" "use_yn"} %))
(filters/add-filter! :use_yn? #(#{"use_yn"} %))

;(defn about-page []
;  (layout/render "about.html"))

(defroutes home-routes
           ;;(GET "/" [] (home-page))
           ;(GET "/about" [] (about-page))
           (GET "/table" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(table-mybatis schema table_name package_name))
                (#(layout/render "template-mybatis.xml" %))
                (response/content-type "text/xml")
                ))
             )
           (GET "/class-dao" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(class-dao schema table_name package_name))
                (#(layout/render "template-class-dao.txt" %))
                (response/content-type "text/plain")
                ))
             )
           (GET "/class-service" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(class-service schema table_name package_name))
                (#(layout/render "template-class-service.txt" %))
                (response/content-type "text/plain")
                ))
             )
           (GET "/class-serviceImpl" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(class-serviceImpl schema table_name package_name))
                (#(layout/render "template-class-serviceImpl.txt" %))
                (response/content-type "text/plain")
                ))
             )
           (GET "/class-entity" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(class-entity schema table_name package_name))
                (#(layout/render "template-class-entity.txt" %))
                (response/content-type "text/plain")
                ))
             )
           (GET "/tables-sql" request
             (do
               ;(println request)
               (->
                request
                :params
                :schema
                tables-sql
                (#(layout/render "tables-sql.txt" %))
                (response/content-type "text/plain")
                )))
           (GET "/" request
             (let [{:keys [schema table_name package_name]} (:params request)]
               (->
                (#(tables-work schema table_name package_name))
                (#(layout/render "template.txt" %))
                (response/content-type "text/plain")
                )))
             ;(do
             ;  (println request)
             ;  (->
             ;   request
             ;   :params
             ;   :schema
             ;   tables-work
             ;   (#(layout/render "template.txt" %))
             ;   (response/content-type "text/plain")
             ;   )))
           )

