(ns blog.template.layout
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-rss.core :as rss]))

(defn menu [dates]
  (->> dates
       (mapcat
              (fn [{:keys [:slug :date :short-name]}]
                [[:a {:href (str (name slug) ".html")} [:span.nav-item-title short-name]]
                 [:span.nav-item-date  (f/unparse (f/formatters :year-month-day) date)]
                 [:br]]))
       (into [:div#menu ])))

(defn modal-menu [dates]
  (->> dates
       (mapcat
              (fn [{:keys [:slug :date :short-name]}]
                [[:a {:href (str (name slug) ".html")} [:span.modal-nav-item-title short-name]]
                 [:span.modal-nav-item-date (str "(" (f/unparse (f/formatters :year-month-day) date) ")")]
                 [:br]]))
       (into [:div.modal-menu-items ])))

(defn front-page-hiccup
  [blog-structure]
  [:div.index-container
   (->> blog-structure
        (into '())
        (sort-by (fn [[slug {:keys [:date]}]] date))
        (map (fn [[slug {:keys [:title :subtitle :date :tags :thumb]}]]
               [:div.index-item
                [:div.index-title [:a {:href (str (name slug) ".html")} title]]
                (when tags
                  [:div.index-tags (map (fn [tag]
                                          (if (<= 7 (count tag))
                                            [:span.index-tag.nowrap tag]
                                            [:span.index-tag tag])) tags)])
                [:div.index-date (f/unparse (f/formatters :year-month-day) date)]
                (when thumb
                  (into [:div.index-thumb] thumb))
                [:div.index-description subtitle]
                [:div.continue-reading [:a {:href (str (name slug) ".html")} "Continue Reading »"]]])))])

(def short-description
  "A place for tech and numerical experimentalism. Be welcome.")

(def twitter-el
  [:a.twitter-share-button
   {:href "https://twitter.com/share" :data-via "fjmarujo" :data-size "large"} "Tweet"
   [:script
    "!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');"]])

(def disqus-el
  [:div#disqus_thread
   [:script
    {:type "text/javascript"}
    "/* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */\nvar disqus_shortname = 'this-data'; // required: replace example with your forum shortname\n/* * * DON'T EDIT BELOW THIS LINE * * */\n(function() {\n    var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;\n    dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';\n    (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);\n})();"]
   [:noscript
    "Please enable JavaScript to view the"
    [:a
     {:href "https://disqus.com/?ref_noscript"}
     "comments powered by Disqus."]]])

(defn layout [title description content menu modal-menu twitter-el disqus-el]
  [:html
   {:lang "en"}
   [:head
    [:meta
     {:content "width=device-width, initial-scale=1, maximum-scale=1" :name "viewport"}]
    [:meta {:name "description" :content description}]
    [:title title]
    [:link {:href "assets/css/font.css", :rel "stylesheet"}]
    [:link {:href "//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" :rel "stylesheet"}]
    [:link {:href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.4/styles/github.min.css" :rel "stylesheet"}]
    [:link {:type "text/css" :href "//cdn-images.mailchimp.com/embedcode/slim-081711.css" :rel "stylesheet"}]
    [:link {:href "assets/css/style.css" :rel "stylesheet"}]
    [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-MML-AM_CHTML"
              :async "async":type "text/javascript"}]]

   [:body
    [:div#desktop-source.source [:a {:target "_blank" :href "https://github.com/data-journal/data-journal.github.io"} "Source"]]
    [:div#side-bar

     ;;modal
     [:div#menu-modal.menu-modal
      [:div.modal-content
       [:span.menu-close "×"]
       modal-menu]]
     ;;modal

     [:button#menu-btn [:i.fa.fa-bars {:aria-hidden "true"}]]
     [:div#header

      [:div#title [:a {:href "/"} "Data Journal"]]
      [:div#author [:div#name "Flávio Sousa"]

       [:div#social-media
        [:a {:target "_blank" :href "//pt.linkedin.com/pub/flávio-sousa/3a/a06/770/"}
         [:i.fa.fa-linkedin]]
        [:a {:target "_blank", :href "//twitter.com/verysocialsousa"}
         [:i.fa.fa-twitter]]
        [:a {:target "_blank", :href "//github.com/fjsousa"}
         [:i.fa.fa-github]]]]]

     menu

     [:div#footer-bar

      [:div#mc_embed_signup
       [:div.rss-feed [:a {:href "/feed.xml"}  [:i.fa.fa-rss {:aria-hidden "true"}]]]
       [:form#mc-embedded-subscribe-form.validate
        {:target "_blank"
         :action "//github.us10.list-manage.com/subscribe/post?u=5b26850668dc6b3f84778ca5e&id=cb5f4eedfe"
         :name "mc-embedded-subscribe-form"
         :novalidate "",
         :method "post"}
        [:div#mc_embed_signup_scroll
         [:label {:for "mce-EMAIL"} "Subscribe to the mailing list"]
         [:input#mce-EMAIL.email
          {:required "",
           :value "",
           :type "email",
           :placeholder "email address",
           :name "EMAIL"}]
         ;;real people should not fill this in and expect good things - do not remove this or risk form bot signups
         [:div
          {:style "position: absolute; left: -5000px;"}
          [:input
           {:value "",
            :type "text",
            :name "b_5b26850668dc6b3f84778ca5e_cb5f4eedfe",
            :tabindex "-1"}]]
         [:div.clear
          [:input#mc-embedded-subscribe.button
           {:value "Subscribe", :type "submit", :name "subscribe"}]]
         [:div#mobile-source.source [:a {:target "_blank" :href "https://github.com/data-journal/data-journal.github.io"} "Source"]]]]]

      [:div#about
       [:p#about-title "About:"]
       [:p short-description]]]]

    [:div#container
     [:div#content

      content

      twitter-el

      disqus-el]]

    [:div#footer]
    [:script
     "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n  ga('create', 'UA-53583095-1', 'auto');\n  ga('send', 'pageview');\n"]
    [:script {:src "//code.jquery.com/jquery-1.11.2.min.js"}]
    [:script {:type "text/javascript" :src "assets/src/modal.js"}]
    [:script {:type "text/javascript" :src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.4/highlight.min.js"}]
    [:script "hljs.initHighlightingOnLoad();"]
    ;;cellular automata post
    [:script {:type "text/javascript" :src "assets/src/rags.js"}]
    [:script {:type "text/javascript" :src "assets/src/fgm-main.js"}]
    ;;cellular automata post
    ]])

(defn sitemap [root blog-structure]
  (->> blog-structure
       (map (fn [[k v]] (name k)))
       (map (fn [slug] (str root "/" slug ".html")))
       (reduce (fn [map-str page] (str map-str page "\n")) (str root "\n"))))

(def index-title
  (str "Data Journal - " short-description))

(def index-description
  "This is a blog about Numerical Algorithms and Software Engineering. I write about experiments I did or
  ongoing work which I think is relevant to the subject. Subscribe to the newsletter to get the latest updates. I won't blog that frequently so don't worry, you won't get spammed!")


(defn rss-feed
  [base-url blog-structure]
  (->> blog-structure
       (map (fn [[slug {:keys [:title :subtitle :date :tags :thumb]}]]
              {:title title
               :link (str base-url "/" (name slug) ".html")
               :description subtitle
               :category tags
               :pubDate (c/to-date date)}))
       (apply rss/channel-xml)))
