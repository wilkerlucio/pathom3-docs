"use strict";(self.webpackChunkpathom_3_docs=self.webpackChunkpathom_3_docs||[]).push([[723],{3905:(e,t,n)=>{n.d(t,{Zo:()=>c,kt:()=>h});var a=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=a.createContext({}),p=function(e){var t=a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},c=function(e){var t=p(e.components);return a.createElement(s.Provider,{value:t},e.children)},u="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},d=a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,r=e.originalType,s=e.parentName,c=i(e,["components","mdxType","originalType","parentName"]),u=p(n),d=o,h=u["".concat(s,".").concat(d)]||u[d]||m[d]||r;return n?a.createElement(h,l(l({ref:t},c),{},{components:n})):a.createElement(h,l({ref:t},c))}));function h(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var r=n.length,l=new Array(r);l[0]=d;var i={};for(var s in t)hasOwnProperty.call(t,s)&&(i[s]=t[s]);i.originalType=e,i[u]="string"==typeof e?e:o,l[1]=i;for(var p=2;p<r;p++)l[p]=n[p];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}d.displayName="MDXCreateElement"},6923:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>l,default:()=>m,frontMatter:()=>r,metadata:()=>i,toc:()=>p});var a=n(7462),o=(n(7294),n(3905));const r={title:"Serverless Pathom with GCF"},l=void 0,i={unversionedId:"tutorials/serverless-pathom-gcf",id:"tutorials/serverless-pathom-gcf",title:"Serverless Pathom with GCF",description:"In this tutorial, we are going to implement a Pathom server and deploy it",source:"@site/docs/tutorials/serverless-pathom-gcf.mdx",sourceDirName:"tutorials",slug:"/tutorials/serverless-pathom-gcf",permalink:"/docs/tutorials/serverless-pathom-gcf",draft:!1,editUrl:"https://github.com/wilkerlucio/pathom3-docs/edit/master/docs/tutorials/serverless-pathom-gcf.mdx",tags:[],version:"current",frontMatter:{title:"Serverless Pathom with GCF"},sidebar:"docs",previous:{title:"Hacker News Scraper",permalink:"/docs/tutorials/hacker-news-scraper"},next:{title:"Scripting with Babashka",permalink:"/docs/tutorials/babashka"}},s={},p=[{value:"Project Setup",id:"project-setup",level:2},{value:"Recommended Settings for Pathom",id:"recommended-settings-for-pathom",level:2},{value:"Cache plan results",id:"cache-plan-results",level:3},{value:"Ring handler setup",id:"ring-handler-setup",level:2},{value:"GCF Deploy",id:"gcf-deploy",level:2}],c={toc:p},u="wrapper";function m(e){let{components:t,...n}=e;return(0,o.kt)(u,(0,a.Z)({},c,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"In this tutorial, we are going to implement a Pathom server and deploy it\nas a ",(0,o.kt)("a",{parentName:"p",href:"https://cloud.google.com/functions"},"Google Cloud Function"),"."),(0,o.kt)("p",null,"We will start with the code created at the ",(0,o.kt)("a",{parentName:"p",href:"/docs/tutorial"},"Pathom Tutorial"),", and make\nit available as a service."),(0,o.kt)("h2",{id:"project-setup"},"Project Setup"),(0,o.kt)("p",null,"To make the GCP integration I'll use the library ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/pepijn/google-cloud-functions-clojure"},"google-cloud-functions-ring-adapter"),"."),(0,o.kt)("p",null,"Example setup for ",(0,o.kt)("inlineCode",{parentName:"p"},"deps.edn"),":"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'{:paths\n ["src/main" "src/java"]\n\n :deps\n {com.wsscode/pathom3                         {:mvn/version "2021.07.10-alpha"}\n  metosin/muuntaja                            {:mvn/version "0.6.8"}\n  nl.epij/google-cloud-functions-ring-adapter {:mvn/version "0.1.0"}\n  org.clojure/clojure                         {:mvn/version "1.10.3"}\n  org.clojure/core.async                      {:mvn/version "1.3.618"}\n  ring-cors/ring-cors                         {:mvn/version "0.1.13"}}\n\n :aliases\n {:assemble\n  {:extra-deps {nl.epij.gcf/deploy {:git/url   "https://github.com/pepijn/google-cloud-functions-clojure"\n                                    :sha       "e0f49db974a8f97e1459efd16c0edfb3030a6115"\n                                    :deps/root "deploy"}}\n   :exec-fn    nl.epij.gcf.deploy/assemble-jar!\n   :exec-args  {:nl.epij.gcf/entrypoint   PathomServer\n                :nl.epij.gcf/java-paths   ["src/java"]\n                :nl.epij.gcf/compile-path "target/classes"\n                :nl.epij.gcf/jar-path     "target/artifacts/application.jar"}}\n  :run\n  {:extra-deps {nl.epij.gcf/deploy {:git/url   "https://github.com/pepijn/google-cloud-functions-clojure"\n                                    :sha       "e0f49db974a8f97e1459efd16c0edfb3030a6115"\n                                    :deps/root "deploy"}}\n   :exec-fn    nl.epij.gcf.deploy/run-server!\n   :exec-args  {:nl.epij.gcf/entrypoint   PathomServer\n                :nl.epij.gcf/java-paths   ["src/java"]\n                :nl.epij.gcf/compile-path "target/classes"\n                :nl.epij.gcf/jar-path     "target/artifacts/application.jar"}}}}\n')),(0,o.kt)("h2",{id:"recommended-settings-for-pathom"},"Recommended Settings for Pathom"),(0,o.kt)("p",null,"Here are some recommended settings to do when exposing a Pathom API."),(0,o.kt)("h3",{id:"cache-plan-results"},"Cache plan results"),(0,o.kt)("p",null,"It's common for clients to make the same EQL requests. Pathom can leverage this and\npersist the planning part of the process across requests. This is how we set it up:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},"; create a var to store the cache\n(defonce plan-cache* (atom {}))\n\n(def env\n  ; persistent plan cache\n  (-> {::pcp/plan-cache* plan-cache*}\n      (pci/register\n        [ip->lat-long\n         latlong->woeid\n         woeid->temperature])))\n")),(0,o.kt)("p",null,"Check the ",(0,o.kt)("a",{parentName:"p",href:"/docs/cache"},"Cache")," page for more details on how to control the cache."),(0,o.kt)("h2",{id:"ring-handler-setup"},"Ring handler setup"),(0,o.kt)("p",null,"Now let's set up a ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/ring-clojure/ring"},"Ring")," handler. The two main things\nfor this:"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Setup content negotiation to decode/encode data, I'll use ",(0,o.kt)("a",{parentName:"li",href:"https://github.com/metosin/muuntaja"},"muuntaja"),"."),(0,o.kt)("li",{parentName:"ol"},"Handle the request using the ",(0,o.kt)("a",{parentName:"li",href:"/docs/eql#boundary-interface"},"Pathom Boundary Interface"),".")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-clojure"},'(ns com.wsscode.pathom-server\n  (:require\n    [cognitect.transit :as t]\n    ; to include the env setup from the Tutorial demo\n    [com.wsscode.pathom3.demos.ip-weather :refer [env]]\n\n    [com.wsscode.pathom3.connect.operation.transit :as pcot]\n    [com.wsscode.pathom3.interface.eql :as p.eql]\n    [muuntaja.core :as muuntaja]\n    [muuntaja.middleware :as middleware]))\n\n; create a boundary interface\n(def pathom (p.eql/boundary-interface env))\n\n(defn handler [{:keys [body-params]}]\n  {:status 200\n   :body   (pathom body-params)})\n\n(def muuntaja-options\n  (update-in\n    muuntaja/default-options\n    [:formats "application/transit+json"]\n    ; in this part we setup the read and write handlers for Pathom resolvers and mutations\n    merge {:decoder-opts {:handlers pcot/read-handlers}\n           :encoder-opts {:handlers  pcot/write-handlers\n                          ; write-meta is required if you wanna see execution stats on Pathom Viz\n                          :transform t/write-meta}}))\n\n(def app\n  (-> handler\n      (middleware/wrap-format muuntaja-options)))\n')),(0,o.kt)("p",null,"The ",(0,o.kt)("inlineCode",{parentName:"p"},":transform t/write-meta"),' will make transit encode also the meta-data. This means the\nrunning status data will flow. It allows Pathom Viz to show debug information.\nKeep in mind the "run status" data is usually much larger than the response itself.'),(0,o.kt)("p",null,"You can mitigate this in two different levels at Pathom:"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Set ",(0,o.kt)("inlineCode",{parentName:"li"},":com.wsscode.pathom3.connect.runner/omit-run-stats-resolver-io? true")," to remove input/output details. This adds a great reduction in the size of the status."),(0,o.kt)("li",{parentName:"ol"},"Set ",(0,o.kt)("inlineCode",{parentName:"li"},":com.wsscode.pathom3.connect.runner/omit-run-stats? true")," to remove all status from meta")),(0,o.kt)("admonition",{type:"tip"},(0,o.kt)("p",{parentName:"admonition"},"This same handler setup works with any other ring server, like ",(0,o.kt)("a",{parentName:"p",href:"http://pedestal.io/"},"Pedestal"),",\n",(0,o.kt)("a",{parentName:"p",href:"https://http-kit.github.io/"},"http-kit"),", ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/weavejester/compojure"},"Compojure"),",\netc...")),(0,o.kt)("p",null,"The boundary interface isn't required, but it gives the clients extra capabilities like allowing the user to provide root data."),(0,o.kt)("p",null,"Now to hook that, we need to create a Java file in our sources that will link our handler:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-java"},'import nl.epij.gcf.RingHttpFunction;\n\npublic class PathomServer extends RingHttpFunction {\n    public String getHandler() {\n        return "com.wsscode.pathom-server/app";\n    }\n}\n')),(0,o.kt)("p",null,"After this part, we can test our server locally:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"PORT=13337 clojure -X:run\n")),(0,o.kt)("p",null,"Once it runs, we can test it by sending a request:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"curl --location --request POST 'http://localhost:13337' \\\n--header 'Content-Type: application/edn' \\\n--header 'Accept: application/edn' \\\n--data-raw '{:pathom/eql [:temperature], :pathom/entity {:ip \"198.29.213.3\"}}'\n\n# => {:temperature 26.064999999999998}\n")),(0,o.kt)("admonition",{type:"important"},(0,o.kt)("p",{parentName:"admonition"},"I used EDN fore readability, but you should use ",(0,o.kt)("inlineCode",{parentName:"p"},"application/transit+json")," (with transit data) for performance\nand to support the custom handlers for resolvers and mutations.")),(0,o.kt)("h2",{id:"gcf-deploy"},"GCF Deploy"),(0,o.kt)("p",null,"For the next steps, you need to have ",(0,o.kt)("a",{parentName:"p",href:"https://accounts.google.com/signin/v2/identifier?service=cloudconsole&passive=1209600&osid=1&continue=https%3A%2F%2Fconsole.cloud.google.com%2Ffreetrial%2Fsignup%2Ftos%3Fref%3Dhttps%3A%2F%2Fwww.google.com%2F&followup=https%3A%2F%2Fconsole.cloud.google.com%2Ffreetrial%2Fsignup%2Ftos%3Fref%3Dhttps%3A%2F%2Fwww.google.com%2F&flowName=GlifWebSignIn&flowEntry=ServiceLogin"},"GCP Account"),"\nand install the ",(0,o.kt)("a",{parentName:"p",href:"https://cloud.google.com/sdk/docs/install"},"Google Cloud SDK"),"."),(0,o.kt)("admonition",{type:"tip"},(0,o.kt)("p",{parentName:"admonition"},"On Mac, you can install GCP with ",(0,o.kt)("inlineCode",{parentName:"p"},"brew install google-cloud-sdk"))),(0,o.kt)("p",null,"To deploy the handler as Google Cloud Function; first we assemble the jar:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"clojure -X:assemble\n")),(0,o.kt)("p",null,"Deploy to GCP (tune as you see fit):"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"gcloud functions deploy --runtime java11 --source target/artifacts/ --max-instances 1 development-pathom-server-demo --trigger-http --allow-unauthenticated --entry-point PathomServer --memory 2GB --timeout 270\n")),(0,o.kt)("p",null,"If you go through with no errors, the function should be online!"),(0,o.kt)("admonition",{type:"note"},(0,o.kt)("p",{parentName:"admonition"},"You can find out the URL to try it in the message output after deploy.\nLook for ",(0,o.kt)("inlineCode",{parentName:"p"},"httpsTrigger: ... url:"))),(0,o.kt)("p",null,"You can find the full sources of this demo at ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/wilkerlucio/pathom3-demo-gcf"},"https://github.com/wilkerlucio/pathom3-demo-gcf"),"."))}m.isMDXComponent=!0}}]);