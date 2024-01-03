"use strict";(self.webpackChunkpathom_3_docs=self.webpackChunkpathom_3_docs||[]).push([[553],{3905:(e,t,a)=>{a.d(t,{Zo:()=>c,kt:()=>h});var n=a(7294);function s(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function o(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,n)}return a}function r(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?o(Object(a),!0).forEach((function(t){s(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):o(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function m(e,t){if(null==e)return{};var a,n,s=function(e,t){if(null==e)return{};var a,n,s={},o=Object.keys(e);for(n=0;n<o.length;n++)a=o[n],t.indexOf(a)>=0||(s[a]=e[a]);return s}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)a=o[n],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(s[a]=e[a])}return s}var p=n.createContext({}),i=function(e){var t=n.useContext(p),a=t;return e&&(a="function"==typeof e?e(t):r(r({},t),e)),a},c=function(e){var t=i(e.components);return n.createElement(p.Provider,{value:t},e.children)},l="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},u=n.forwardRef((function(e,t){var a=e.components,s=e.mdxType,o=e.originalType,p=e.parentName,c=m(e,["components","mdxType","originalType","parentName"]),l=i(a),u=s,h=l["".concat(p,".").concat(u)]||l[u]||d[u]||o;return a?n.createElement(h,r(r({ref:t},c),{},{components:a})):n.createElement(h,r({ref:t},c))}));function h(e,t){var a=arguments,s=t&&t.mdxType;if("string"==typeof e||s){var o=a.length,r=new Array(o);r[0]=u;var m={};for(var p in t)hasOwnProperty.call(t,p)&&(m[p]=t[p]);m.originalType=e,m[l]="string"==typeof e?e:s,r[1]=m;for(var i=2;i<o;i++)r[i]=a[i];return n.createElement.apply(null,r)}return n.createElement.apply(null,a)}u.displayName="MDXCreateElement"},8982:(e,t,a)=>{a.d(t,{t:()=>s});var n=a(7294);function s(e){let{videoId:t}=e;const a="https://www.youtube.com/embed/"+t;return n.createElement("iframe",{width:"560",height:"315",src:a,frameBorder:"0",allow:"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",allowFullScreen:!0})}},5733:(e,t,a)=>{a.r(t),a.d(t,{assets:()=>i,contentTitle:()=>m,default:()=>u,frontMatter:()=>r,metadata:()=>p,toc:()=>c});var n=a(7462),s=(a(7294),a(3905)),o=a(8982);const r={id:"smart-maps",title:"Smart Maps"},m=void 0,p={unversionedId:"smart-maps",id:"smart-maps",title:"Smart Maps",description:"Smart maps are a custom map type powered by Pathom resolvers.",source:"@site/docs/smart-maps.mdx",sourceDirName:".",slug:"/smart-maps",permalink:"/docs/smart-maps",draft:!1,editUrl:"https://github.com/wilkerlucio/pathom3-docs/edit/master/docs/smart-maps.mdx",tags:[],version:"current",frontMatter:{id:"smart-maps",title:"Smart Maps"},sidebar:"docs",previous:{title:"Built-in Resolvers",permalink:"/docs/built-in-resolvers"},next:{title:"EQL",permalink:"/docs/eql"}},i={},c=[{value:"Using smart maps",id:"using-smart-maps",level:2},{value:"Nested maps",id:"nested-maps",level:3},{value:"Nested sequences",id:"nested-sequences",level:3},{value:"Error modes",id:"error-modes",level:3},{value:"Disable nested wrap",id:"disable-nested-wrap",level:3},{value:"Preload data",id:"preload-data",level:3},{value:"Keys modes",id:"keys-modes",level:2},{value:"Changes to smart maps",id:"changes-to-smart-maps",level:2},{value:"Inside smart maps",id:"inside-smart-maps",level:2},{value:"Debugging reads",id:"debugging-reads",level:2},{value:"Smart Maps caching",id:"smart-maps-caching",level:2},{value:"Datafy",id:"datafy",level:2}],l={toc:c},d="wrapper";function u(e){let{components:t,...a}=e;return(0,s.kt)(d,(0,n.Z)({},l,a,{components:t,mdxType:"MDXLayout"}),(0,s.kt)("p",null,"Smart maps are a custom map type powered by Pathom resolvers."),(0,s.kt)("p",null,"With smart maps, you can leverage the power of Pathom processing using the accessible\nstandard Clojure map interfaces."),(0,s.kt)("h2",{id:"using-smart-maps"},"Using smart maps"),(0,s.kt)("p",null,"To create a smart map, we need the indexes with resolvers, here is a basic example:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(ns com.wsscode.pathom.docs.smart-maps-demo\n  (:require [com.wsscode.pathom3.connect.indexes :as pci]\n            [com.wsscode.pathom3.connect.operation :as pco]\n            [com.wsscode.pathom3.interface.smart-map :as psm]))\n\n(pco/defresolver full-name [{::keys [first-name last-name]}]\n  {::full-name (str first-name " " last-name)})\n\n(def indexes (pci/register full-name))\n\n(def person-data {::first-name "Anne" ::last-name "Frank"})\n\n(def smart-map (psm/smart-map indexes person-data))\n\n; if you lookup for a key in the initial data, it works the same way as a regular map\n(::first-name smart-map) ; => "Anne"\n\n; but when you read something that\'s not there, it will trigger the Pathom engine to\n; fulfill the attribute\n(::full-name smart-map) ; => "Anne Frank"\n')),(0,s.kt)("p",null,"When you start a smart map, Pathom creates an atom with the initial data, and then when\nyou request some new information, Pathom triggers the resolver engine, merges the result\nin the same atom and returns the value for that key, effectively caching it."),(0,s.kt)("p",null,"This way, the subsequent accesses have the same speed as a local entry."),(0,s.kt)("h3",{id:"nested-maps"},"Nested maps"),(0,s.kt)("p",null,"Nested map values are wrapped with a smart map using the same environment configuration. For example:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(pco/defresolver full-name [{::keys [first-name last-name]}]\n  {::full-name (str first-name " " last-name)})\n\n(pco/defresolver anne []\n  {::anne {::first-name "Anne" ::last-name "Frank"}})\n\n(def indexes (pci/register [full-name anne]))\n\n(def smart-map (psm/smart-map indexes))\n\n(::anne smart-map) ; => {::first-name "Anne" ::last-name "Frank"}\n\n; nested access\n(-> smart-map ::anne ::full-name) ; => "Anne Frank"\n')),(0,s.kt)("admonition",{type:"important"},(0,s.kt)("p",{parentName:"admonition"},"This only applies to native Clojure/script maps. It doesn't wrap records and other\ncustom map types.")),(0,s.kt)("h3",{id:"nested-sequences"},"Nested sequences"),(0,s.kt)("p",null,"It also applies for maps inside sequences:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(pco/defresolver full-name [{::keys [first-name last-name]}]\n  {::full-name (str first-name " " last-name)})\n\n(pco/defresolver stars []\n  {::star-wars-characters\n   [{::first-name "Luke" ::last-name "Skywalker"}\n    {::first-name "Darth" ::last-name "Vader"}\n    {::first-name "Han" ::last-name "Solo"}]})\n\n(def indexes (pci/register [full-name stars]))\n\n(def smart-map (psm/smart-map indexes))\n\n; nested access on sequences\n(mapv ::full-name (::star-wars-characters smart-map))\n; => ["Luke Skywalker"\n;     "Darth Vader"\n;     "Han Solo"]\n')),(0,s.kt)("admonition",{type:"note"},(0,s.kt)("p",{parentName:"admonition"},"Smart maps do the conversion of sequence items to smart maps on the sequence read. This\nmeans that anytime you read a sequence on a smart map, if the sequence is a ",(0,s.kt)("inlineCode",{parentName:"p"},"vector"),", that\nscan will be done eagerly. Otherwise, it will use ",(0,s.kt)("inlineCode",{parentName:"p"},"map"),", and the processing is lazy.")),(0,s.kt)("h3",{id:"error-modes"},"Error modes"),(0,s.kt)("p",null,"By default, if some error happens during the Pathom process, the Smart Map is going\nto be silent about it. You can allow the errors to flow up by using the ",(0,s.kt)("inlineCode",{parentName:"p"},"psm/with-error-mode"),"\nhelper:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(pco/defresolver error-resolver []\n  {:error (throw (ex-info "Error" {}))})\n\n(let [sm (-> (pci/register error-resolver)\n             (psm/with-error-mode ::psm/error-mode-loud)\n             (psm/smart-map))]\n  (:error sm))\n; => Execution error (ExceptionInfo) at ...\n;    Error\n')),(0,s.kt)("p",null,"The options for error mode are:"),(0,s.kt)("ul",null,(0,s.kt)("li",{parentName:"ul"},(0,s.kt)("inlineCode",{parentName:"li"},"::psm/error-mode-silent")," (default): Return ",(0,s.kt)("inlineCode",{parentName:"li"},"nil")," as the value, don't throw errors."),(0,s.kt)("li",{parentName:"ul"},(0,s.kt)("inlineCode",{parentName:"li"},"::psm/error-mode-loud"),": Throw the errors on read.")),(0,s.kt)("h3",{id:"disable-nested-wrap"},"Disable nested wrap"),(0,s.kt)("p",null,"You can disable the automatic nest wrap using ",(0,s.kt)("inlineCode",{parentName:"p"},"(psm/with-wrap-nested? false)")," in the env."),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(psm/smart-map (-> indexes\n                   (psm/with-wrap-nested? false)\n  {:initial-data "value"}))\n')),(0,s.kt)("p",null,"Then all values will return as-is."),(0,s.kt)("admonition",{type:"tip"},(0,s.kt)("p",{parentName:"admonition"},"You can manually wrap nested values in the same way the library does by using the same\nenv on the value, for example:"),(0,s.kt)("pre",{parentName:"admonition"},(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},"(psm/smart-map (psm/sm-env smart-map) (::map-value smart-map))\n"))),(0,s.kt)("h3",{id:"preload-data"},"Preload data"),(0,s.kt)("p",null,"If you know the attributes you will need ahead of time, it's more efficient to load then\nin a single run than fetching one by one lazily."),(0,s.kt)("p",null,"You can accomplish this using the fn ",(0,s.kt)("inlineCode",{parentName:"p"},"psm/touch!"),", example:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},"(pco/defresolver right [{::keys [left width]}]\n  {::right (+ left width)})\n\n(pco/defresolver bottom [{::keys [top height]}]\n  {::bottom (+ top height)})\n\n(def indexes (pci/register [right bottom]))\n\n(def square {::left  10 ::top 30\n             ::width 23 ::height 35})\n\n(def smart-map\n  (-> (psm/smart-map indexes square)\n      (psm/sm-touch! [::right ::bottom])))\n\n(::right smart-map) ; => 33, read from cache\n(::bottom smart-map) ; => 65, read from cache\n")),(0,s.kt)("admonition",{type:"tip"},(0,s.kt)("p",{parentName:"admonition"},"You can also use ",(0,s.kt)("inlineCode",{parentName:"p"},"psm/sm-touch-ast!")," to provide an AST directly.")),(0,s.kt)("h2",{id:"keys-modes"},"Keys modes"),(0,s.kt)("p",null,"Smart Maps keys mode is a configuration to decide how a smart map respond to ",(0,s.kt)("inlineCode",{parentName:"p"},"(keys smart-map)")),(0,s.kt)("p",null,"To change this, use the helper ",(0,s.kt)("inlineCode",{parentName:"p"},"(psm/with-keys-mode ...)"),", these are the available options\nfor it:"),(0,s.kt)("ul",null,(0,s.kt)("li",{parentName:"ul"},(0,s.kt)("inlineCode",{parentName:"li"},"::psm/keys-mode-cached")," - the default option, ",(0,s.kt)("inlineCode",{parentName:"li"},"keys")," will return the keys cached\nin the internal smart map atom."),(0,s.kt)("li",{parentName:"ul"},(0,s.kt)("inlineCode",{parentName:"li"},"::psm/keys-mode-reachable")," - ",(0,s.kt)("inlineCode",{parentName:"li"},"keys")," will return all possible keys that are reachable\nfrom the current data and the index.")),(0,s.kt)("admonition",{type:"danger"},(0,s.kt)("p",{parentName:"admonition"},"Be careful with ",(0,s.kt)("inlineCode",{parentName:"p"},"::psm/keys-mode-reachable"),' combined with enabled "nested wrapping".\nConsidering that, depending on the index and the current data, a simple ',(0,s.kt)("inlineCode",{parentName:"p"},"print")," of the\nsmart map can lead to infinite loops due to smart maps' recursive properties.")),(0,s.kt)("h2",{id:"changes-to-smart-maps"},"Changes to smart maps"),(0,s.kt)("p",null,"You can use the change operations of maps in the smart map (",(0,s.kt)("inlineCode",{parentName:"p"},"assoc"),", ",(0,s.kt)("inlineCode",{parentName:"p"},"dissoc"),", ...)."),(0,s.kt)("p",null,"When a change operation happens, you get a new smart map. Be aware this new smart map\ndoesn't have the cached data from resolvers from the previous one. The modification is\ndone from the ",(0,s.kt)("inlineCode",{parentName:"p"},"source map"),", the one used to create the smart map in the first place."),(0,s.kt)("p",null,"This is important so any data computed by resolvers can react to the changes based on\nthe new data context. The following example illustrates this behavior:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},'(pco/defresolver full-name [{::keys [first-name last-name]}]\n  {::full-name (str first-name " " last-name)})\n\n(def indexes (pci/register full-name))\n\n(def person-data {::first-name "John" ::last-name "Lock"})\n\n(def smart-map (psm/smart-map indexes person-data))\n\n(::full-name smart-map) ; => "John Lock"\n\n(-> smart-map\n    (assoc ::last-name "Oliver")\n    ::full-name)\n; => "John Oliver", the full-name gets re-computed due to the change\n')),(0,s.kt)("h2",{id:"inside-smart-maps"},"Inside smart maps"),(0,s.kt)("p",null,"The smart map data structure contains an ",(0,s.kt)("a",{parentName:"p",href:"/docs/environment"},"environment")," inside of it, there\nyou can find the ",(0,s.kt)("a",{parentName:"p",href:"/docs/indexes"},"indexes"),", the cached data, and any other relevant options that\nmake the setup of the smart map."),(0,s.kt)("p",null,"You can access the smart map environment using ",(0,s.kt)("inlineCode",{parentName:"p"},"psm/sm-env"),". This includes all the data\nyou sent as ",(0,s.kt)("inlineCode",{parentName:"p"},"env")," during the smart map creation, plus the cache atom."),(0,s.kt)("h2",{id:"debugging-reads"},"Debugging reads"),(0,s.kt)("p",null,"Sometimes the result will be unexpected, to debug the smart map you can use the ",(0,s.kt)("inlineCode",{parentName:"p"},"psm/sm-get-with-stats"),"\nfunction to return the run stats of the process:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-clojure"},"(def indexes\n  (pci/register\n    [(pbir/constantly-resolver ::n 10)\n     (pbir/single-attr-resolver ::n ::x inc)\n     (pbir/single-attr-resolver ::x ::y #(* % 2))\n     (pbir/single-attr-resolver ::y ::z #(- % 10))]))\n\n(-> (psm/smart-map indexes)\n    (psm/sm-get-with-stats ::y))\n;{:com.wsscode.pathom3.connect.planner/index-attrs #:com.wsscode.pathom.docs.smart-maps-demo{:n #{3},\n;                                                                                            :y #{1},\n;                                                                                            :x #{2}},\n; :com.wsscode.pathom3.connect.planner/unreachable-attrs {},\n; :com.wsscode.pathom3.connect.runner/graph-process-duration-ms 0.11225199699401855,\n; :com.wsscode.pathom3.connect.runner.stats/overhead-duration-ms 0.09637504816055298,\n; :com.wsscode.pathom3.connect.planner/root 3,\n; :com.wsscode.pathom3.connect.runner.stats/overhead-duration-percentage 0.8585597650052356,\n; :com.wsscode.pathom3.connect.runner/node-run-stats {1 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.006672978401184082,\n;                                                                                            :node-run-input #:com.wsscode.pathom.docs.smart-maps-demo{:x 11}},\n;                                                     3 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.004477977752685547,\n;                                                                                            :node-run-input {}},\n;                                                     2 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.004725992679595947,\n;                                                                                            :node-run-input #:com.wsscode.pathom.docs.smart-maps-demo{:n 10}}},\n; :com.wsscode.pathom3.connect.planner/index-ast #:com.wsscode.pathom.docs.smart-maps-demo{:y {:key :com.wsscode.pathom.docs.smart-maps-demo/y,\n;                                                                                              :type :prop,\n;                                                                                              :dispatch-key :com.wsscode.pathom.docs.smart-maps-demo/y}},\n; :com.wsscode.pathom3.connect.planner/unreachable-resolvers #{},\n; :com.wsscode.pathom3.interface.smart-map/value 22,\n; :com.wsscode.pathom3.connect.planner/index-resolver->nodes {com.wsscode.pathom.docs.smart_maps_demo_SLASH_x->com.wsscode.pathom.docs.smart_maps_demo_SLASH_y-single-attr-transform #{1},\n;                                                             com.wsscode.pathom.docs.smart_maps_demo_SLASH_n-constant #{3},\n;                                                             com.wsscode.pathom.docs.smart_maps_demo_SLASH_n->com.wsscode.pathom.docs.smart_maps_demo_SLASH_x-single-attr-transform #{2}},\n; :com.wsscode.pathom3.connect.planner/nodes {1 {:com.wsscode.pathom3.connect.planner/after-nodes #{2},\n;                                                :com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:y {}},\n;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_x->com.wsscode.pathom.docs.smart_maps_demo_SLASH_y-single-attr-transform,\n;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/y},\n;                                                :com.wsscode.pathom3.connect.planner/input #:com.wsscode.pathom.docs.smart-maps-demo{:x {}},\n;                                                :com.wsscode.pathom3.connect.planner/node-id 1},\n;                                             3 {:com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:n {}},\n;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_n-constant,\n;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/n},\n;                                                :com.wsscode.pathom3.connect.planner/input {},\n;                                                :com.wsscode.pathom3.connect.planner/run-next 2,\n;                                                :com.wsscode.pathom3.connect.planner/node-id 3},\n;                                             2 {:com.wsscode.pathom3.connect.planner/after-nodes #{3},\n;                                                :com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:x {}},\n;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_n->com.wsscode.pathom.docs.smart_maps_demo_SLASH_x-single-attr-transform,\n;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/x},\n;                                                :com.wsscode.pathom3.connect.planner/input #:com.wsscode.pathom.docs.smart-maps-demo{:n {}},\n;                                                :com.wsscode.pathom3.connect.planner/run-next 1,\n;                                                :com.wsscode.pathom3.connect.planner/node-id 2}},\n; :com.wsscode.pathom3.connect.runner.stats/resolver-accumulated-duration-ms 0.015876948833465576}\n")),(0,s.kt)("h2",{id:"smart-maps-caching"},"Smart Maps caching"),(0,s.kt)("p",null,"Smart maps use a durable form of the cache by default. The smart maps add the caches\nat initialization, and they are persisted when variations of that Smart Map are created."),(0,s.kt)("p",null,"This means, for example, when you ",(0,s.kt)("inlineCode",{parentName:"p"},"assoc")," on a Smart Map, the new returned Smart Map still shares the same plan and resolver caches."),(0,s.kt)("h2",{id:"datafy"},"Datafy"),(0,s.kt)("p",null,"Smart Maps support the Clojure Datafy / Navigate protocols. This means if you use a REPL\nvisualizer like ",(0,s.kt)("a",{parentName:"p",href:"https://vlaaad.github.io/reveal/"},"Reveal")," or ",(0,s.kt)("a",{parentName:"p",href:"https://docs.datomic.com/cloud/other-tools/REBL.html"},"REBL"),"\nyou can navigate the projected data from the Smart Map lazily."),(0,s.kt)("p",null,"To demonstrate I'll be showing its usage with Reveal in the following video:"),(0,s.kt)(o.t,{videoId:"n_MJOKEqqnM",mdxType:"YoutubeVideo"}),(0,s.kt)("p",null,"Also in REBL:"),(0,s.kt)(o.t,{videoId:"v4vzdLRrkr8",mdxType:"YoutubeVideo"}))}u.isMDXComponent=!0}}]);