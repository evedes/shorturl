(ns app.core
  (:require [helix.core :refer [defnc $]]
            [helix.hooks :as hooks]
            [helix.dom :as d]
            ["react-dom/client" :as rdom]
            [promesa.core :as p]))

(defnc app []
  (let [[state set-state] (hooks/use-state {:slug nil
                                            :url ""})
        fetch-slug (fn []
                     (p/let [_response (js/fetch "/api/redirect/" (clj->js {:headers {:Content-Type "application/json"}
                                                                            :method "POST"
                                                                            :body (js/JSON.stringify #js {:url (:url state)})}))
                             response (.json _response)
                             data (js->clj response :keywordize-keys true)]
                       (set-state assoc :slug (:slug data))))
        redirect-link (str (.-origin js/location) "/" (:slug state) "/")]
    (d/div {:class-name "bg-gray-100 grid place-items-center h-screen"}
           (if (:slug state)
             (d/div (d/a {:class-name "text-blue-500 text-3xl font-semibold" :href redirect-link} redirect-link))
             (d/div {:class-name "flex flex-col gap-8 items-center justify-center"}
                    (d/div {:class-name "text-2xl text-blue-700 font-semibold"} "Url Shortener")
                    (d/div {:class-name "flex items-center gap-2"}
                           (d/input {:value (:url state)
                                     :on-change #(set-state assoc :url (.. % -target -value))
                                     :class-name "py-2 px-4 shadow-lg rounded-lg form-control border border-gray-300 min-w-[300px]"
                                     :auto-focus true
                                     :placeholder "Please enter the URL"})
                           (d/button {:on-click #(fetch-slug)
                                      :class-name "border shadow-lg rounded-lg uppercase py-2 px-4 bg-blue-600 hover:bg-blue-500 text-white"} "Shorten Url"))))
           (d/div {:class-name "fixed flex flex-col justify-center items-center bottom-8 mx-auto"}
                  (d/div "🇵🇹 🇪🇺")
                  (d/div "vedes.pt")))))

(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))))
