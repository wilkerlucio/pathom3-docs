(ns com.wsscode.pathom3.docs.embed
  (:require [helix.core :as h]
            [cljs.tools.reader :refer [read-string]]
            [com.wsscode.pathom3.viz.plan :as p.plan]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [edn-query-language.core :as eql]
            [com.wsscode.pathom3.viz.ui :as ui]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            ["react-dom" :as react-dom]
            [cljs.spec.alpha :as s]))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available]}]
  (dom/div {:style {:flex "1"}}
    (h/$ p.plan/PlanCytoscape
      {:frames
       (->> (p.plan/compute-frames {::pci/index-oir                                     oir
                                    ::eql/query                                         query
                                    :com.wsscode.pathom3.connect.planner/available-data available})
            (mapv (juxt identity p.plan/c-nodes-edges)))

       :display
       (some-> (or displayType "display-type-label") (#(keyword "com.wsscode.pathom3.viz.plan" %)))})))

(def area-style
  {:height  "200px"
   :width   "100%"
   :outline "0"
   :resize  "none"})

(defn safe-read-string [s]
  (try (read-string s) (catch :default _ nil)))

(h/defnc ^:export PlannerExplorer []
  (let [[oir :as oir-state] (hooks/use-state "{:a {#{} #{a}}\n :b {#{:g} #{b}}\n :c {#{} #{c}}\n :e {#{} #{e e1}}\n :f {#{:e} #{f}}\n :g {#{:c :f} #{g}}\n :h {#{:a :b} #{h}}}")
        [query :as query-state] (hooks/use-state "[:h]")
        [gd set-gd!] (hooks/use-state nil)
        valid-oir?   (s/valid? ::pci/index-oir (safe-read-string oir))
        valid-query? (s/valid? ::eql/query (safe-read-string query))]
    (dom/div {:style {:flex "1" :display "flex" :flex-direction "column"}}
      (dom/div {:style {:display "flex"}}
        (dom/div {:style {:flex "1"}}
          (dom/div "Index OIR")
          (dom/textarea {:style (cond-> area-style
                                  (not valid-oir?)
                                  (assoc :border-color "#f00"))
                         :&     (ui/dom-props {::ui/state oir-state})}))
        (dom/div {:style {:flex "1"}}
          (dom/div "Query")
          (dom/textarea {:style (cond-> area-style
                                  (not valid-query?)
                                  (assoc :border-color "#f00"))
                         :&     (ui/dom-props {::ui/state query-state})})))

      (dom/div {:style {:text-align "center"}}
        (dom/button {:on-click #(let [config {::pci/index-oir (safe-read-string oir)
                                              ::eql/query     (safe-read-string query)}]
                                  (if (s/valid? (s/keys) config)
                                    (set-gd! config)))
                     :disabled (not (and valid-oir? valid-query?))
                     :style    {:margin-bottom "12px"}}
          "Render Graph"))

      (if gd
        (dom/div {:style {:flex 1}}
          (h/$ p.plan/PlanCytoscape
            {:frames
             (->> (p.plan/compute-frames gd)
                  (mapv (juxt identity p.plan/c-nodes-edges)))

             :display
             ::p.plan/display-type-label}))))))

(defn listen-window-messages [f]
  (js/window.addEventListener "message"
    (fn [^js msg]
      (try
        (f (read-string (.-data msg)))
        (catch :default _)))))

(def component-map
  {"plan-cytoscape"   PlanCytoscapeJS
   "planner-explorer" PlannerExplorer})

(defn start-component [{:keys [component] :as msg}]
  (if-let [Comp (component-map component)]
    (react-dom/render (h/$ Comp {:& msg}) (js/document.getElementById "component"))
    (js/console.warn "Component not found:" component)))

(defn start []
  (listen-window-messages start-component)
  (if-let [msg (some-> js/window.location.search (js/URLSearchParams.) (.get "msg"))]
    (try
      (start-component (read-string msg))
      (catch :default e
        (js/console.error "Error parsing query msg:" msg e)))))

(start)
