(ns kit-web.core
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        )
  (:require
   [ring.middleware.reload :as reload]
   [org.httpkit.server :refer [run-server]]))

(defroutes all-routes
  (GET "/" [] "handling-page")
  (GET "/ping" [] "pong")
  (POST "/ping" {body :body} (slurp body))
  (not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn in-dev? [& args] true) ;; TODO read a config variable from command line, env, or file?

(defn -main [& args] ;; entry point, lein run will pick up and start from here
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload #'all-routes) ;; only reload when dev
                  #'all-routes)]
    (run-server handler {:port 8080})))

