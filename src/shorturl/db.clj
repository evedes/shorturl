(ns shorturl.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all]
            [shorturl.env :refer [env]]))


(def mysql-db {:host (env :HOST)
               :dbtype (env :DBTYPE)
               :dbname (env :DBNAME)
               :user (env :USER)
               :password (env :PASSWORD)
               :ssl {:sslmode "require"}})

(defn query [q]
  (j/query mysql-db q))

(defn insert! [q]
  (j/db-do-prepared mysql-db q))

(defn insert-redirect! [slug url]
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [[slug url]])
               (sql/format))))

(defn get-url [slug]
  (-> (query (-> (select :*)
                 (from :redirects)
                 (where [:= :slug slug])
                 (sql/format)))
      first :url))

(comment
  (query (-> (select :*)
             (from :redirects)
             (sql/format)))
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [["abc" "https://github.com/seancorfield/honeysql"]])
               (sql/format)))
  (insert-redirect! "xyz" "https://eduardovedes.com")
  (get-url "xyz")) 
