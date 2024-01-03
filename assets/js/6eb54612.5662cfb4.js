"use strict";(self.webpackChunkpathom_3_docs=self.webpackChunkpathom_3_docs||[]).push([[410],{3905:(e,t,n)=>{n.d(t,{Zo:()=>d,kt:()=>h});var a=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=a.createContext({}),p=function(e){var t=a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},d=function(e){var t=p(e.components);return a.createElement(s.Provider,{value:t},e.children)},m="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},c=a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,r=e.originalType,s=e.parentName,d=l(e,["components","mdxType","originalType","parentName"]),m=p(n),c=o,h=m["".concat(s,".").concat(c)]||m[c]||u[c]||r;return n?a.createElement(h,i(i({ref:t},d),{},{components:n})):a.createElement(h,i({ref:t},d))}));function h(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var r=n.length,i=new Array(r);i[0]=c;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l[m]="string"==typeof e?e:o,i[1]=l;for(var p=2;p<r;p++)i[p]=n[p];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}c.displayName="MDXCreateElement"},4441:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>i,default:()=>u,frontMatter:()=>r,metadata:()=>l,toc:()=>p});var a=n(7462),o=(n(7294),n(3905));const r={id:"tutorial",title:"Pathom Tutorial - IP Weather",sidebar_label:"Pathom Tutorial"},i=void 0,l={unversionedId:"tutorial",id:"tutorial",title:"Pathom Tutorial - IP Weather",description:"The task of this tutorial is to implement a series of resolvers that can tell the current",source:"@site/docs/tutorial.mdx",sourceDirName:".",slug:"/tutorial",permalink:"/docs/tutorial",draft:!1,editUrl:"https://github.com/wilkerlucio/pathom3-docs/edit/master/docs/tutorial.mdx",tags:[],version:"current",frontMatter:{id:"tutorial",title:"Pathom Tutorial - IP Weather",sidebar_label:"Pathom Tutorial"},sidebar:"docs",previous:{title:"Getting Started",permalink:"/docs/"},next:{title:"Nouns",permalink:"/docs/nouns"}},s={},p=[{value:"App Setup",id:"app-setup",level:2},{value:"Command Line Application",id:"command-line-application",level:2},{value:"Start from the tail",id:"start-from-the-tail",level:3},{value:"Resolvers",id:"resolvers",level:3},{value:"Graph Traversal",id:"graph-traversal",level:3},{value:"What&#39;s next",id:"whats-next",level:2}],d={toc:p},m="wrapper";function u(e){let{components:t,...r}=e;return(0,o.kt)(m,(0,a.Z)({},d,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"The task of this tutorial is to implement a series of resolvers that can tell the current\ntemperature, based on some IP address."),(0,o.kt)("p",null,"To implement this, I'll use ",(0,o.kt)("a",{parentName:"p",href:"https://get.geojs.io"},"GeoJS")," to find the location from some\ngiven IP, and then use ",(0,o.kt)("a",{parentName:"p",href:"http://www.7timer.info/doc.php?lang=en#api"},"7timer")," to find the temperature."),(0,o.kt)("h2",{id:"app-setup"},"App Setup"),(0,o.kt)("p",null,"In this demo, I'll be using ",(0,o.kt)("a",{parentName:"p",href:"https://clojure.org/guides/deps_and_cli"},"Clojure Deps")," to manage the dependencies:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:'title="deps.edn"',title:'"deps.edn"'},'{:paths\n ["src"]\n\n :deps\n {cheshire/cheshire   {:mvn/version "5.10.0"}\n  com.wsscode/pathom3 {:mvn/version "2022.10.19-alpha"}\n  org.clojure/clojure {:mvn/version "1.11.1"}\n  http-kit/http-kit   {:mvn/version "2.5.3"}}}\n')),(0,o.kt)("p",null,"We are going to use ",(0,o.kt)("inlineCode",{parentName:"p"},"http-kit")," to make our requests and ",(0,o.kt)("inlineCode",{parentName:"p"},"cheshire")," to parse ",(0,o.kt)("inlineCode",{parentName:"p"},"JSON")," from\nthe API responses."),(0,o.kt)("h2",{id:"command-line-application"},"Command Line Application"),(0,o.kt)("p",null,"To run our application, we will use the ",(0,o.kt)("inlineCode",{parentName:"p"},":exec-fn")," feature from ",(0,o.kt)("inlineCode",{parentName:"p"},"deps.edn"),", I'll start\nsetting up the main entry point and show how to trigger it from the command line:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(ns com.wsscode.pathom3.demos.ip-weather)\n\n(defn main [{:keys [ip]}]\n  (println "Request temperature for the IP" ip))\n')),(0,o.kt)("p",null,"To test this, run the following:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-bash"},"clj -X com.wsscode.pathom3.demos.ip-weather/main :ip '\"198.29.213.3\"'\n")),(0,o.kt)("p",null,"To make this command shorter, add an alias to ",(0,o.kt)("inlineCode",{parentName:"p"},"deps.edn"),":"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:'{10-13} title="deps.edn"',"{10-13}":!0,title:'"deps.edn"'},'{:paths\n ["src"]\n\n :deps\n {cheshire/cheshire   {:mvn/version "5.10.0"}\n  com.wsscode/pathom3 {:mvn/version "2022.10.19-alpha"}\n  org.clojure/clojure {:mvn/version "1.11.1"}\n  http-kit/http-kit   {:mvn/version "2.5.3"}}\n\n ; add alias to make easier to call\n :aliases\n {:ip-weather\n  {:exec-fn com.wsscode.pathom3.demos.ip-weather/main}}}\n')),(0,o.kt)("p",null,"Now we can run:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-bash"},"clj -X:ip-weather :ip '\"198.29.213.3\"'\n")),(0,o.kt)("p",null,"Scaffolding is done. Time to start writing some application logic."),(0,o.kt)("h3",{id:"start-from-the-tail"},"Start from the tail"),(0,o.kt)("p",null,"First, let's understand the data scenario for this task. If we start assuming that we know\nnothing about the services involved, we still know what we have (the IP) and what we\nwant (the temperature), we can start with this graph representation:"),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(6677).Z,width:"320",height:"82"}))),(0,o.kt)("p",null,"By looking at the documentation on the ",(0,o.kt)("a",{parentName:"p",href:"http://www.7timer.info/doc.php?lang=en#api"},"7timer"),",\nI see that the temperature information is present in the\n",(0,o.kt)("inlineCode",{parentName:"p"},"http://www.7timer.info/bin/api.pl?lon=$LONGITUDE$&lat=$LATITUDE$&product=astro&output=json")," endpoint\n. This means that to fetch the temperature, we need some ",(0,o.kt)("inlineCode",{parentName:"p"},"latitude")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"longitude"),"."),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(4745).Z,width:"639",height:"119"}))),(0,o.kt)("p",null,"Using the GeoJS API, we can use the endpoint ",(0,o.kt)("inlineCode",{parentName:"p"},"https://get.geojs.io/v1/ip/geo/$IP$.json"),"\nto figure the latitude and longitude, given some IP:"),(0,o.kt)("p",null,"Now we have the complete path from the ",(0,o.kt)("inlineCode",{parentName:"p"},"IP")," to the ",(0,o.kt)("inlineCode",{parentName:"p"},"temperature"),"."),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(1349).Z,width:"639",height:"119"}))),(0,o.kt)("admonition",{type:"tip"},(0,o.kt)("p",{parentName:"admonition"},"I found those API's using the ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/public-apis/public-apis"},"Public API's"),"\nservice, I find it a great source to look for open API's to play.")),(0,o.kt)("h3",{id:"resolvers"},"Resolvers"),(0,o.kt)("p",null,"In Pathom, resolvers are the main building blocks express attribute relationships."),(0,o.kt)("p",null,"To implement the resolvers, I'll start with the one to fetch the latitude and longitude\nfrom the IP:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(ns com.wsscode.pathom3.demos.ip-weather\n  (:require\n    [cheshire.core :as json]\n    [com.wsscode.pathom3.connect.operation :as pco]))\n\n(pco/defresolver ip->lat-long\n  [{:keys [ip]}]\n  {::pco/output [:latitude :longitude]}\n  (-> (slurp (str "https://get.geojs.io/v1/ip/geo/" ip ".json"))\n      (json/parse-string keyword)\n      (select-keys [:latitude :longitude])))\n\n(defn main [{:keys [ip]}]\n  (println "Request temperature for the IP" ip))\n')),(0,o.kt)("p",null,"A resolver is like a function, with some constraints:"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"The resolver input ",(0,o.kt)("strong",{parentName:"li"},"must")," be a map, so the input information is labeled."),(0,o.kt)("li",{parentName:"ol"},"A resolver ",(0,o.kt)("strong",{parentName:"li"},"must")," return a map, so the output information is labeled."),(0,o.kt)("li",{parentName:"ol"},"A resolver may also receive another map containing the environment information.")),(0,o.kt)("p",null,"To test the resolver in the REPL, call it like a function:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(ip->lat-long {:ip "198.29.213.3"})\n; => {:longitude "-88.0569", :latitude "41.5119"}\n')),(0,o.kt)("p",null,"A resolver is a custom type, here is what's inside:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:'title="ip->lat-long"',title:'"ip->lat-long"'},'#com.wsscode.pathom3.connect.operation.Resolver\n{:config\n #:com.wsscode.pathom3.connect.operation\n {:input [:ip]\n  :provides {:longitude {}\n             :latitude {}}\n  :output [:longitude\n           :latitude]\n  :op-name com.wsscode.pathom3.demos.ip-weather/ip->lat-long},\n\n :resolve\n #object[com.wsscode.pathom3.demos.ip_weather$ip__GT_lat_long__17350\n         0x4b7b5266\n         "com.wsscode.pathom3.demos.ip_weather$ip__GT_lat_long__17350@4b7b5266"]}\n')),(0,o.kt)("p",null,"Note that in the configuration map of the resolver, we have the same ",(0,o.kt)("inlineCode",{parentName:"p"},"::pco/output")," as\nwe wrote in the resolver, while the ",(0,o.kt)("inlineCode",{parentName:"p"},"::pco/input")," was inferred from the destructuring used in\nthe resolver attribute vector."),(0,o.kt)("p",null,"You can learn more about the details at ",(0,o.kt)("a",{parentName:"p",href:"/docs/resolvers"},"resolvers documentation page"),"."),(0,o.kt)("p",null,"Now that we have the latitude and longitude, the next resolver will find a ",(0,o.kt)("inlineCode",{parentName:"p"},"temperature"),"\nfrom them:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:"{5,14-23}","{5,14-23}":!0},'(ns com.wsscode.pathom3.demos.ip-weather\n  (:require\n    [cheshire.core :as json]\n    [com.wsscode.pathom3.connect.operation :as pco]\n    [org.httpkit.client :as http]))\n\n(pco/defresolver ip->lat-long\n  [{:keys [ip]}]\n  {::pco/output [:latitude :longitude]}\n  (-> (slurp (str "https://get.geojs.io/v1/ip/geo/" ip ".json"))\n      (json/parse-string keyword)\n      (select-keys [:latitude :longitude])))\n\n(pco/defresolver latlong->temperature\n  [{:keys [latitude longitude]}]\n  {:temperature\n   (-> @(http/request\n          {:url (str "http://www.7timer.info/bin/api.pl?lon=" longitude\n                     "&lat=" latitude\n                     "&product=astro&output=json")})\n       :body\n       (json/parse-string keyword)\n       :dataseries first :temp2m)})\n\n(defn main [{:keys [ip]}]\n  (println "Request temperature for the IP" ip))\n')),(0,o.kt)("admonition",{type:"note"},(0,o.kt)("p",{parentName:"admonition"},"In ",(0,o.kt)("inlineCode",{parentName:"p"},"latlong->temperature")," resolver, we let Pathom infer the output automatically. To use this\nfeature, remember that the last expression ",(0,o.kt)("strong",{parentName:"p"},"must")," be a map. Otherwise, Pathom will\nnot try to infer the output.")),(0,o.kt)("p",null,"Testing the resolver in the REPL:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(latlong->temperature {:longitude "-88.0569", :latitude "41.5119"})\n; => {:temperature -1}\n')),(0,o.kt)("p",null,"The whole process chains nicely, starting from ",(0,o.kt)("inlineCode",{parentName:"p"},"ip")," to ",(0,o.kt)("inlineCode",{parentName:"p"},"temperature"),", like this:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(-> {:ip "198.29.213.3"}\n    ip->lat-long\n    latlong->temperature)\n; => {:temperature -1}\n')),(0,o.kt)("h3",{id:"graph-traversal"},"Graph Traversal"),(0,o.kt)("p",null,"In the previous example, we were able to find the temperature starting from the IP. I\nlike to point all the names involved in the operation when we finished it, let's look\nat it again:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(-> {:ip "198.29.213.3"}\n    ip->lat-long\n    latlong->temperature)\n')),(0,o.kt)("p",null,"We have the ",(0,o.kt)("inlineCode",{parentName:"p"},":ip")," attribute in a map, and then we have ",(0,o.kt)("inlineCode",{parentName:"p"},"3")," function names,\nwhich dictates the step. Now we will replace all the resolver names with a single\nattribute: our data demand, the ",(0,o.kt)("inlineCode",{parentName:"p"},":temperature"),"."),(0,o.kt)("p",null,"It's time to leverage the attribute relations established from the resolvers."),(0,o.kt)("p",null,"To do this, Pathom needs some indexes that combine the attribute relations described\nby a list of resolvers. This is demonstrated in the highlighted fragments of the\nfollowing snippet:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:"{4,26-29}","{4,26-29}":!0},'(ns com.wsscode.pathom3.demos.ip-weather\n  (:require\n    [cheshire.core :as json]\n    [com.wsscode.pathom3.connect.indexes :as pci]\n    [com.wsscode.pathom3.connect.operation :as pco]\n    [org.httpkit.client :as http]))\n\n(pco/defresolver ip->lat-long\n  [{:keys [ip]}]\n  {::pco/output [:latitude :longitude]}\n  (-> (slurp (str "https://get.geojs.io/v1/ip/geo/" ip ".json"))\n      (json/parse-string keyword)\n      (select-keys [:latitude :longitude])))\n\n(pco/defresolver latlong->temperature\n  [{:keys [latitude longitude]}]\n  {:temperature\n   (-> @(http/request\n          {:url (str "http://www.7timer.info/bin/api.pl?lon=" longitude\n                     "&lat=" latitude\n                     "&product=astro&output=json")})\n       :body\n       (json/parse-string keyword)\n       :dataseries first :temp2m)})\n\n(def env\n  (pci/register [ip->lat-long\n                 latlong->temperature]))\n\n(defn main [{:keys [ip]}]\n  (println "Request temperature for the IP" ip))\n')),(0,o.kt)("p",null,"Using the indexes we generated at the name of ",(0,o.kt)("inlineCode",{parentName:"p"},"env"),", we can do the same processing\nwithout mentioning any resolver name, using a ",(0,o.kt)("a",{parentName:"p",href:"/docs/eql"},"EQL Processor"),":"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(p.eql/process env {:ip "198.29.213.3"} [:temperature])\n; 5.24\n')),(0,o.kt)("p",null,"Note the previous snippet doesn't include the name of any resolver!"),(0,o.kt)("p",null,"We can move that code to our ",(0,o.kt)("inlineCode",{parentName:"p"},"main")," function and make our program work:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure",metastring:"{6,32-35}","{6,32-35}":!0},'(ns com.wsscode.pathom3.demos.ip-weather\n  (:require\n    [cheshire.core :as json]\n    [com.wsscode.pathom3.connect.indexes :as pci]\n    [com.wsscode.pathom3.connect.operation :as pco]\n    [com.wsscode.pathom3.interface.eql :as p.eql]\n    [org.httpkit.client :as http]))\n\n(pco/defresolver ip->lat-long\n  [{:keys [ip]}]\n  {::pco/output [:latitude :longitude]}\n  (-> (slurp (str "https://get.geojs.io/v1/ip/geo/" ip ".json"))\n      (json/parse-string keyword)\n      (select-keys [:latitude :longitude])))\n\n(pco/defresolver latlong->temperature\n  [{:keys [latitude longitude]}]\n  {:temperature\n   (-> @(http/request\n          {:url (str "http://www.7timer.info/bin/api.pl?lon=" longitude\n                     "&lat=" latitude\n                     "&product=astro&output=json")})\n       :body\n       (json/parse-string keyword)\n       :dataseries first :temp2m)})\n\n(def env\n  (pci/register [ip->lat-long\n                 latlong->temperature]))\n\n(defn main [args]\n  (let [temp (p.eql/process-one env args :temperature)]\n    (println (str "It\'s currently " temp "C at " (pr-str args)))))\n')),(0,o.kt)("p",null,"Then we can run from the command line:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-bash"},'# some specific IP\nclj -X:ip-weather :ip \'"198.29.213.3"\'\n# => It\'s currently 8.33C at {:ip "198.29.213.3"}\n\n# get from your IP\nclj -X:ip-weather :ip "\\"$(curl -s ifconfig.me)\\""\n# => It\'s currently ??C at {:ip "YOUR_IP"}\n')),(0,o.kt)("p",null,"Magic? No, it's the power of graphs!"),(0,o.kt)("p",null,"To help understand how this works, have a look inside that ",(0,o.kt)("inlineCode",{parentName:"p"},"env")," variable we defined (the\nmap on the right side):"),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(2744).Z,width:"838",height:"337"}))),(0,o.kt)("admonition",{type:"note"},(0,o.kt)("p",{parentName:"admonition"},"For now, let's focus on the ",(0,o.kt)("inlineCode",{parentName:"p"},"index-oir"),", which is the main index used to traverse\ndependencies. Check the ",(0,o.kt)("a",{parentName:"p",href:"/docs/indexes"},"indexes page")," to learn more about the other\nindexes.")),(0,o.kt)("p",null,"When we request the ",(0,o.kt)("inlineCode",{parentName:"p"},":temperature"),", Pathom looks in the index for a path to that attribute.\nIt depends on ",(0,o.kt)("inlineCode",{parentName:"p"},":latitude")," and ",(0,o.kt)("inlineCode",{parentName:"p"},":longitude"),", which we don't have, but the index says you can\nget it if you provide an ",(0,o.kt)("inlineCode",{parentName:"p"},":ip"),", which is in the data context. It's the same path we described\nbefore with the table column associations."),(0,o.kt)("p",null,"The previous paragraph described the ",(0,o.kt)("strong",{parentName:"p"},"planning")," process. The output of that process\nis an execution graph that describes what it will take to fulfill the demand. Then it\nstarts running it, first figure ",(0,o.kt)("inlineCode",{parentName:"p"},":latitude")," and ",(0,o.kt)("inlineCode",{parentName:"p"},":longitude")," from ",(0,o.kt)("inlineCode",{parentName:"p"},":ip"),":"),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(9629).Z,width:"639",height:"119"}))),(0,o.kt)("p",null,"Then it can use ",(0,o.kt)("inlineCode",{parentName:"p"},":latitude")," and ",(0,o.kt)("inlineCode",{parentName:"p"},":longitude")," to get the ",(0,o.kt)("inlineCode",{parentName:"p"},":temperature"),":"),(0,o.kt)("div",{className:"pathom-diagram"},(0,o.kt)("p",null,"  ",(0,o.kt)("img",{src:n(6696).Z,width:"639",height:"119"}))),(0,o.kt)("p",null,"Because our code only talks about context and demand, we can also use this command\nline tool in a few other forms, like starting for latitude and longitude:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-bash"},'# from lat long\nclj -X:ip-weather :latitude \'"41.5119"\' :longitude \'"-88.0569"\'\n# => It\'s currently 8.33C at {:latitude "41.5119", :longitude "-88.0569"}\n')),(0,o.kt)("p",null,"As long as you use some data that has a path to the temperature, it works."),(0,o.kt)("h2",{id:"whats-next"},"What's next"),(0,o.kt)("p",null,"This concludes this tutorial. A quick review:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Map the available data you have and the data you want in terms of attributes."),(0,o.kt)("li",{parentName:"ul"},"Write resolvers connecting the attribute names, adding more attributes as needed."),(0,o.kt)("li",{parentName:"ul"},"Prepare an environment with the ",(0,o.kt)("a",{parentName:"li",href:"/docs/indexes"},"indexes"),"."),(0,o.kt)("li",{parentName:"ul"},"Use an ",(0,o.kt)("a",{parentName:"li",href:"/docs/eql"},"EQL Processor")," to make the information request.")),(0,o.kt)("p",null,"I designed this demo to illustrate the basic concepts of attribute modeling and Pathom."),(0,o.kt)("p",null,"Here are a few exercise suggestions you can do to extend this demo:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Add a resolver to tell if the temperature is cold or not, based on some cold threshold"),(0,o.kt)("li",{parentName:"ul"},"Add a resolver to use the current user public IP when nothing else is provided.")))}u.isMDXComponent=!0},2744:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/index-oir-connected-b6fd49ab417fa5fc6a1467d685e86d3c.png"},6677:(e,t,n)=>{n.d(t,{Z:()=>a});const a="data:image/png;base64,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"},4745:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/table-plan-1-2f0fb83a0f43186235d4e333d30d4760.png"},1349:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/table-plan-2-1a8e40192d6ec22c13a4d6db548c21e1.png"},9629:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/table-run-1-22d6ff719af895ce86db9b8da06fc98c.png"},6696:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/table-run-2-d820d7ffb0646f4bf99e199b58e8fdab.png"}}]);